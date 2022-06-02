package com.df.service;

import com.df.entity.Pet;
import com.df.repository.PetRepository;
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

import static org.mockito.ArgumentMatchers.anyList;
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
                .expectNextMatches(petDto -> pet.getId() == petDto.getId() && pet.getName().equals(petDto.getName()))
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
                .expectNextMatches(petDto -> pets.get(0).getId() == petDto.getId() && pets.get(0).getName().equals(petDto.getName()))
                .expectNextMatches(petDto -> pets.get(1).getId() == petDto.getId() && pets.get(1).getName().equals(petDto.getName()))
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    public void testGetByIdsWhenDoesNotExist() {
        Mockito.when(petRepository.findAllById(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(petServiceImpl.getByIds(anyList()))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(petRepository, times(1)).findAllById(anyList());
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
                .expectNextMatches(petDto -> pets.get(0).getId() == petDto.getId() && pets.get(0).getName().equals(petDto.getName()))
                .expectNextMatches(petDto -> pets.get(1).getId() == petDto.getId() && pets.get(1).getName().equals(petDto.getName()))
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

}
