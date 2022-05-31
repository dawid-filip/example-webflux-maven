package com.df.service;

import com.df.entity.Owner;
import com.df.request.OwnerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OwnerService {
    Mono<Owner> getById(Long id);
    Flux<Owner> getAll();
    Mono<Owner> create(OwnerRequest ownerRequest);
    Mono<Owner> deleteById(Long id);
    Mono<Owner> alter(OwnerRequest ownerRequest);
}
