package com.df.service;

import com.df.dto.PetDto;
import com.df.entity.Pet;
import com.df.repository.PetRepository;
import com.df.util.PetUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceTest {

    @InjectMocks
    public PetServiceImpl petServiceImpl;

    @Mock
    public PetRepository petRepository;

    @Test
    public void testGetById() {
        Pet pet = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);
        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Mono.just(pet));

        StepVerifier.create(petServiceImpl.getById(pet.getId()))
                .expectNextMatches(petDto -> pet.getId() == petDto.getId() && pet.getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findById(pet.getId());
    }

    @Test
    public void testGetByIdWhenDoesNotExist() {
        Long id = 1L;
        Mockito.when(petRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(petServiceImpl.getById(id))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findById(id);
    }

    @Test
    public void testGetByIdsOne() {
        Pet pet = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);
        Mockito.when(petRepository.findAllById(List.of(pet.getId()))).thenReturn(Flux.just(pet));

        StepVerifier.create(petServiceImpl.getByIds(List.of(pet.getId())))
                .expectNextMatches(petDto -> pet.getId() == petDto.getId() && pet.getName().equals(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(List.of(pet.getId()));
    }

    @Test
    public void testGetByIdsTwo() {
        List<Pet> pets = List.of(new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11),
                new Pet(2L, "petName2", (short) 3, (short) 4, (short) 12));
        Mockito.when(petRepository.findAllById(List.of(1L, 2L))).thenReturn(Flux.fromIterable(pets));

        StepVerifier.create(petServiceImpl.getByIds(List.of(1L, 2L)))
                .expectNextMatches(petDto -> pets.get(0).getId() == petDto.getId() && pets.get(0).getName().equalsIgnoreCase(petDto.getName()))
                .expectNextMatches(petDto -> pets.get(1).getId() == petDto.getId() && pets.get(1).getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    public void testGetByIdsWhenDoesNotExist() {
        StepVerifier.create(petServiceImpl.getByIds(List.of()))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, never()).findAllById(anyList());
    }

    @Test
    public void testGetAllOne() {
        Pet pet = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);
        Mockito.when(petRepository.findAll()).thenReturn(Flux.just(pet));

        StepVerifier.create(petServiceImpl.getAll())
                .expectNextMatches(petDto -> pet.getId() == petDto.getId() && pet.getName().equals(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTwo() {
        List<Pet> pets = List.of(new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11),
                new Pet(2L, "petName2", (short) 3, (short) 4, (short) 12));
        Mockito.when(petRepository.findAll()).thenReturn(Flux.fromIterable(pets));

        StepVerifier.create(petServiceImpl.getAll())
                .expectNextMatches(petDto -> pets.get(0).getId() == petDto.getId() && pets.get(0).getName().equalsIgnoreCase(petDto.getName()))
                .expectNextMatches(petDto -> pets.get(1).getId() == petDto.getId() && pets.get(1).getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllWhenNoData() {
        Mockito.when(petRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(petServiceImpl.getAll())
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAll();
    }

    @Test
    public void testCreate() {
        Pet petBefore = new Pet(null, "petName1", (short) 2, (short) 3, (short) 11);
        Pet petAfter = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);

        Mockito.when(petRepository.save(petBefore)).thenReturn(Mono.just(petAfter));

        StepVerifier.create(petServiceImpl.create(PetUtility.petToPetDto(petBefore)))
                .expectNextMatches(petDto -> petAfter.getId() == petDto.getId())
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).save(petBefore);
    }

    @Test
    public void testCreateWhenIdIsSetThenItIsIgnore() {
        Pet petBefore = new Pet(2L, "petName1", (short) 2, (short) 3, (short) 11);
        Pet petAfter = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);

        Mockito.when(petRepository.save(petBefore)).thenReturn(Mono.just(petAfter));

        StepVerifier.create(petServiceImpl.create(PetUtility.petToPetDto(petBefore)))
                .expectNextMatches(petDto -> petAfter.getId() == petDto.getId())
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).save(petBefore);
    }

    @Test
    public void testCreateTwoInRow() {
        Pet petBefore1 = new Pet(null, "petName1", (short) 2, (short) 3, (short) 11);
        Pet petBefore2 = new Pet(null, "petName2", (short) 3, (short) 4, (short) 12);
        Pet petAfter1 = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);
        Pet petAfter2 = new Pet(2L, "petName2", (short) 3, (short) 4, (short) 12);

        Mockito.when(petRepository.save(petBefore1)).thenReturn(Mono.just(petAfter1));
        Mockito.when(petRepository.save(petBefore2)).thenReturn(Mono.just(petAfter2));

        StepVerifier.create(petServiceImpl.create(PetUtility.petToPetDto(petBefore1)))
                .expectNextMatches(petDto -> petAfter1.getId() == petDto.getId())
                .verifyComplete();
        StepVerifier.create(petServiceImpl.create(PetUtility.petToPetDto(petBefore2)))
                .expectNextMatches(petDto -> petAfter2.getId() == petDto.getId())
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).save(petBefore1);
        Mockito.verify(petRepository, times(1)).save(petBefore2);
    }

    @Test
    public void testCreateEmpty() {
        StepVerifier.create(petServiceImpl.create(null))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, never()).save(any());
    }

    @Test
    public void testCreateAllOne() {
        Pet petBefore = new Pet(null, "petName1", (short) 2, (short) 3, (short) 11);
        Pet petAfter = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);

        Mockito.when(petRepository.saveAll(List.of(petBefore))).thenReturn(Flux.just(petAfter));

        StepVerifier.create(petServiceImpl.createAll(List.of(PetUtility.petToPetDto(petBefore))))
                .expectNextMatches(petDto -> petAfter.getId() == petDto.getId() && petAfter.getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).saveAll(List.of(petBefore));
    }

    @Test
    public void testCreateAllTwo() {
        Pet petBefore1 = new Pet(null, "petName1", (short) 2, (short) 3, (short) 11);
        Pet petBefore2 = new Pet(null, "petName2", (short) 3, (short) 4, (short) 12);
        Pet petAfter1 = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);
        Pet petAfter2 = new Pet(2L, "petName2", (short) 3, (short) 4, (short) 12);

        Mockito.when(petRepository.saveAll(List.of(petBefore1, petBefore2))).thenReturn(Flux.just(petAfter1, petAfter2));

        StepVerifier.create(petServiceImpl.createAll(List.of(PetUtility.petToPetDto(petBefore1), PetUtility.petToPetDto(petBefore2))))
                .expectNextMatches(petDto -> petAfter1.getId() == petDto.getId() && petAfter1.getName().equalsIgnoreCase(petDto.getName()))
                .expectNextMatches(petDto -> petAfter2.getId() == petDto.getId() && petAfter2.getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).saveAll(List.of(petBefore1, petBefore2));
    }

    @Test
    public void testCreateAllEmpty() {
        StepVerifier.create(petServiceImpl.createAll(List.of()))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, never()).saveAll(anyList());
    }

    @Test
    public void testDeleteById() {
        Pet pet = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);

        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Mono.just(pet));
        Mockito.when(petRepository.deleteById(pet.getId())).thenReturn(Mono.empty().then());

        StepVerifier.create(petServiceImpl.deleteById(pet.getId()))
                .expectNextMatches(petDto -> petDto.getId() == petDto.getId())
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findById(pet.getId());
        Mockito.verify(petRepository, times(1)).deleteById(pet.getId());
    }

    @Test
    public void testDeleteByIdWhenDoesNotExist() {
        Long petId = 1L;

        Mockito.when(petRepository.findById(petId)).thenReturn(Mono.empty());

        StepVerifier.create(petServiceImpl.deleteById(petId))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findById(petId);
        Mockito.verify(petRepository, never()).deleteById(petId);
    }

    @Test
    public void testDeleteByIdsOne() {
        List<Pet> pets = List.of(new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11));
        List<Long> petIds = List.of(pets.get(0).getId());

        Mockito.when(petRepository.findAllById(petIds)).thenReturn(Flux.fromIterable(pets));
        Mockito.when(petRepository.deleteAllById(petIds)).thenReturn(Flux.empty().then());

        StepVerifier.create(petServiceImpl.deleteByIds(petIds))
                .expectNextMatches(petDto -> pets.get(0).getId() == petDto.getId() && pets.get(0).getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(petIds);
        Mockito.verify(petRepository, times(1)).deleteAllById(petIds);
    }

    @Test
    public void testDeleteByIdsTwo() {
        List<Pet> pets = List.of(new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11),
                new Pet(2L, "petName2", (short) 3, (short) 4, (short) 12));
        List<Long> petIds = pets.stream().map(Pet::getId).collect(Collectors.toList());

        Mockito.when(petRepository.findAllById(petIds)).thenReturn(Flux.fromIterable(pets));
        Mockito.when(petRepository.deleteAllById(petIds)).thenReturn(Flux.empty().then());

        StepVerifier.create(petServiceImpl.deleteByIds(petIds))
                .expectNextMatches(petDto -> pets.get(0).getId() == petDto.getId() && pets.get(0).getName().equalsIgnoreCase(petDto.getName()))
                .expectNextMatches(petDto -> pets.get(1).getId() == petDto.getId() && pets.get(1).getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(petIds);
        Mockito.verify(petRepository, times(1)).deleteAllById(petIds);
    }

    @Test
    public void testDeleteByIdsWhenDoesNotExist() {
        List<Long> petIds = List.of(1L);

        Mockito.when(petRepository.findAllById(petIds)).thenReturn(Flux.empty());

        StepVerifier.create(petServiceImpl.deleteByIds(petIds))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(petIds);
        Mockito.verify(petRepository, never()).deleteAllById(petIds);
    }

    @Test
    public void testAlter() {
        PetDto petBefore = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        PetDto petNew = new PetDto(1L, "petName1New", (short) 3, (short) 4, (short) 12);

        Mockito.when(petRepository.findById(petBefore.getId())).thenReturn(Mono.just(PetUtility.petDtoToPet(petBefore)));
        Mockito.when(petRepository.save(PetUtility.petDtoToPet(petNew))).thenReturn(Mono.just(PetUtility.petDtoToPet(petNew)));

        StepVerifier.create(petServiceImpl.alter(petNew))
                .expectNextMatches(petDto -> petNew.getId() == petDto.getId() && petNew.getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findById(petBefore.getId());
        Mockito.verify(petRepository, times(1)).save(PetUtility.petDtoToPet(petNew));
    }

    @Test
    public void testAlterWhenDoesNotExist() {
        PetDto petNotExisting = new PetDto(1L, "petName1", (short) 3, (short) 4, (short) 12);

        Mockito.when(petRepository.findById(petNotExisting.getId())).thenReturn(Mono.empty());

        StepVerifier.create(petServiceImpl.alter(petNotExisting))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findById(petNotExisting.getId());
        Mockito.verify(petRepository, never()).save(any());
    }

    @Test
    public void testAlterWhenIdIsNull() {
        PetDto petDto = new PetDto(null, "petName1", (short) 3, (short) 4, (short) 12);

        StepVerifier.create(petServiceImpl.alter(petDto))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, never()).findById(petDto.getId());
        Mockito.verify(petRepository, never()).save(any());
    }

    @Test
    public void testAlterWhenDoesNull() {
        StepVerifier.create(petServiceImpl.alter(null))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, never()).findById(anyLong());
        Mockito.verify(petRepository, never()).save(any());
    }

    @Test
    public void testAlterAllOne() {
        PetDto petBefore = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        PetDto petNew = new PetDto(1L, "petName1New", (short) 3, (short) 4, (short) 12);

        Mockito.when(petRepository.findAllById(List.of(petBefore.getId()))).thenReturn(Flux.just(PetUtility.petDtoToPet(petBefore)));
        Mockito.when(petRepository.saveAll(List.of(PetUtility.petDtoToPet(petNew)))).thenReturn(Flux.just(PetUtility.petDtoToPet(petNew)));

        StepVerifier.create(petServiceImpl.alterAll(List.of(petNew)))
                .expectNextMatches(petDto -> petNew.getId() == petDto.getId() && petNew.getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(List.of(petBefore.getId()));
        Mockito.verify(petRepository, times(1)).saveAll(List.of(PetUtility.petDtoToPet(petNew)));
    }

    @Test
    public void testAlterAllTwo() {
        List<PetDto> petsBefore = List.of(new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11),
                new PetDto(2L, "petName2", (short) 3, (short) 4, (short) 12));
        List<PetDto> petsAfter = List.of(new PetDto(1L, "petName1New", (short) 2, (short) 3, (short) 11),
                new PetDto(2L, "petName2New", (short) 3, (short) 4, (short) 12));
        List<Long> petIds = petsAfter.stream().map(PetDto::getId).collect(Collectors.toList());

        Mockito.when(petRepository.findAllById(petIds)).thenReturn(Flux.fromIterable(petsBefore).map(PetUtility::petDtoToPet));
        Mockito.when(petRepository.saveAll(PetUtility.petDtosToPets(petsAfter))).thenReturn(Flux.fromIterable(petsAfter).map(PetUtility::petDtoToPet));

        StepVerifier.create(petServiceImpl.alterAll(petsAfter))
                .expectNextMatches(petDto -> petsAfter.get(0).getId() == petDto.getId() && petsAfter.get(0).getName().equalsIgnoreCase(petDto.getName()))
                .expectNextMatches(petDto -> petsAfter.get(1).getId() == petDto.getId() && petsAfter.get(1).getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(petIds);
        Mockito.verify(petRepository, times(1)).saveAll(PetUtility.petDtosToPets(petsAfter));
    }

    @Test
    public void testAlterAllWhenDoesNotExist() {
        StepVerifier.create(petServiceImpl.alterAll(List.of()))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, never()).findAllById(anyList());
        Mockito.verify(petRepository, never()).saveAll(anyList());
    }


    @Test
    public void testAlterAllIdIsNull() {
        PetDto petBefore = new PetDto(null, "petName1", (short) 2, (short) 3, (short) 11);

        StepVerifier.create(petServiceImpl.alterAll(List.of(petBefore)))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, never()).findAllById(anyList());
        Mockito.verify(petRepository, never()).saveAll(anyList());
    }

    @Test
    public void testAlterAllWithOneOkAndOneWithIdNull() {
        List<PetDto> petsBefore = List.of(new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11),
                new PetDto(null, "petName2", (short) 3, (short) 4, (short) 12));
        List<PetDto> petsAfter = List.of(new PetDto(1L, "petName1New", (short) 2, (short) 3, (short) 11));
        List<Long> petIds = petsAfter.stream().map(PetDto::getId).collect(Collectors.toList());

        Mockito.when(petRepository.findAllById(petIds)).thenReturn(Flux.just(petsBefore.get(0)).map(PetUtility::petDtoToPet));
        Mockito.when(petRepository.saveAll(anyList())).thenReturn(Flux.fromIterable(petsAfter).map(PetUtility::petDtoToPet));

        StepVerifier.create(petServiceImpl.alterAll(petsBefore))
                .expectNextMatches(petDto -> petsAfter.get(0).getId() == petDto.getId() && petsAfter.get(0).getName().equalsIgnoreCase(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(petIds);
        Mockito.verify(petRepository, times(1)).saveAll(anyList());
    }

}
