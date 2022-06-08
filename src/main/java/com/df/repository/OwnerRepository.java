package com.df.repository;

import com.df.annotation.Audit;
import com.df.entity.Owner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OwnerRepository extends ReactiveCrudRepository<Owner, Long> {

    @Audit
    @Override
    <S extends Owner> Mono<S> save(S entity);

    @Audit
    @Override
    <S extends Owner> Flux<S> saveAll(Iterable<S> entities);
}
