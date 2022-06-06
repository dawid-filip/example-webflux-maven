package com.df.startup;

import com.df.entity.Owner;
import com.df.entity.Pet;
import com.df.repository.OwnerRepository;
import com.df.repository.PetRepository;
import com.df.service.PetService;
import com.df.util.PetUtility;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
@Log4j2
public class TestDataStartUp {

    final private PetRepository petRepository;
    final private OwnerRepository ownerRepository;
    final private PetService petService;

    @EventListener(ContextRefreshedEvent.class)
    public void doOnContextRefreshedEvent() {
        printAllPets();
        printAlterService("X", (short)99);
        printAlter("Y", (short)88);
        printAllPets();

//        petStartUp();
//        ownerStartUp();
    }

    private void petStartUp() {
        Flux.range(1, 6).doOnNext(i -> savePet(i)).subscribe();
    }

    private void printAlterService(String suffix, Short numbers) {
        petService.getById(1L)
                .flatMap(petDto -> {
                    petDto.setId(1L);
                    petDto.setName("petName " + suffix);
                    petDto.setAge(numbers);
                    petDto.setWeight(numbers);
                    petDto.setLength(numbers);
                    return petService.alter(PetUtility.petDtoToPetDto(petDto));
                })
                .subscribe();
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

    private void printAllPets() {
        petRepository.findAll().collectList()
                .map(pets ->
                        pets.stream().map(p -> p.toString())
                                .collect(Collectors.joining("\n", "{\n", "}"))
                )
                .log().subscribe();
    }

    private void savePet(int petId) {
        petRepository.save(createPet("petName " + petId, petId, petId + 2, petId + 3))
                .doOnNext(p -> log.info("PetRepo " + p + " saved."))
                .subscribe();
    }

    private Pet createPet(String petName, int age, int weight, int length) {
        return new Pet(null, petName, (short) age, (short) weight, (short) length);
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
