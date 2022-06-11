package com.df.startup;

import com.df.entity.Owner;
import com.df.entity.Pet;
import com.df.repository.AuditRepository;
import com.df.repository.OwnerDtoCustomRepository;
import com.df.repository.OwnerRepository;
import com.df.repository.PetRepository;
import com.df.service.OwnerDtoService;
import com.df.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Log4j2
public class TestDataStartUp {

    final private PetRepository petRepository;
    final private OwnerRepository ownerRepository;
    final private PetService petService;
    final private OwnerDtoService ownerDtoService;
    final private AuditRepository auditRepository;

    final private OwnerDtoCustomRepository ownerDtoCustomRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void doOnContextRefreshedEvent() {
        printAllPets();

//        printAlter("Y", (short)88);
//        printAllPets();

//        petStartUp();
//        ownerStartUp();

        printGetAllFromOwnerDtoService();
        printAllOwnerDtoCustomRepository();
        printTransactionalAllPetDtos();
    }

    private void printTransactionalAllPetDtos() {
        ownerDtoCustomRepository.getTransactionalAllPetDtos()
                .collectList()
                .map(pets ->
                        "printTransactionalAllPetDtos() -> " + pets.stream()
                                .map(p -> p.toString())
                                .collect(Collectors.joining("\n", "{\n", "}"))
                )
                .log()
                .subscribe();
    }

    private void printAllOwnerDtoCustomRepository() {
        ownerDtoCustomRepository.getAll()
                .collectList()
                .map(pets ->
                        "printAllOwnerDtoCustomRepository() -> " + pets.stream()
                                .map(p -> p.toString())
                                .collect(Collectors.joining("\n", "{\n", "}"))
                )
                .log()
                .subscribe();
    }

    private void printGetAllFromOwnerDtoService() {
        ownerDtoService.getAll()
                .collectList()
                .map(pets ->
                        "getAllFromOwnerDtoService() -> " + pets.stream()
                                .map(p -> p.toString())
                                .collect(Collectors.joining("\n", "{\n", "}"))
                )
                .log()
                .subscribe();
    }

    private void petStartUp() {
        Flux.range(1, 6).doOnNext(i -> saveNewPet(i)).subscribe();
    }

    private void printAlter(String suffix, Short numbers) {
        petRepository.findById(1L)
                .flatMap(pet -> {
                    pet.setName("petName " + suffix);
                    pet.setAge(numbers);
                    pet.setWeight(numbers);
                    pet.setLength(numbers);
                    return petRepository.save(pet);
                })
                .subscribe();
    }

    private void printAlterWithGetById(String suffix, Short numbers) {
        petRepository.findById(1L)
                .flatMap(pet -> {
                    log.info("Before ALTER -> " + pet);
                    pet.setName("petName " + suffix);
                    pet.setAge(numbers);
                    pet.setWeight(numbers);
                    pet.setLength(numbers);
                    return petRepository.save(pet)
                            .doOnError(e -> log.info("Failed to alter pet id=" + pet.getId() + " version=" + pet.getVersion() + ", age=" + pet.getAge()))
                            .map(alteredPet -> {
                                log.info(" After ALTER -> " + alteredPet);
                                return alteredPet;
                            });
                })
                .subscribe();
    }

    private void printAllPets() {
        petRepository.findAll().collectList()
                .map(pets ->
                        "allPets() -> " + pets.stream()
                                .map(p -> p.toString())
                                .collect(Collectors.joining("\n", "{\n", "}"))
                )
                .log()
                .subscribe();
    }

    private void saveNewPet(int petId) {
        petRepository.save(createPet(null, "petName " + petId, petId, petId + 2, petId + 3))
                .doOnNext(p -> log.info("PetRepo " + p + " saved."))
                .subscribe();
    }

    private Pet createPet(Long id, String petName, int age, int weight, int length) {
        return new Pet(id, petName, (short) age, (short) weight, (short) length);
    }

    private void ownerStartUp() {
        Flux.range(1, 2).doOnNext(i -> saveOwner(i)).subscribe();
    }

    private void saveOwner(int ownerId) {
        List<Long> petIds = Stream.iterate(ownerId, n -> n + 1).limit(ownerId)
                .map(i -> Long.valueOf(i))
                .collect(Collectors.toList());

        ownerRepository.save(createOwner("firstName " + ownerId, "lastName " + ownerId,
                        ownerId + 20, petIds))
                .doOnSuccess(o -> log.info("OwnerRepo " + o + " saved."))
                .subscribe();
    }

    private Owner createOwner(String firstName, String lastName, int age, List<Long> petIds) {
        return new Owner(null, firstName, lastName, (short) age, petIds);
    }

}
