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
        List<BasicAudit> basicAuditsArgs = extractEntitiesIfAny(methodArgs);

        if (basicAuditsArgs.isEmpty()) return monoOrFlux;

        if (monoOrFlux instanceof Mono<?>) {
            Mono<?> mono = (Mono<?>) monoOrFlux;
            return doOnMono(mono, basicAuditsArgs);
        } else if (monoOrFlux instanceof Flux<?>) {
            Flux<?> flux = (Flux<?>) monoOrFlux;
            return doOnFlux(flux, basicAuditsArgs);
        }

        return monoOrFlux;
    }

    private Mono<?> doOnMono(Mono<?> mono, List<BasicAudit> basicAuditsArgs) {
        Mono.fromCallable(() -> delegateAuditCreation(basicAuditsArgs))
                .subscribe();
        return mono;
    }

    private Flux<?> doOnFlux(Flux<?> flux, List<BasicAudit> basicAuditsArgs) {
        Mono.fromCallable(() -> delegateAuditCreation(basicAuditsArgs))
                .subscribe();
        return flux;
    }

    private List<BasicAudit> delegateAuditCreation(List<BasicAudit> basicAuditsArgs) {
        if (basicAuditsArgs.size() == 1) {
            auditService.create(basicAuditsArgs.get(0))
                .subscribe();
        } else if (basicAuditsArgs.size() > 1) {
            auditService.createAll(basicAuditsArgs)
                .subscribe();
        } else {
            log.warn("Audit object entity is empty. Nothing to do..");
        }

        return basicAuditsArgs;
    }

    private List<BasicAudit> extractEntitiesIfAny(Object[] methodArgs) {
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
