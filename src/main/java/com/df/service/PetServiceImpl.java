package com.df.service;

import com.df.dto.PetDto;
import com.df.repository.PetRepository;
import com.df.util.PetUtility;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    public Mono<PetDto> getById(Long id) {
        return Mono.justOrEmpty(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(petId ->
                        petRepository.findById(petId)
                                .map(PetUtility::petToPetDto)
                                .switchIfEmpty(Mono.empty())
                );
    }

    @Override
    public Flux<PetDto> getByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .hasElements()
                .flatMapMany(hasElements ->
                        hasElements ? petRepository.findAllById(ids)
                                .switchIfEmpty(Flux.empty())
                                .map(PetUtility::petToPetDto)
                                .collectList()
                                .flatMapMany(Flux::fromIterable) : Flux.empty()
                )
                .map(p -> p);
    }

    @Override
    public Flux<PetDto> getAll() {
        return petRepository.findAll()
                .map(PetUtility::petToPetDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    @Transactional
    public Mono<PetDto> create(PetDto petDto) {
        return Mono.justOrEmpty(petDto)
                .switchIfEmpty(Mono.empty())
                .map(PetUtility::petDtoToPet)
                .flatMap(pet ->
                        petRepository.save(pet)
                                .map(PetUtility::petToPetDto)
                                .doOnSuccess(p -> log.info("Created " + p + "."))
                                .doOnError(p -> log.info("Failed to create " + p + "."))
                )
                .map(p -> p);
    }

    @Override
    @Transactional
    public Flux<PetDto> createAll(List<PetDto> petDtos) {
        return petRepository.saveAll(PetUtility.petDtosToPets(petDtos))
                .map(PetUtility::petToPetDto)
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
    public Flux<PetDto> deleteByIds(List<Long> ids) {
        return getByIds(ids)
                .collectList()
                .filter(petDtos -> !petDtos.isEmpty())
                .flatMap(petDtos ->
                        petRepository.deleteAllById(petDtos.stream().map(PetDto::getId).collect(Collectors.toList()))//ids)
                                .doOnSuccess(p ->
                                        petDtos.forEach(petDto -> log.info("Deleted " + petDto + "."))
                                )
                                .doOnError(p -> log.info("Failed to delete " + petDtos.size() + ", ids=" + ids + "."))
                                .then(Mono.just(petDtos))
                )
                .flatMapMany(Flux::fromIterable)
                .map(p -> p);
    }

    @Override
    @Transactional
    public Mono<PetDto> alter(PetDto petDto) {
        return Mono.justOrEmpty(petDto)
                .switchIfEmpty(Mono.empty())
                .flatMap(pet -> getById(pet.getId()))
                .flatMap(currentPetDto ->
                        petRepository.save(PetUtility.petDtoToPet(petDto))
                                .flatMap(pet -> Mono.just(new PetDto(pet)))
                                .doOnSuccess(p -> log.info("Altered " + petDto + "."))
                                .doOnError(p -> log.info("Failed to alter " + petDto + "."))
                );
    }

    @Override
    @Transactional
    public Flux<PetDto> alterAll(List<PetDto> petDto) {
        return validatePetDtos(petDto)
                .flatMap(availablePetDtos ->
                        getByIds(PetUtility.petDtosToPetIds(availablePetDtos))
                                .map(PetUtility::petDtoToPet)
                                .collectList()
                                .flatMap(currentPetDtos ->
                                        petRepository.saveAll(PetUtility.petDtosToPets(availablePetDtos))
                                                .map(PetUtility::petToPetDto)
                                                .collectList()
                                                .doOnSuccess(pets ->
                                                        pets.forEach(pet -> log.info("Altered " + pet + "."))
                                                )
                                )
                                .flatMapMany(Flux::fromIterable)
                                .map(p -> p)
                )
                .map(p -> p);
    }

    private Flux<List<PetDto>> validatePetDtos(List<PetDto> petDto) {
        return Flux.just(petDto)
                .flatMap(currentPetDtos -> {
                    List<PetDto> correctPetDtos = Objects.nonNull(currentPetDtos) && !currentPetDtos.isEmpty() ?
                            currentPetDtos.stream()
                                    .filter(p -> p.getId() != null)
                                    .collect(Collectors.toList()) :
                            List.of();
                    return correctPetDtos.isEmpty() ?
                            Mono.empty() :
                            Mono.just(correctPetDtos);
                })
                .switchIfEmpty(Mono.empty());
    }

}
