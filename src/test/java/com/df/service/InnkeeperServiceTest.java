package com.df.service;

import com.df.entity.Owner;
import com.df.entity.Pet;
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
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class InnkeeperServiceTest {

    @InjectMocks
    public InnkeeperServiceImpl innkeeperServiceImpl;

    @Mock
    public OwnerService ownerService;

    @Mock
    public PetService petService;

    @Test
    public void testGetById() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));
        Pet pet = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);

        Mockito.when(ownerService.getById(owner.getId())).thenReturn(Mono.just(owner));
        Mockito.when(petService.getByIds(owner.getPetIds())).thenReturn(Flux.just(pet).map(PetUtility::petToPetDto));

        StepVerifier.create(innkeeperServiceImpl.getById(owner.getId()))
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        owner.getPetIds().size() == expOwnerDto.getPets().size() &&
                        pet.getId() == expOwnerDto.getPets().get(0).getId() &&
                        pet.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(owner.getId());
        Mockito.verify(petService, times(1)).getByIds(owner.getPetIds());
    }

    @Test
    public void testGetByIdWhenPetIdsEmpty() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of());

        Mockito.when(ownerService.getById(owner.getId())).thenReturn(Mono.just(owner));
        Mockito.when(petService.getByIds(owner.getPetIds())).thenReturn(Flux.empty());

        StepVerifier.create(innkeeperServiceImpl.getById(owner.getId()))
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        expOwnerDto.getPets().isEmpty()
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(owner.getId());
        Mockito.verify(petService, times(1)).getByIds(owner.getPetIds());
    }

    @Test
    public void testGetByIdWhenPetIdsNull() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, null);

        Mockito.when(ownerService.getById(owner.getId())).thenReturn(Mono.just(owner));
        Mockito.when(petService.getByIds(owner.getPetIds())).thenReturn(Flux.empty());

        StepVerifier.create(innkeeperServiceImpl.getById(owner.getId()))
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        Objects.isNull(expOwnerDto.getPets())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(owner.getId());
        Mockito.verify(petService, times(1)).getByIds(owner.getPetIds());
    }

    @Test
    public void testGetByIdWhenOwnerIsEmpty() {
        Long ownerId = 1L;
        Mockito.when(ownerService.getById(ownerId)).thenReturn(Mono.empty());

        StepVerifier.create(innkeeperServiceImpl.getById(ownerId))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(ownerId);
        Mockito.verify(petService, never()).getByIds(anyList());
    }

    @Test
    public void testGetByIdWhenOwnerIsNull() {
        Mockito.when(ownerService.getById(null)).thenReturn(Mono.empty());

        StepVerifier.create(innkeeperServiceImpl.getById(null))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerService, never()).getById(1L);
        Mockito.verify(petService, never()).getByIds(anyList());
    }

    @Test
    public void testGetAllOne() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));
        Pet pet = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);

        Mockito.when(ownerService.getAll()).thenReturn(Flux.just(owner));
        Mockito.when(petService.getAll()).thenReturn(Flux.just(pet).map(PetUtility::petToPetDto));

        StepVerifier.create(innkeeperServiceImpl.getAll())
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        owner.getPetIds().size() == expOwnerDto.getPets().size() &&
                        pet.getId() == expOwnerDto.getPets().get(0).getId() &&
                        pet.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getAll();
        Mockito.verify(petService, times(1)).getAll();
    }

    @Test
    public void testGetAllTwo() {
        Owner owner1 = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));
        Pet pet1 = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);
        Owner owner2 = new Owner(2L, "firstName2", "lastName2", (short) 22, List.of(2L));
        Pet pet2 = new Pet(2L, "petName2", (short) 3, (short) 4, (short) 12);

        Mockito.when(ownerService.getAll()).thenReturn(Flux.just(owner1, owner2));
        Mockito.when(petService.getAll()).thenReturn(Flux.just(pet1, pet2).map(PetUtility::petToPetDto));

        StepVerifier.create(innkeeperServiceImpl.getAll())
                .expectNextMatches(expOwnerDto -> owner1.getId() == expOwnerDto.getId() &&
                        owner1.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        owner1.getPetIds().size() == expOwnerDto.getPets().size() &&
                        pet1.getId() == expOwnerDto.getPets().get(0).getId() &&
                        pet1.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .expectNextMatches(expOwnerDto -> owner2.getId() == expOwnerDto.getId() &&
                        owner2.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        owner2.getPetIds().size() == expOwnerDto.getPets().size() &&
                        pet2.getId() == expOwnerDto.getPets().get(0).getId() &&
                        pet2.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getAll();
        Mockito.verify(petService, times(1)).getAll();
    }

    @Test
    public void testGetAllWhenOwnerDoesNotHavePets() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of());
        Pet pet = new Pet(1L, "petName1", (short) 2, (short) 3, (short) 11);

        Mockito.when(ownerService.getAll()).thenReturn(Flux.just(owner));
        Mockito.when(petService.getAll()).thenReturn(Flux.just(pet).map(PetUtility::petToPetDto));

        StepVerifier.create(innkeeperServiceImpl.getAll())
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        owner.getPetIds().size() == expOwnerDto.getPets().size() &&
                        expOwnerDto.getPets().isEmpty()
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getAll();
        Mockito.verify(petService, times(1)).getAll();
    }

    @Test
    public void testGetAllEmpty() {
        Mockito.when(ownerService.getAll()).thenReturn(Flux.empty());
        Mockito.when(petService.getAll()).thenReturn(Flux.empty());

        StepVerifier.create(innkeeperServiceImpl.getAll())
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getAll();
        Mockito.verify(petService, times(1)).getAll();
    }

}
