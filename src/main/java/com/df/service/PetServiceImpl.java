package com.df.service;

import com.df.dto.PetDto;
import com.df.repository.PetCrudRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Data
public class PetServiceImpl implements PetService {

    private final PetCrudRepository petCrudRepository;

    @Override
    public Mono<PetDto> getById(Long id) {
        return petCrudRepository.findById(id)
                .map(pet -> new PetDto(pet))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<PetDto> getAll() {
        return petCrudRepository.findAll()
                .map(pet -> new PetDto(pet))
                .switchIfEmpty(Flux.empty());
    }
}
