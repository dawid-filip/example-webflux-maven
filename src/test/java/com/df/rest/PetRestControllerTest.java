package com.df.rest;

import com.df.dto.PetDto;
import com.df.entity.Pet;
import com.df.repository.PetRepository;
import com.df.service.PetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PetRestController.class)
@Import({PetServiceImpl.class})
public class PetRestControllerTest {

    private static final String BASE_URL = "/api/v1/pet";

    @Autowired
    private WebTestClient webClient;

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
                .body(Mono.just(new PetDto(petBefore)), PetDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PetDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertTrue(entityExchangeResult.getResponseBody().getId()==petAfter.getId());
                });

        verify(petRepository, times(1)).save(petBefore);
    }

    @Test
    void testGetById() {
        Pet pet = new Pet(1L, "petName1", (short)3, (short)3, (short)16);

        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Mono.just(pet));

        webClient.get()
                .uri(BASE_URL + "/{id}",pet.getId())
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
                .uri(BASE_URL + "/{id}",petId)
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
    void testGetAll() {
        Pet pet1 = new Pet(1L, "petName1", (short)3, (short)3, (short)16);
        Pet pet2 = new Pet(2L, "petName2", (short)4, (short)4, (short)17);
        Pet pet3 = new Pet(3L, "petName3", (short)6, (short)6, (short)18);

        Mockito.when(petRepository.findAll()).thenReturn(Flux.just(pet1, pet2, pet3));

        webClient.get()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(3, entityExchangeResult.getResponseBody().size());
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
                .expectBody(List.class)
                .consumeWith(entityExchangeResult -> {
                    assertTrue(entityExchangeResult.getResponseBody().isEmpty());
                });

        verify(petRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        Pet pet = new Pet(1L, "petName1", (short)3, (short)3, (short)16);

        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Mono.just(pet));
        Mockito.when(petRepository.deleteById(pet.getId())).thenReturn(Mono.empty().then());

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
        verify(petRepository, times(1)).deleteById(pet.getId());
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
                .body(Mono.just(new PetDto(petNew)), PetDto.class)
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

}
