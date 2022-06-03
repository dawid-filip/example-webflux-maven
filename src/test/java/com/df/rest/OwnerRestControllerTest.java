package com.df.rest;

import com.df.entity.Owner;
import com.df.repository.OwnerRepository;
import com.df.request.OwnerRequest;
import com.df.rest.basic.BasicControllerTestConfig;
import com.df.service.OwnerServiceImpl;
import com.df.util.OwnerUtility;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = OwnerRestController.class)
@Import({OwnerServiceImpl.class})
public class OwnerRestControllerTest extends BasicControllerTestConfig {

    private static final String BASE_URL = "/api/v1/owner";

    @MockBean
    private OwnerRepository ownerRepository;

    @Test
    void testCreate() {
        Owner ownerBefore = new Owner(null, "firstName1", "lastName1", (short)20, List.of(1L));
        Owner ownerAfter = new Owner(1L, "firstName1", "lastName1", (short)20, List.of(1L));

        Mockito.when(ownerRepository.save(ownerBefore)).thenReturn(Mono.just(ownerAfter));

        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.just(OwnerUtility.ownerToOwnerRequest(ownerBefore)), OwnerRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OwnerRequest.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(ownerAfter.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(ownerAfter.getPetIds().size(), entityExchangeResult.getResponseBody().getPetIds().size());
                });

        verify(ownerRepository, times(1)).save(ownerBefore);
    }

    @Test
    void testCreateWhenNoBodyThenBadRequest() {
        Mockito.when(ownerRepository.save(any())).thenReturn(Mono.empty());

        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(ownerRepository, never()).save(any());
    }

    @Test
    void testGetById() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short)20, List.of(1L));

        Mockito.when(ownerRepository.findById(owner.getId())).thenReturn(Mono.just(owner));

        webClient.get()
                .uri(BASE_URL + "/{id}",owner.getId())
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerRequest.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(owner.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(owner.getFirstName(), entityExchangeResult.getResponseBody().getFirstName());
                    assertEquals(owner.getLastName(), entityExchangeResult.getResponseBody().getLastName());
                });

        verify(ownerRepository, times(1)).findById(owner.getId());
    }

    @Test
    void testGetByIdWhichDoesNotExist() {
        Long ownerId = 1L;
        Mockito.when(ownerRepository.findById(ownerId)).thenReturn(Mono.empty());

        webClient.get()
                .uri(BASE_URL + "/{id}",ownerId)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerRequest.class)
                .consumeWith(entityExchangeResult -> {
                    assertNull(entityExchangeResult.getResponseBody());
                });

        verify(ownerRepository, times(1)).findById(ownerId);
    }

    @Test
    void testGetAll() {
        List<Owner> owners = List.of(new Owner(1L, "firstName1", "lastName1", (short)21, null),
                new Owner(2L, "firstName2", "lastName2", (short)22, List.of(1L)),
                new Owner(3L, "firstName3", "lastName3", (short)23, List.of(1L, 2L)));

        Mockito.when(ownerRepository.findAll()).thenReturn(Flux.fromIterable(owners));

        webClient.get()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(getListOwnerType())
                .consumeWith(entityExchangeResult -> {
                    assertEquals(owners.size(), entityExchangeResult.getResponseBody().size());
                    assertEquals(owners.get(0).getPetIds(), entityExchangeResult.getResponseBody().get(0).getPetIds());
                    assertEquals(owners.get(1).getPetIds().size(), entityExchangeResult.getResponseBody().get(1).getPetIds().size());
                    assertEquals(owners.get(2).getPetIds().size(), entityExchangeResult.getResponseBody().get(2).getPetIds().size());
                });

        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void testGetAllWhenEmpty() {

        Mockito.when(ownerRepository.findAll()).thenReturn(Flux.empty());

        webClient.get()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(getListOwnerType())
                .consumeWith(entityExchangeResult -> {
                    assertTrue(entityExchangeResult.getResponseBody().isEmpty());
                });

        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void testDelete() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short)21, List.of(1L, 2L));

        Mockito.when(ownerRepository.findById(owner.getId())).thenReturn(Mono.just(owner));
        Mockito.when(ownerRepository.deleteById(owner.getId())).thenReturn(Mono.empty().then());

        webClient.delete()
                .uri(BASE_URL + "/{id}", owner.getId())
                .header(HttpHeaders.ALLOW, HttpMethod.DELETE.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Owner.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(owner.getId(), entityExchangeResult.getResponseBody().getId());
                });

        verify(ownerRepository, times(1)).findById(owner.getId());
        verify(ownerRepository, times(1)).deleteById(owner.getId());
    }

    @Test
    void testDeleteWhenDoesNotExist() {
        Long ownerId = 1L;
        Mockito.when(ownerRepository.findById(ownerId)).thenReturn(Mono.empty());
        Mockito.when(ownerRepository.deleteById(ownerId)).thenReturn(Mono.empty().then());

        webClient.delete()
                .uri(BASE_URL + "/{id}", ownerId)
                .header(HttpHeaders.ALLOW, HttpMethod.DELETE.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    assertNull(entityExchangeResult.getResponseBody());
                });

        verify(ownerRepository, times(1)).findById(ownerId);
        verify(ownerRepository, times(0)).deleteById(ownerId);
    }

    @Test
    void testAlter() {
        Owner ownerBefore = new Owner(1L, "firstName1", "lastName1", (short)20, List.of(1L));
        Owner ownerNew = new Owner(1L, "firstName2", "lastName2", (short)21, List.of(1L, 2L));

        Mockito.when(ownerRepository.findById(ownerNew.getId())).thenReturn(Mono.just(ownerBefore));
        Mockito.when(ownerRepository.save(ownerNew)).thenReturn(Mono.just(ownerNew));

        webClient.patch()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.PATCH.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(ownerNew), Owner.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerRequest.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(ownerNew.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(ownerNew.getFirstName(), entityExchangeResult.getResponseBody().getFirstName());
                    assertEquals(ownerNew.getLastName(), entityExchangeResult.getResponseBody().getLastName());
                });

        verify(ownerRepository, times(1)).findById(ownerNew.getId());
        verify(ownerRepository, times(1)).save(ownerNew);
    }

    @Test
    void testAlterWhenNoBodyThenBadRequest() {
        Mockito.when(ownerRepository.findById(anyLong())).thenReturn(Mono.empty());
        Mockito.when(ownerRepository.save(any())).thenReturn(Mono.empty());

        webClient.patch()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.PATCH.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(ownerRepository, never()).findById(anyLong());
        verify(ownerRepository, never()).save(any());
    }

}
