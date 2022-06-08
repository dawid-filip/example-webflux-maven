package com.df.repository;

import com.df.annotation.Audit;
import com.df.entity.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PetRepository extends ReactiveCrudRepository<Pet, Long> {

    @Audit
    @Override
    <S extends Pet> Mono<S> save(S entity);

    @Audit
    @Override
    <S extends Pet> Flux<S> saveAll(Iterable<S> entities);
}
