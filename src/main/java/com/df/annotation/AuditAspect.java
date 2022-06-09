package com.df.annotation;

import com.df.entity.BasicAudit;
import com.df.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j2
public class AuditAspect {

    private final AuditService auditService;

    @Around(value = "@annotation(com.df.annotation.Audit)")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object monoOrFlux;

        try {
            monoOrFlux = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        Object[] methodArgs = Optional.of(joinPoint.getArgs()).orElse(List.of().toArray());
        List<BasicAudit> basicAuditsArgs = checkIfEntities(methodArgs);

        if (basicAuditsArgs.isEmpty()) return monoOrFlux;

        if (monoOrFlux instanceof Mono) {
            Mono<BasicAudit> mono = (Mono<BasicAudit>) monoOrFlux;
            doWhenMono(mono, basicAuditsArgs)
                    .subscribe();
            return mono;
        } else if (monoOrFlux instanceof Flux) {
            Flux<BasicAudit> flux = (Flux<BasicAudit>) monoOrFlux;
            doWhenFlux(flux, basicAuditsArgs)
                    .subscribe();
            return flux;
        }

        return monoOrFlux;
    }

    private Flux<BasicAudit> doWhenFlux(Flux<BasicAudit> flux, List<BasicAudit> basicAuditsArgs) {
        return flux.collectList()
                .doOnSuccess(basic ->
                    delegateAuditCreation(basicAuditsArgs)
                )
                .flatMapMany(Flux::fromIterable)
                .map(p -> p);
    }

    private Mono<BasicAudit> doWhenMono(Mono<BasicAudit> mono, List<BasicAudit> basicAuditsArgs) {
        return mono.doOnSuccess(basic ->
            delegateAuditCreation(basicAuditsArgs)
        );
    }

    private void delegateAuditCreation(List<BasicAudit> basicAuditsArgs) {
        if (basicAuditsArgs.size() == 1) {
            auditService.create(basicAuditsArgs.get(0))
                    .subscribe();
        } else if (basicAuditsArgs.size() > 1) {
            auditService.createAll(basicAuditsArgs)
                    .subscribe();
        } else {
            //
        }
    }

    private List<BasicAudit> checkIfEntities(Object[] methodArgs) {
        List<BasicAudit> basicAudits = new ArrayList<>();

        if (methodArgs != null && methodArgs[0] instanceof BasicAudit) {

            BasicAudit basicAuditEntity = (BasicAudit) methodArgs[0];
            basicAudits.add(basicAuditEntity);

        } else if (methodArgs != null && methodArgs[0] instanceof List<?>) {
            List<BasicAudit> basicAuditEntities = (List<BasicAudit>) methodArgs[0];
            basicAudits.addAll(basicAuditEntities);

        }

        return basicAudits;
    }

}
