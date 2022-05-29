package com.df.service;

import com.df.dto.PetDto;
import com.df.entity.Pet;
import com.df.repository.PetCrudRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Data
@Log4j2
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

    @Override
    @Transactional
    public Mono<PetDto> create(PetDto petDto) {
        return petCrudRepository.save(new Pet(petDto))
                .map(pet -> new PetDto(pet))
                .doOnSuccess(pet -> log.info("Created " + pet + "."))
                .doOnError(pet -> log.info("Failed to create " + pet + "."));
    }

    @Override
    @Transactional
    public Mono<PetDto> deleteById(Long id) {
        return getById(id)
                .flatMap(petDto ->
                        petCrudRepository.deleteById(id)
                                .flatMap(p -> Mono.just(petDto))
                                .doOnSuccess(pet -> log.info("Deleted " + petDto + "."))
                                .doOnError(pet -> log.info("Failed to delete " + petDto + "."))
                );
    }

}
