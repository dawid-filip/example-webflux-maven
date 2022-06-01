package com.df.service;

import com.df.dto.PetDto;
import com.df.entity.Pet;
import com.df.repository.PetRepository;
import com.df.util.PetUtility;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    public Mono<PetDto> getById(Long id) {
        return petRepository.findById(id)
                .map(pet -> new PetDto(pet))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<PetDto> getByIds(List<Long> ids) {
        return petRepository.findAllById(ids)
                .map(pet -> new PetDto(pet))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<PetDto> getAll() {
        return petRepository.findAll()
                .map(pet -> new PetDto(pet))
                .switchIfEmpty(Flux.empty());
    }

    @Override
    @Transactional
    public Mono<PetDto> create(PetDto petDto) {
        return petRepository.save(new Pet(petDto))
                .map(pet -> new PetDto(pet))
                .doOnSuccess(p -> log.info("Created " + p + "."))
                .doOnError(p -> log.info("Failed to create " + p + "."));
    }

    @Override
    @Transactional
    public Flux<PetDto> createAll(List<PetDto> petDtos) {
        return petRepository.saveAll(PetUtility.petDtosToPets(petDtos))
                .map(pet -> new PetDto(pet))
                .doOnNext(p -> log.info("Created " + p + "."))
                .doOnError(p -> log.info("Failed to create " + p + "."));
    }

    @Override
    @Transactional
    public Mono<PetDto> deleteById(Long id) {
        return getById(id)
                .flatMap(petDto ->
                        petRepository.deleteById(id)
                                .doOnSuccess(p -> log.info("Deleted " + petDto + "."))
                                .doOnError(p -> log.info("Failed to delete " + petDto + "."))
                                .then(Mono.just(petDto))
                );
    }

    @Override
    @Transactional
    public Flux<PetDto> deleteAllById(List<Long> ids) {
        return getByIds(ids)
                .collectList()
                .flatMap(petDtos ->
                        petRepository.deleteAllById(ids)
                                .doOnSuccess(p ->
                                    petDtos.forEach(petDto ->
                                            log.info("Deleted " + petDto + ".")))
                                .doOnError(p -> log.info("Failed to delete " + petDtos.size() + ", ids=" + ids + "."))
                                .then(Mono.just(petDtos))
                )
                .flatMapMany(Flux::fromIterable)
                .map(p -> p);
    }

    @Override
    @Transactional
    public Mono<PetDto> alter(PetDto petDto) {
        return getById(petDto.getId())
                .flatMap(currentPetDto ->
                        petRepository.save(new Pet(petDto))
                                .flatMap(pet -> Mono.just(new PetDto(pet)))
                                .doOnSuccess(p -> log.info("Altered " + petDto + "."))
                                .doOnError(p -> log.info("Failed to alter " + petDto + "."))
                );
    }

}
