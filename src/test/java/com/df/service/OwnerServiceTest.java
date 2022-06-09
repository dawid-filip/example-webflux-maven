package com.df.service;

import com.df.entity.Owner;
import com.df.repository.OwnerRepository;
import com.df.util.OwnerUtility;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OwnerServiceTest {

    @InjectMocks
    public OwnerServiceImpl ownerServiceImpl;

    @Mock
    public OwnerRepository ownerRepository;

    @Test
    public void testGetById() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));

        Mockito.when(ownerRepository.findById(owner.getId())).thenReturn(Mono.just(owner));

        StepVerifier.create(ownerServiceImpl.getById(owner.getId()))
                .expectNextMatches(expOwner -> owner.getId() == expOwner.getId() &&
                        owner.getFirstName().equalsIgnoreCase(expOwner.getFirstName()))
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findById(owner.getId());
    }

    @Test
    public void testGetByIdWhenDoesNotExist() {
        Long ownerId = 1L;
        Mockito.when(ownerRepository.findById(ownerId)).thenReturn(Mono.empty());

        StepVerifier.create(ownerServiceImpl.getById(ownerId))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findById(ownerId);
    }

    @Test
    public void testGetByIdWhenIdNull() {
        StepVerifier.create(ownerServiceImpl.getById(null))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerRepository, never()).findById(anyLong());
    }

    @Test
    public void testGetAllOne() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));

        Mockito.when(ownerRepository.findAll()).thenReturn(Flux.just(owner));

        StepVerifier.create(ownerServiceImpl.getAll())
                .expectNextMatches(expOwner -> owner.getId() == expOwner.getId() &&
                        owner.getPetIds().get(0) == expOwner.getPetIds().get(0) &&
                        owner.getFirstName().equalsIgnoreCase(expOwner.getFirstName()))
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTwo() {
        List<Owner> owners = List.of(new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L)),
                new Owner(2L, "firstName2", "lastName2", (short) 22, List.of()));

        Mockito.when(ownerRepository.findAll()).thenReturn(Flux.fromIterable(owners));

        StepVerifier.create(ownerServiceImpl.getAll())
                .expectNextMatches(expOwner -> owners.get(0).getId() == expOwner.getId() &&
                        owners.get(0).getPetIds().size() == expOwner.getPetIds().size() &&
                        owners.get(0).getFirstName().equalsIgnoreCase(expOwner.getFirstName()))
                .expectNextMatches(expOwner -> owners.get(1).getId() == expOwner.getId() &&
                        owners.get(1).getPetIds().size() == expOwner.getPetIds().size() &&
                        owners.get(1).getFirstName().equalsIgnoreCase(expOwner.getFirstName()))
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllWhenEmpty() {

        Mockito.when(ownerRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(ownerServiceImpl.getAll())
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void testCreate() {
        Owner ownerBefore = new Owner(null, "firstName1", "lastName1", (short) 21, List.of(1L));
        Owner ownerAfter = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));

        Mockito.when(ownerRepository.save(ownerBefore)).thenReturn(Mono.just(ownerAfter));

        StepVerifier.create(ownerServiceImpl.create(OwnerUtility.ownerToOwnerRequest(ownerBefore)))
                .expectNextMatches(expOwner -> ownerAfter.getId() == expOwner.getId() &&
                        ownerAfter.getPetIds().get(0) == expOwner.getPetIds().get(0) &&
                        ownerAfter.getFirstName().equalsIgnoreCase(expOwner.getFirstName()))
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).save(ownerBefore);
    }

    @Test
    public void testCreateWhenIdIsSetThenItWillBeIgnore() {
        Owner ownerBefore = new Owner(2L, "firstName1", "lastName1", (short) 21, List.of(1L));
        Owner ownerAfter = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));

        Mockito.when(ownerRepository.save(ownerBefore)).thenReturn(Mono.just(ownerAfter));

        StepVerifier.create(ownerServiceImpl.create(OwnerUtility.ownerToOwnerRequest(ownerBefore)))
                .expectNextMatches(expOwner -> ownerAfter.getId() == expOwner.getId() &&
                        ownerAfter.getPetIds().get(0) == expOwner.getPetIds().get(0) &&
                        ownerAfter.getFirstName().equalsIgnoreCase(expOwner.getFirstName()))
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).save(ownerBefore);
    }

    @Test
    public void testCreateNull() {
        StepVerifier.create(ownerServiceImpl.create(null))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerRepository, never()).save(any());
    }

    @Test
    public void testDeleteById() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short) 21, List.of(1L));

        Mockito.when(ownerRepository.findById(owner.getId())).thenReturn(Mono.just(owner));
        Mockito.when(ownerRepository.delete(owner)).thenReturn(Mono.empty().then());

        StepVerifier.create(ownerServiceImpl.deleteById(owner.getId()))
                .expectNextMatches(expOwner -> owner.getId() == expOwner.getId() &&
                        owner.getPetIds().get(0) == expOwner.getPetIds().get(0) &&
                        owner.getFirstName().equalsIgnoreCase(expOwner.getFirstName()))
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findById(owner.getId());
        Mockito.verify(ownerRepository, times(1)).delete(owner);
    }

    @Test
    public void testDeleteByIdWhenDoesNotExist() {
        Long ownerId = 1L;
        Mockito.when(ownerRepository.findById(ownerId)).thenReturn(Mono.empty());

        StepVerifier.create(ownerServiceImpl.deleteById(ownerId))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findById(ownerId);
        Mockito.verify(ownerRepository, never()).deleteById(ownerId);
    }

    @Test
    public void testAlterWhenDoesNotExist() {
        Owner owner = new Owner(1L, "firstName1New", "lastName1New", (short) 21, List.of(1L, 2L));

        Mockito.when(ownerRepository.findById(owner.getId())).thenReturn(Mono.empty());

        StepVerifier.create(ownerServiceImpl.alter(OwnerUtility.ownerToOwnerRequest(owner)))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerRepository, times(1)).findById(owner.getId());
        Mockito.verify(ownerRepository, never()).save(any());
    }

    @Test
    public void testAlterWhenIdIsNull() {
        Owner owner = new Owner(null, "firstName1New", "lastName1New", (short) 21, List.of(1L, 2L));

        StepVerifier.create(ownerServiceImpl.alter(OwnerUtility.ownerToOwnerRequest(owner)))
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerRepository, never()).findById(anyLong());
        Mockito.verify(ownerRepository, never()).save(any());
    }

}
