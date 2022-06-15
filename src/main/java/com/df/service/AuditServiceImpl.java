package com.df.service;

import com.df.entity.Audit;
import com.df.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public <T> Mono<Audit> create(T entityValue) {
        return auditRepository.save(createByEntityObject(entityValue))
                .doOnSuccess(createdAuditEntity -> log.info("Created " + createdAuditEntity + " audit."))
                .doOnError(e -> log.info("Failed to create audit " + entityValue + ".", e))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @Transactional
    public <T> Flux<Audit> createAll(List<T> entitiesValue) {
        return Flux.fromIterable(entitiesValue)
                .map(this::createByEntityObject)
                .collectList()
                .flatMap(audits -> auditRepository.saveAll(audits)
                                .collectList()
                                .doOnSuccess(createdAuditEntities -> log.info("Created " + createdAuditEntities + " audits."))
                                .doOnError(e -> log.info("Failed to create audits " + entitiesValue + ".", e))
                                .then(Mono.just(audits)))
                                .subscribeOn(Schedulers.boundedElastic()
                )
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

    @Override
    public Flux<Audit> getAll() {
        return auditRepository.findAll()
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<Audit> getById(Long id) {
        return Mono.justOrEmpty(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(checkedId ->
                        auditRepository.findById(id));
    }

    @Override
    public Flux<Audit> getBetweenIds(Long startId, Long endId) {
        return auditRepository.findBetweenIds(startId, endId);
    }

    @Override
    public Flux<Audit> getBetweenAuditedOns(LocalDateTime startAuditedOn, LocalDateTime endAuditedOn) {
        return auditRepository.findBetweenAuditedOns(startAuditedOn, endAuditedOn);
    }

    @Override
    public Flux<Audit> getLikeEntityClass(String entityClass) {
        return Mono.justOrEmpty(entityClass)
                .map(textEntityClass -> new StringBuilder("%")
                        .append(textEntityClass)
                        .append("%")
                        .toString())
                .flatMapMany(entityClassLike ->
                        auditRepository.findLikeEntityClass(entityClassLike))
                .map(a -> a);
    }

}
