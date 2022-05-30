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
                .doOnSuccess(p -> log.info("Created " + p + "."))
                .doOnError(p -> log.info("Failed to create " + p + "."));
    }

    @Override
    @Transactional
    public Mono<PetDto> deleteById(Long id) {
        return getById(id)
                .flatMap(petDto ->
                    petCrudRepository.deleteById(id)
                            .doOnSuccess(p -> log.info("Deleted " + petDto + "."))
                            .doOnError(p -> log.info("Failed to delete " + petDto + "."))
                            .then(Mono.just(petDto))
                );
    }

    @Override
    @Transactional
    public Mono<PetDto> alter(PetDto petDto) {
        return getById(petDto.getId())
                .flatMap(currentPetDto ->
                        petCrudRepository.save(new Pet(petDto))
                                .flatMap(pet -> Mono.just(new PetDto(pet)))
                                .doOnSuccess(p -> log.info("Altered " + petDto + "."))
                                .doOnError(p -> log.info("Failed to alter " + petDto + "."))
                );
    }

}
