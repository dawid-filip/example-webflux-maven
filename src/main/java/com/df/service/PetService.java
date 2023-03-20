package com.df.service;

import com.df.dto.PetDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PetService {
    Mono<PetDto> getRandom();
    Flux<PetDto> getAll();
    Mono<PetDto> getById(Long id);
    Flux<PetDto> getByIds(List<Long> ids);
    Mono<PetDto> create(PetDto petDto);
    Flux<PetDto> createAll(List<PetDto> petDtos);
    Mono<PetDto> deleteById(Long id);
    Flux<PetDto> deleteByIds(List<Long> ids);
    Mono<PetDto> alter(PetDto petDto);
    Flux<PetDto> alterAll(List<PetDto> petDtos);
}
