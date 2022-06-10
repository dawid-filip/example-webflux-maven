package com.df.service;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.repository.OwnerDtoRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@Log4j2
public class OwnerDtoServiceTest {

    @InjectMocks
    public OwnerDtoServiceImpl ownerDtoServiceImpl;

    @Mock
    public OwnerDtoRepository ownerDtoRepository;

    @Test
    public void testGetAllOne() {
        PetDto petDto = PetDto.builder().id(1L).name("petName1").age((short) 1).weight((short) 2).length((short) 11).build();
        OwnerDto ownerDto = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petDto));

        Mockito.when(ownerDtoRepository.getAll()).thenReturn(Flux.fromIterable(List.of(ownerDto)));

        StepVerifier.create(ownerDtoServiceImpl.getAll())
                .expectNextMatches(resultOwnerDto -> ownerDto.getId() == resultOwnerDto.getId() &&
                        resultOwnerDto.getFirstName().equalsIgnoreCase(ownerDto.getFirstName()) &&
                        resultOwnerDto.getLastName().equalsIgnoreCase(ownerDto.getLastName()) &&
                        resultOwnerDto.getPets().size() == ownerDto.getPets().size() &&
                        resultOwnerDto.getPets().get(0).getId() == petDto.getId() &&
                        resultOwnerDto.getPets().get(0).getName().equalsIgnoreCase(petDto.getName())
                )
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerDtoRepository, times(1)).getAll();
    }

    @Test
    public void testGetAllTwo() {
        PetDto petDto1 = PetDto.builder().id(1L).name("petName1").age((short) 1).weight((short) 2).length((short) 11).build();
        OwnerDto ownerDto1 = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petDto1));
        PetDto petDto2 = PetDto.builder().id(2L).name("petName2").age((short) 2).weight((short) 4).length((short) 12).build();
        OwnerDto ownerDto2 = new OwnerDto(2L, "firstName2", "lastName2", (short) 22, List.of(petDto2));

        Mockito.when(ownerDtoRepository.getAll()).thenReturn(Flux.fromIterable(List.of(ownerDto1, ownerDto2)));

        StepVerifier.create(ownerDtoServiceImpl.getAll())
                .expectNextMatches(resultOwnerDto -> ownerDto1.getId() == resultOwnerDto.getId() &&
                        resultOwnerDto.getFirstName().equalsIgnoreCase(ownerDto1.getFirstName()) &&
                        resultOwnerDto.getLastName().equalsIgnoreCase(ownerDto1.getLastName()) &&
                        resultOwnerDto.getPets().size() == ownerDto1.getPets().size() &&
                        resultOwnerDto.getPets().get(0).getId() == petDto1.getId() &&
                        resultOwnerDto.getPets().get(0).getName().equalsIgnoreCase(petDto1.getName())
                )
                .expectNextMatches(resultOwnerDto -> ownerDto2.getId() == resultOwnerDto.getId() &&
                        resultOwnerDto.getFirstName().equalsIgnoreCase(ownerDto2.getFirstName()) &&
                        resultOwnerDto.getLastName().equalsIgnoreCase(ownerDto2.getLastName()) &&
                        resultOwnerDto.getPets().size() == ownerDto2.getPets().size() &&
                        resultOwnerDto.getPets().get(0).getId() == petDto2.getId() &&
                        resultOwnerDto.getPets().get(0).getName().equalsIgnoreCase(petDto2.getName())
                )
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerDtoRepository, times(1)).getAll();
    }

    @Test
    public void testGetAllOneWith2PetDtos() {
        PetDto petDto1 = PetDto.builder().id(1L).name("petName1").age((short) 1).weight((short) 2).length((short) 11).build();
        PetDto petDto2 = PetDto.builder().id(2L).name("petName2").age((short) 2).weight((short) 4).length((short) 12).build();
        OwnerDto ownerDto = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of(petDto1, petDto2));

        Mockito.when(ownerDtoRepository.getAll()).thenReturn(Flux.fromIterable(List.of(ownerDto)));

        StepVerifier.create(ownerDtoServiceImpl.getAll())
                .expectNextMatches(resultOwnerDto -> ownerDto.getId() == resultOwnerDto.getId() &&
                        resultOwnerDto.getFirstName().equalsIgnoreCase(ownerDto.getFirstName()) &&
                        resultOwnerDto.getLastName().equalsIgnoreCase(ownerDto.getLastName()) &&
                        resultOwnerDto.getPets().size() == ownerDto.getPets().size() &&
                        resultOwnerDto.getPets().get(0).getId() == petDto1.getId() &&
                        resultOwnerDto.getPets().get(0).getName().equalsIgnoreCase(petDto1.getName()) &&
                        resultOwnerDto.getPets().get(1).getId() == petDto2.getId() &&
                        resultOwnerDto.getPets().get(1).getName().equalsIgnoreCase(petDto2.getName())
                )
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerDtoRepository, times(1)).getAll();
    }

    @Test
    public void testGetAllOneWithNoPetDto() {
        OwnerDto ownerDto = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, List.of());

        Mockito.when(ownerDtoRepository.getAll()).thenReturn(Flux.fromIterable(List.of(ownerDto)));

        StepVerifier.create(ownerDtoServiceImpl.getAll())
                .expectNextMatches(resultOwnerDto -> ownerDto.getId() == resultOwnerDto.getId() &&
                        resultOwnerDto.getFirstName().equalsIgnoreCase(ownerDto.getFirstName()) &&
                        resultOwnerDto.getLastName().equalsIgnoreCase(ownerDto.getLastName()) &&
                        resultOwnerDto.getPets().size() == ownerDto.getPets().size()
                )
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerDtoRepository, times(1)).getAll();
    }

    @Test
    public void testGetAllOneWithNullPetDto() {
        OwnerDto ownerDto = new OwnerDto(1L, "firstName1", "lastName1", (short) 21, null);

        Mockito.when(ownerDtoRepository.getAll()).thenReturn(Flux.fromIterable(List.of(ownerDto)));

        StepVerifier.create(ownerDtoServiceImpl.getAll())
                .expectNextMatches(resultOwnerDto -> ownerDto.getId() == resultOwnerDto.getId() &&
                        resultOwnerDto.getFirstName().equalsIgnoreCase(ownerDto.getFirstName()) &&
                        resultOwnerDto.getLastName().equalsIgnoreCase(ownerDto.getLastName()) &&
                        resultOwnerDto.getPets().isEmpty()
                )
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerDtoRepository, times(1)).getAll();
    }

    @Test
    public void testGetAllEmpty() {
        Mockito.when(ownerDtoRepository.getAll()).thenReturn(Flux.fromIterable(List.of()));

        StepVerifier.create(ownerDtoServiceImpl.getAll())
                .expectNextCount(0L)
                .verifyComplete();

        Mockito.verify(ownerDtoRepository, times(1)).getAll();
    }

}
