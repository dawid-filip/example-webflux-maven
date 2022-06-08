package com.df.annotation;

import com.df.entity.Audit;
import com.df.entity.Owner;
import com.df.entity.Pet;
import com.df.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j2
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(com.df.annotation.Audit)")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object monoOrFlux;

        try {
            monoOrFlux = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        if (monoOrFlux instanceof Mono) {
            Mono<?> mono = (Mono<?>) monoOrFlux;
            return doWhenMono(mono);
        } else if (monoOrFlux instanceof Flux) {
            Flux<?> flux = (Flux<?>) monoOrFlux;
            doWhenFlux(Flux.from(flux))
                    .subscribe();
            return flux;
        }

        return monoOrFlux;
    }

    private Flux<?> doWhenFlux(Flux<?> flux) {
        return flux.flatMap(entity ->
                        doWhenMono(Mono.just(entity)))
                .map(p -> p);
    }

    private Mono<?> doWhenMono(Mono<?> mono) {
        return mono.doOnSuccess(entity -> {
            dispatcherEntity(entity);
        });
    }

    private void dispatcherEntity(Object entity) {
        if (entity instanceof Pet) {
            auditService.create(entity)
                    .subscribe();
        } else if (entity instanceof Owner) {
            auditService.create(entity)
                    .subscribe();
        } else if (entity instanceof Audit) {
            log.warn("Entity cannot be type of Audit!");
            return;
        }
    }

}
