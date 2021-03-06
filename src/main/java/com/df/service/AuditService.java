package com.df.service;

import com.df.entity.Audit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {
    <T> Mono<Audit> create(T entityValue);
    <T> Flux<Audit> createAll(List<T> entitiesValue);
    Flux<Audit> getAll();
    Mono<Audit> getById(Long id);
    Flux<Audit> getBetweenIds(Long startId, Long endId);
    Flux<Audit> getBetweenAuditedOns(LocalDateTime startAuditedOn, LocalDateTime endAuditedOn);
    Flux<Audit> getLikeEntityClass(String entityClass);
}
