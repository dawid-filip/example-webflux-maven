package com.df.service;

import com.df.entity.Audit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuditService {
    <T> Mono<Audit> create(T entityValue);
    <T> Flux<Audit> createAll(List<T> entitiesValue);
}
