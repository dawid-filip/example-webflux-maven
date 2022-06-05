package com.df.service;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.entity.Owner;
import com.df.entity.Pet;
import com.df.request.OwnerRequest;
import com.df.util.OwnerUtility;
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
import static org.mockito.ArgumentMatchers.anyLong;
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
                .expectNextCount(0)
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(ownerId);
        Mockito.verify(petService, never()).getByIds(anyList());
    }

    @Test
    public void testGetByIdWhenOwnerIsNull() {
        Mockito.when(ownerService.getById(null)).thenReturn(Mono.empty());

        StepVerifier.create(innkeeperServiceImpl.getById(null))
                .expectNextCount(0)
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
                .expectNextCount(0)
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getAll();
        Mockito.verify(petService, times(1)).getAll();
    }

    @Test
    public void testCreate() {
        PetDto petBefore = new PetDto(null, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerBefore = new OwnerDto(null, "firstName1", "lastName1", (short) 21, List.of(petBefore));
        OwnerRequest ownerRequest = new OwnerRequest(null, "firstName1", "lastName1", (short)21, List.of(1L));

        PetDto petAfter = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerAfter = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petAfter));

        Mockito.when(petService.createAll(ownerBefore.getPets())).thenReturn(Flux.fromIterable(ownerAfter.getPets()));
        Mockito.when(ownerService.create(ownerRequest)).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerAfter)));

        StepVerifier.create(innkeeperServiceImpl.create(OwnerUtility.ownerDtoToOwnerDto(ownerBefore)))
                .expectNextMatches(expOwnerDto -> ownerAfter.getId() == expOwnerDto.getId() &&
                        ownerAfter.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        ownerAfter.getPets().size() == expOwnerDto.getPets().size() &&
                        ownerAfter.getPets().get(0).getId() == expOwnerDto.getPets().get(0).getId() &&
                        ownerAfter.getPets().get(0).getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(petService, times(1)).createAll(ownerBefore.getPets());
        Mockito.verify(ownerService, times(1)).create(ownerRequest);
    }

    @Test
    public void testCreateWhenOwnerIdSetThenItIsIgnore() {
        PetDto petBefore = new PetDto(2L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerBefore = new OwnerDto(2L, "firstName1", "lastName1", (short) 21, List.of(petBefore));
        OwnerRequest ownerRequest = new OwnerRequest(2L, "firstName1", "lastName1", (short)21, List.of(1L));

        PetDto petAfter = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerAfter = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petAfter));

        Mockito.when(petService.createAll(ownerBefore.getPets())).thenReturn(Flux.fromIterable(ownerAfter.getPets()));
        Mockito.when(ownerService.create(ownerRequest)).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerAfter)));

        StepVerifier.create(innkeeperServiceImpl.create(OwnerUtility.ownerDtoToOwnerDto(ownerBefore)))
                .expectNextMatches(expOwnerDto -> ownerAfter.getId() == expOwnerDto.getId() &&
                        ownerAfter.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        ownerAfter.getPets().size() == expOwnerDto.getPets().size() &&
                        ownerAfter.getPets().get(0).getId() == expOwnerDto.getPets().get(0).getId() &&
                        ownerAfter.getPets().get(0).getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(petService, times(1)).createAll(ownerBefore.getPets());
        Mockito.verify(ownerService, times(1)).create(ownerRequest);
    }

    @Test
    public void testCreatePetsEmpty() {
        OwnerDto ownerBefore = new OwnerDto(null, "firstName1", "lastName1", (short) 21, List.of());
        OwnerDto ownerAfter = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of());

        Mockito.when(petService.createAll(ownerBefore.getPets())).thenReturn(Flux.fromIterable(ownerAfter.getPets()));
        Mockito.when(ownerService.create(OwnerUtility.ownerDtoToOwnerRequest(ownerBefore))).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerAfter)));

        StepVerifier.create(innkeeperServiceImpl.create(OwnerUtility.ownerDtoToOwnerDto(ownerBefore)))
                .expectNextMatches(expOwnerDto -> ownerAfter.getId() == expOwnerDto.getId() &&
                        ownerAfter.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        expOwnerDto.getPets().isEmpty()
                )
                .verifyComplete();

        Mockito.verify(petService, times(1)).createAll(ownerBefore.getPets());
        Mockito.verify(ownerService, times(1)).create(OwnerUtility.ownerDtoToOwnerRequest(ownerBefore));
    }

    @Test
    public void testCreatePetsNull() {
        OwnerDto ownerBefore = new OwnerDto(null, "firstName1", "lastName1", (short) 21, null);
        OwnerDto ownerAfter = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of());

        Mockito.when(petService.createAll(ownerBefore.getPets())).thenReturn(Flux.empty());
        Mockito.when(ownerService.create(OwnerUtility.ownerDtoToOwnerRequest(ownerBefore))).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerAfter)));

        StepVerifier.create(innkeeperServiceImpl.create(OwnerUtility.ownerDtoToOwnerDto(ownerBefore)))
                .expectNextMatches(expOwnerDto -> ownerAfter.getId() == expOwnerDto.getId() &&
                        ownerAfter.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        expOwnerDto.getPets().isEmpty()
                )
                .verifyComplete();

        Mockito.verify(petService, times(1)).createAll(null);
        Mockito.verify(ownerService, times(1)).create(OwnerUtility.ownerDtoToOwnerRequest(ownerBefore));
    }

    @Test
    public void testDeleteById() {
        PetDto pet = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto owner = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(pet));

        Mockito.when(ownerService.getById(owner.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(owner)));
        Mockito.when(petService.getByIds(List.of(pet.getId()))).thenReturn(Flux.fromIterable(owner.getPets()));
        Mockito.when(ownerService.deleteById(owner.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(owner)));
        Mockito.when(petService.deleteByIds(List.of(pet.getId()))).thenReturn(Flux.fromIterable(owner.getPets()));

        StepVerifier.create(innkeeperServiceImpl.deleteById(owner.getId()))
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        owner.getPets().size() == expOwnerDto.getPets().size() &&
                        pet.getId() == expOwnerDto.getPets().get(0).getId() &&
                        pet.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(owner.getId());
        Mockito.verify(petService, times(1)).getByIds(List.of(pet.getId()));
        Mockito.verify(ownerService, times(1)).deleteById(owner.getId());
        Mockito.verify(petService, times(1)).deleteByIds(List.of(pet.getId()));
    }

    @Test
    public void testDeleteByIdWhenPetsEmpty() {
        OwnerDto owner = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of());

        Mockito.when(ownerService.getById(owner.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(owner)));
        Mockito.when(petService.getByIds(List.of())).thenReturn(Flux.empty());
        Mockito.when(ownerService.deleteById(owner.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(owner)));
        Mockito.when(petService.deleteByIds(List.of())).thenReturn(Flux.empty());

        StepVerifier.create(innkeeperServiceImpl.deleteById(owner.getId()))
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        expOwnerDto.getPets().isEmpty()
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(owner.getId());
        Mockito.verify(petService, times(1)).getByIds(List.of());
        Mockito.verify(ownerService, times(1)).deleteById(owner.getId());
        Mockito.verify(petService, times(1)).deleteByIds(List.of());
    }

    @Test
    public void testDeleteByIdWhenPetsNull() {
        OwnerDto owner = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, null);

        Mockito.when(ownerService.getById(owner.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(owner)));
        Mockito.when(petService.getByIds(anyList())).thenReturn(Flux.empty());
        Mockito.when(ownerService.deleteById(owner.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(owner)));
        Mockito.when(petService.deleteByIds(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(innkeeperServiceImpl.deleteById(owner.getId()))
                .expectNextMatches(expOwnerDto -> owner.getId() == expOwnerDto.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        expOwnerDto.getPets().isEmpty()
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(owner.getId());
        Mockito.verify(petService, times(1)).getByIds(anyList());
        Mockito.verify(ownerService, times(1)).deleteById(owner.getId());
        Mockito.verify(petService, times(1)).deleteByIds(anyList());
    }

    @Test
    public void testDeleteByIdWhenOwnerDoesNotExist() {
        Long ownerId = 1L;

        Mockito.when(ownerService.getById(ownerId)).thenReturn(Mono.empty());

        StepVerifier.create(innkeeperServiceImpl.deleteById(ownerId))
                .expectNextCount(0)
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(ownerId);
        Mockito.verify(petService, never()).getByIds(anyList());
        Mockito.verify(ownerService, never()).deleteById(anyLong());
        Mockito.verify(petService, never()).deleteByIds(anyList());
    }

    @Test
    public void testAlter() {
        PetDto petBefore = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerBefore = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petBefore));

        PetDto petNew = new PetDto(1L, "petName1New", (short) 3, (short) 4, (short) 12);
        OwnerDto ownerNew = new OwnerDto(1L, "firstName1New", "lastName1New", (short) 22, List.of(petNew));

        Mockito.when(ownerService.getById(ownerBefore.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerBefore)));
        Mockito.when(petService.getByIds(List.of(petBefore.getId()))).thenReturn(Flux.fromIterable(ownerBefore.getPets()));
        Mockito.when(ownerService.alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew))).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerNew)));
        Mockito.when(petService.alterAll(ownerNew.getPets())).thenReturn(Flux.fromIterable(ownerNew.getPets()));

        StepVerifier.create(innkeeperServiceImpl.alter(OwnerUtility.ownerDtoToOwnerDto(ownerNew)))
                .expectNextMatches(expOwnerDto -> ownerNew.getId() == expOwnerDto.getId() &&
                        ownerNew.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        ownerNew.getPets().size() == expOwnerDto.getPets().size() &&
                        petNew.getId() == expOwnerDto.getPets().get(0).getId() &&
                        petNew.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(ownerBefore.getId());
        Mockito.verify(petService, times(1)).getByIds(List.of(petBefore.getId()));
        Mockito.verify(ownerService, times(1)).alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew));
        Mockito.verify(petService, times(1)).alterAll(ownerNew.getPets());
    }

    @Test
    public void testAlterWhenPetAlterOnly() {
        PetDto petBefore = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerBefore = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petBefore));

        PetDto petNew = new PetDto(1L, "petName1New", (short) 3, (short) 4, (short) 12);
        OwnerDto ownerNew = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petNew));

        Mockito.when(ownerService.getById(ownerBefore.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerBefore)));
        Mockito.when(petService.getByIds(List.of(petBefore.getId()))).thenReturn(Flux.fromIterable(ownerBefore.getPets()));
        Mockito.when(ownerService.alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew))).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerNew)));
        Mockito.when(petService.alterAll(ownerNew.getPets())).thenReturn(Flux.fromIterable(ownerNew.getPets()));

        StepVerifier.create(innkeeperServiceImpl.alter(OwnerUtility.ownerDtoToOwnerDto(ownerNew)))
                .expectNextMatches(expOwnerDto -> ownerNew.getId() == expOwnerDto.getId() &&
                        ownerNew.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        ownerNew.getPets().size() == expOwnerDto.getPets().size() &&
                        petNew.getId() == expOwnerDto.getPets().get(0).getId() &&
                        petNew.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(ownerBefore.getId());
        Mockito.verify(petService, times(1)).getByIds(List.of(petBefore.getId()));
        Mockito.verify(ownerService, times(1)).alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew));
        Mockito.verify(petService, times(1)).alterAll(ownerNew.getPets());
    }

    @Test
    public void testAlterWhenOwnerAlterOnly() {
        PetDto petBefore = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerBefore = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petBefore));

        PetDto petNew = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerNew = new OwnerDto(1L, "firstName1New", "lastName1New", (short) 21, List.of(petNew));

        Mockito.when(ownerService.getById(ownerBefore.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerBefore)));
        Mockito.when(petService.getByIds(List.of(petBefore.getId()))).thenReturn(Flux.fromIterable(ownerBefore.getPets()));
        Mockito.when(ownerService.alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew))).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerNew)));
        Mockito.when(petService.alterAll(ownerNew.getPets())).thenReturn(Flux.fromIterable(ownerNew.getPets()));

        StepVerifier.create(innkeeperServiceImpl.alter(OwnerUtility.ownerDtoToOwnerDto(ownerNew)))
                .expectNextMatches(expOwnerDto -> ownerNew.getId() == expOwnerDto.getId() &&
                        ownerNew.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        ownerNew.getPets().size() == expOwnerDto.getPets().size() &&
                        petNew.getId() == expOwnerDto.getPets().get(0).getId() &&
                        petNew.getName().equalsIgnoreCase(expOwnerDto.getPets().get(0).getName())
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(ownerBefore.getId());
        Mockito.verify(petService, times(1)).getByIds(List.of(petBefore.getId()));
        Mockito.verify(ownerService, times(1)).alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew));
        Mockito.verify(petService, times(1)).alterAll(ownerNew.getPets());
    }

    @Test
    public void testAlterWhenOwnerIdNotProvided() {
        PetDto petBefore = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerBefore = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petBefore));

        PetDto petNew = new PetDto(1L, "petName1New", (short) 3, (short) 4, (short) 12);
        OwnerDto ownerNew = new OwnerDto(null, "firstName1New", "lastName1New", (short) 22, List.of(petNew));

        Mockito.when(ownerService.getById(null)).thenReturn(Mono.empty());

        StepVerifier.create(innkeeperServiceImpl.alter(OwnerUtility.ownerDtoToOwnerDto(ownerNew)))
                .expectNextCount(0)
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(null);
        Mockito.verify(petService, never()).getByIds(List.of(petBefore.getId()));
        Mockito.verify(ownerService, never()).alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew));
        Mockito.verify(petService, never()).alterAll(ownerNew.getPets());
    }

    @Test
    public void testAlterWhenNewPetsAreEmpty() {
        PetDto petBefore = new PetDto(1L, "petName1", (short) 2, (short) 3, (short) 11);
        OwnerDto ownerBefore = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petBefore));

        OwnerDto ownerNew = new OwnerDto(1L, "firstName1New", "lastName1New", (short) 21, List.of());

        Mockito.when(ownerService.getById(ownerBefore.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerBefore)));
        Mockito.when(petService.getByIds(List.of(petBefore.getId()))).thenReturn(Flux.fromIterable(ownerBefore.getPets()));
        Mockito.when(ownerService.alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew))).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerNew)));
        Mockito.when(petService.alterAll(ownerNew.getPets())).thenReturn(Flux.fromIterable(ownerNew.getPets()));

        StepVerifier.create(innkeeperServiceImpl.alter(OwnerUtility.ownerDtoToOwnerDto(ownerNew)))
                .expectNextMatches(expOwnerDto -> ownerNew.getId() == expOwnerDto.getId() &&
                        ownerNew.getFirstName().equalsIgnoreCase(expOwnerDto.getFirstName()) &&
                        ownerNew.getPets().size() == expOwnerDto.getPets().size() &&
                        expOwnerDto.getPets().isEmpty()
                )
                .verifyComplete();

        Mockito.verify(ownerService, times(1)).getById(ownerBefore.getId());
        Mockito.verify(petService, times(1)).getByIds(List.of(petBefore.getId()));
        Mockito.verify(ownerService, times(1)).alter(OwnerUtility.ownerDtoToOwnerRequest(ownerNew));
        Mockito.verify(petService, times(1)).alterAll(ownerNew.getPets());
    }

}
