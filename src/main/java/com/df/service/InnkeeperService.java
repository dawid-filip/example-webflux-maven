package com.df.service;

import com.df.dto.OwnerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InnkeeperService {
    Flux<OwnerDto> getAll();
    Mono<OwnerDto> getById(Long id);
    Mono<OwnerDto> create(OwnerDto ownerDto);
    Mono<OwnerDto> deleteById(Long id);
    Mono<OwnerDto> alter(OwnerDto ownerDto);
}
