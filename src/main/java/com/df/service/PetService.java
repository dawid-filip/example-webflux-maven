package com.df.service;

import com.df.dto.PetDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetService {
    Mono<PetDto> getById(Long id);
    Flux<PetDto> getAll();
    Mono<PetDto> create(PetDto petDto);
    Mono<PetDto> deleteById(Long id);
    Mono<PetDto> alter(PetDto petDto);
}
