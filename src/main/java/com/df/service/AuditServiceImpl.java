package com.df.service;

import com.df.entity.Audit;
import com.df.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    public <T> Mono<Audit> create(T entityValue) {
        return auditRepository.save(createByEntityObject(entityValue))
                .doOnSuccess(createdAuditEntity -> log.info("Created " + createdAuditEntity + " audit."))
                .doOnError(e -> log.info("Failed to create audit " + entityValue + ".", e))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public <T> Flux<Audit> createAll(List<T> entitiesValue) {
        return Flux.fromIterable(entitiesValue)
                .map(this::createByEntityObject)
                .collectList()
                .flatMap(audits -> auditRepository.saveAll(audits)
                        .collectList()
                        .doOnSuccess(createdAuditEntity -> log.info("Created " + createdAuditEntity + " audits."))
                        .doOnError(e -> log.info("Failed to create audits " + entitiesValue + ".", e))
                        .subscribeOn(Schedulers.boundedElastic())
                        .then(Mono.just(audits)))
                .flatMapMany(Flux::fromIterable)
                .map(p -> p);
    }

    private <T> Audit createByEntityObject(T entityObject) {
        return Audit.builder()
                .id(null)
                .entityClass(entityObject.getClass().getName())
                .entityValue(entityObject.toString())
                .auditedOn(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }

}
