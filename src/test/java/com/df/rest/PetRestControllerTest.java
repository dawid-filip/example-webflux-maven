package com.df.rest;

import com.df.dto.PetDto;
import com.df.entity.Pet;
import com.df.repository.PetRepository;
import com.df.rest.basic.BasicControllerTestConfig;
import com.df.service.PetServiceImpl;
import com.df.util.GeneralUtility;
import com.df.util.PetUtility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.df.util.GeneralUtility.getRandomValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = PetRestController.class)
@Import({PetServiceImpl.class, GeneralUtility.class})
public class PetRestControllerTest extends BasicControllerTestConfig {

    private static final String BASE_URL = "/api/v1/pet";

    @MockBean
    private PetRepository petRepository;

    @Test
    void testCreate() {
        Pet petBefore = new Pet(null, "petName1", (short)3, (short)3, (short)16);
        Pet petAfter = new Pet(1L, "petName1", (short)3, (short)3, (short)16);

        Mockito.when(petRepository.save(petBefore)).thenReturn(Mono.just(petAfter));

        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.just(PetUtility.petToPetDto(petBefore)), PetDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PetDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertTrue(entityExchangeResult.getResponseBody().getId() == petAfter.getId());
                });

        verify(petRepository, times(1)).save(petBefore);
    }

    @Test
    void testCreateWhenNoBodyThenBadRequest() {
        Mockito.when(petRepository.save(any())).thenReturn(Mono.empty());

        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(petRepository, never()).save(any());
    }

    @Test
    void testCreateAll() {
        List<Pet> petsBefore = List.of(new Pet(null, "petName1", (short)1, (short)1, (short)11),
                new Pet(null, "petName2", (short)2, (short)2, (short)12));
        List<Pet> petsAfter = List.of(new Pet(1L, "petName1", (short)1, (short)1, (short)11),
                new Pet(2L, "petName2", (short)2, (short)2, (short)12));

        Mockito.when(petRepository.saveAll(petsBefore)).thenReturn(Flux.fromIterable(petsAfter));

        webClient.post()
                .uri(BASE_URL + "/many")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.just(petsBefore), getListPetDtoType())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(getListPetDtoType())
                .consumeWith(entityExchangeResult -> {
                    assertEquals(petsAfter.size(), entityExchangeResult.getResponseBody().size());
                    assertEquals(petsAfter.get(0).getId(), entityExchangeResult.getResponseBody().get(0).getId());
                    assertEquals(petsAfter.get(1).getId(), entityExchangeResult.getResponseBody().get(1).getId());
                });

        verify(petRepository, times(1)).saveAll(petsBefore);
    }

    @Test
    void testCreateAllWhenNoBodyThenBadRequest() {
        Mockito.when(petRepository.saveAll(anyList())).thenReturn(Flux.empty());

        webClient.post()
                .uri(BASE_URL + "/many")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(petRepository, never()).saveAll(anyList());
    }

    @Test
    void testGetById() {
        Pet pet = new Pet(1L, "petName1", (short) 3, (short) 3, (short) 16);

        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Mono.just(pet));

        webClient.get()
                .uri(BASE_URL + "/{id}", pet.getId())
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(pet.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(pet.getName(), entityExchangeResult.getResponseBody().getName());
                });

        verify(petRepository, times(1)).findById(pet.getId());
    }

    @Test
    void testGetRandom() {
        long randomId = getRandomValue(3);
        Pet pet = new Pet(randomId, "petName" + randomId, (short) randomId, (short) randomId, (short) (randomId * 2));

        Mockito.when(petRepository.count()).thenReturn(Mono.just(randomId + 1));
        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Mono.just(pet));

        webClient.get()
                .uri(BASE_URL + "/id/random")
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(pet.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(pet.getName(), entityExchangeResult.getResponseBody().getName());
                });

        verify(petRepository, times(1)).findById(pet.getId());
    }

    @Test
    void testGetByIdWhichDoesNotExist() {
        Long petId = 1L;
        Mockito.when(petRepository.findById(petId)).thenReturn(Mono.empty());

        webClient.get()
                .uri(BASE_URL + "/{id}", petId)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertNull(entityExchangeResult.getResponseBody());
                });

        verify(petRepository, times(1)).findById(petId);
    }

    @Test
    void testGetByIds() {
        Pet pet1 = new Pet(1L, "petName1", (short)3, (short)3, (short)16);
        Pet pet2 = new Pet(2L, "petName2", (short)4, (short)4, (short)17);

        Mockito.when(petRepository.findAllById(List.of(pet1.getId(), pet2.getId()))).thenReturn(Flux.just(pet1, pet2));

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/ids")
                        .queryParam("id", 1L)
                        .queryParam("id", 2L)
                        .build())
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(getListPetDtoType())
                .consumeWith(entityExchangeResult -> {
                    assertEquals(2, entityExchangeResult.getResponseBody().size());
                });

        verify(petRepository, times(1)).findAllById(List.of(pet1.getId(), pet2.getId()));
    }

    @Test
    void testGetByIdsWhichDoesNotExist() {
        Mockito.when(petRepository.findAllById(List.of(1L))).thenReturn(Flux.empty());

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/ids")
                        .queryParam("id", 1L)
                        .build())
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(getListPetDtoType())
                .consumeWith(entityExchangeResult -> {
                    assertEquals(0, entityExchangeResult.getResponseBody().size());
                });

        verify(petRepository, times(1)).findAllById(List.of(1L));
    }

    @Test
    void testGetByIdsWhen1ExistAnd1No() {
        Pet pet1 = new Pet(1L, "petName1", (short)3, (short)3, (short)16);
        Mockito.when(petRepository.findAllById(List.of(pet1.getId(), 2L))).thenReturn(Flux.just(pet1));

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/ids")
                        .queryParam("id", 1L)
                        .queryParam("id", 2L)
                        .build())
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(getListPetDtoType())
                .consumeWith(entityExchangeResult -> {
                    assertEquals(1, entityExchangeResult.getResponseBody().size());
                });

        verify(petRepository, times(1)).findAllById(List.of(pet1.getId(), 2L));
    }

    @Test
    void testGetByIdsWhereNoIdsThenBadRequest() {
        Mockito.when(petRepository.findAllById(anyList())).thenReturn(Flux.empty());

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/ids").build())
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isBadRequest();

        verify(petRepository, never()).findAllById(anyList());
    }

    @Test
    void testGetAll() {
        List<Pet> pets = List.of(new Pet(1L, "petName1", (short)3, (short)3, (short)16),
                new Pet(2L, "petName2", (short)4, (short)4, (short)17),
                new Pet(3L, "petName3", (short)6, (short)6, (short)18));

        Mockito.when(petRepository.findAll()).thenReturn(Flux.fromIterable(pets));

        webClient.get()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(getListPetDtoType())
                .consumeWith(entityExchangeResult -> {
                    assertEquals(pets.size(), entityExchangeResult.getResponseBody().size());
                });

        verify(petRepository, times(1)).findAll();
    }

    @Test
    void testGetAllWhenEmpty() {

        Mockito.when(petRepository.findAll()).thenReturn(Flux.empty());

        webClient.get()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(getListPetDtoType())
                .consumeWith(entityExchangeResult -> {
                    assertTrue(entityExchangeResult.getResponseBody().isEmpty());
                });

        verify(petRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        Pet pet = new Pet(1L, "petName1", (short)3, (short)3, (short)16);

        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Mono.just(pet));
        Mockito.when(petRepository.delete(pet)).thenReturn(Mono.empty().then());

        webClient.delete()
                .uri(BASE_URL + "/{id}", pet.getId())
                .header(HttpHeaders.ALLOW, HttpMethod.DELETE.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(pet.getId(), entityExchangeResult.getResponseBody().getId());
                });

        verify(petRepository, times(1)).findById(pet.getId());
        verify(petRepository, times(1)).delete(pet);
    }

    @Test
    void testDeleteWhenDoesNotExist() {
        Long petId = 1L;
        Mockito.when(petRepository.findById(petId)).thenReturn(Mono.empty());
        Mockito.when(petRepository.deleteById(petId)).thenReturn(Mono.empty().then());

        webClient.delete()
                .uri(BASE_URL + "/{id}", petId)
                .header(HttpHeaders.ALLOW, HttpMethod.DELETE.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    assertNull(entityExchangeResult.getResponseBody());
                });

        verify(petRepository, times(1)).findById(petId);
        verify(petRepository, times(0)).deleteById(petId);
    }

    @Test
    void testAlter() {
        Pet petBefore = new Pet(1L, "petName1", (short)3, (short)3, (short)16);
        Pet petNew = new Pet(1L, "petName2", (short)4, (short)4, (short)17);

        Mockito.when(petRepository.findById(petNew.getId())).thenReturn(Mono.just(petBefore));
        Mockito.when(petRepository.save(petNew)).thenReturn(Mono.just(petNew));

        webClient.patch()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.PATCH.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(PetUtility.petToPetDto(petNew)), PetDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PetDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(petNew.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(petNew.getName(), entityExchangeResult.getResponseBody().getName());
                });

        verify(petRepository, times(1)).findById(petNew.getId());
        verify(petRepository, times(1)).save(petNew);
    }

    @Test
    void testAlterWhenNoBodyThenBadRequest() {
        Mockito.when(petRepository.findById(anyLong())).thenReturn(Mono.empty());
        Mockito.when(petRepository.save(any())).thenReturn(Mono.empty());

        webClient.patch()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.PATCH.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(petRepository, never()).findById(anyLong());
        verify(petRepository, never()).save(any());
    }

    @Test
    void testAlterAll() {
        List<Long> petIds = List.of(1L, 2L);
        List<Pet> petsCurrent = List.of(new Pet(1L, "petName1", (short)1, (short)1, (short)11),
                new Pet(2L, "petName2", (short)2, (short)2, (short)12));
        List<Pet> petsNew = List.of(new Pet(1L, "petName1New", (short)1, (short)1, (short)11),
                new Pet(2L, "petName2New", (short)2, (short)2, (short)12));

        Mockito.when(petRepository.findAllById(petIds)).thenReturn(Flux.fromIterable(petsCurrent));
        Mockito.when(petRepository.saveAll(petsNew)).thenReturn(Flux.fromIterable(petsNew));

        webClient.patch()
                .uri(BASE_URL + "/many")
                .header(HttpHeaders.ALLOW, HttpMethod.PATCH.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(petsNew), getListPetDtoType())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(getListPetDtoType())
                .consumeWith(entityExchangeResult -> {
                    assertEquals(petsNew.size(), entityExchangeResult.getResponseBody().size());
                    assertEquals(petsNew.get(0).getName(), entityExchangeResult.getResponseBody().get(0).getName());
                    assertEquals(petsNew.get(1).getName(), entityExchangeResult.getResponseBody().get(1).getName());
                });

        verify(petRepository, times(1)).findAllById(petIds);
        verify(petRepository, times(1)).saveAll(petsNew);
    }

    @Test
    void testAlterAllWhenNoBodyThenBadRequest() {
        Mockito.when(petRepository.findAllById(anyList())).thenReturn(Flux.empty());
        Mockito.when(petRepository.saveAll(anyList())).thenReturn(Flux.empty());

        webClient.patch()
                .uri(BASE_URL + "/many")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(petRepository, never()).findAllById(anyList());
        verify(petRepository, never()).saveAll(anyList());
    }

}
