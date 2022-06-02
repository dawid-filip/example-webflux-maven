package com.df.rest;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.entity.Owner;
import com.df.request.OwnerRequest;
import com.df.service.InnkeeperServiceImpl;
import com.df.service.OwnerService;
import com.df.service.PetService;
import com.df.util.OwnerUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = InnkeeperRestController.class)
@Import({InnkeeperServiceImpl.class})
public class InnkeeperRestControllerTest {

    private static final String BASE_URL = "/api/v1/innkeeper";

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private PetService petService;

    @Test
    public void testGetById() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short)21, List.of(1L));
        PetDto pet = new PetDto(1L, "petName1", (short)1, (short)1, (short)11);

        Mockito.when(ownerService.getById(owner.getId())).thenReturn(Mono.just(owner));
        Mockito.when(petService.getByIds(owner.getPetIds())).thenReturn(Flux.just(pet));

        webClient.get()
                .uri(BASE_URL + "/{id}", owner.getId())
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(owner.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(owner.getPetIds().size(), entityExchangeResult.getResponseBody().getPets().stream().count());
                });

        verify(ownerService, times(1)).getById(owner.getId());
        verify(petService, times(1)).getByIds(owner.getPetIds());
    }

    @Test
    public void testGetByIdWhichDoesNotExist() {
        Long id = 1L;
        Mockito.when(ownerService.getById(id)).thenReturn(Mono.empty());
        Mockito.when(petService.getByIds(List.of(id))).thenReturn(Flux.empty());

        webClient.get()
                .uri(BASE_URL + "/{id}", id)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(null, entityExchangeResult.getResponseBody());
                });

        verify(ownerService, times(1)).getById(id);
        verify(petService, never()).getByIds(List.of(1L));
    }

    @Test
    public void testGetAll() {
        Owner owner = new Owner(1L, "firstName1", "lastName1", (short)21, List.of(1L));
        PetDto pet = new PetDto(1L, "petName1", (short)1, (short)1, (short)11);

        Mockito.when(ownerService.getAll()).thenReturn(Flux.just(owner));
        Mockito.when(petService.getAll()).thenReturn(Flux.just(pet));

        webClient.get()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<OwnerDto>>(){})
                .consumeWith(entityExchangeResult -> {
                    assertEquals(1L, entityExchangeResult.getResponseBody().size());
                });

        verify(ownerService, times(1)).getAll();
        verify(petService, times(1)).getAll();
    }

    @Test
    public void testGetAllWhenEmpty() {
        Mockito.when(ownerService.getAll()).thenReturn(Flux.empty());
        Mockito.when(petService.getAll()).thenReturn(Flux.empty());

        webClient.get()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.GET.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<OwnerDto>>(){})
                .consumeWith(entityExchangeResult -> {
                    assertEquals(0L, entityExchangeResult.getResponseBody().size());
                });

        verify(ownerService, times(1)).getAll();
        verify(petService, times(1)).getAll();
    }

    @Test
    void testCreate() {
        PetDto petBefore = new PetDto(null, "petName1", (short)1, (short)1, (short)11);
        OwnerDto ownerBefore = new OwnerDto(null, "firstName1", "lastName1", (short)21, List.of(petBefore));
        PetDto petDtoAfter = new PetDto(1L, "petName1", (short)1, (short)1, (short)11);
        Owner ownerAfter = new Owner(1L, "firstName1", "lastName1", (short)21, List.of(1L));

        Mockito.when(petService.createAll(List.of(petBefore))).thenReturn(Flux.just(petDtoAfter));
        Mockito.when(ownerService.create(OwnerUtility.ownerDtoToOwnerRequest(ownerBefore))).thenReturn(Mono.just(ownerAfter));

        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.just(ownerBefore), OwnerDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OwnerDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(ownerAfter.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(ownerAfter.getPetIds().get(0), entityExchangeResult.getResponseBody().getPets().get(0).getId());
                });

        verify(ownerService, times(1)).create(new OwnerRequest(ownerBefore));
        verify(petService, times(1)).createAll(List.of(petBefore));
    }

    @Test
    void testCreateWhereNoBodyThenBadRequest() {
        Mockito.when(petService.createAll(anyList())).thenReturn(Flux.empty());
        Mockito.when(ownerService.create(any())).thenReturn(Mono.empty());

        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ALLOW, HttpMethod.POST.name())
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(ownerService, never()).create(any());
        verify(petService, never()).createAll(anyList());
    }

    @Test
    void testDelete() {
        PetDto petDto = new PetDto(1L, "petName1", (short)1, (short)1, (short)11);
        OwnerDto ownerDto = new OwnerDto(1L, "firstName1", "lastName1", (short)21, List.of(petDto));

        Mockito.when(ownerService.getById(ownerDto.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerDto)));
        Mockito.when(ownerService.deleteById(ownerDto.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerDto)));
        Mockito.when(petService.getByIds(List.of(petDto.getId()))).thenReturn(Flux.just(petDto));
        Mockito.when(petService.deleteAllById(List.of(petDto.getId()))).thenReturn(Flux.just(petDto));

        webClient.delete()
                .uri(BASE_URL + "/{id}", ownerDto.getId())
                .header(HttpHeaders.ALLOW, HttpMethod.DELETE.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(ownerDto.getId(), entityExchangeResult.getResponseBody().getId());
                    assertEquals(petDto.getId(), entityExchangeResult.getResponseBody().getPets().get(0).getId());
                });

        verify(ownerService, times(1)).getById(ownerDto.getId());
        verify(ownerService, times(1)).deleteById(ownerDto.getId());
        verify(petService, times(1)).getByIds(List.of(petDto.getId()));
        verify(petService, times(1)).deleteAllById(List.of(petDto.getId()));
    }

    @Test
    void testDeleteWhenDoesNotExist() {
        Mockito.when(ownerService.getById(anyLong())).thenReturn(Mono.empty());
        Mockito.when(ownerService.deleteById(anyLong())).thenReturn(Mono.empty());
        Mockito.when(petService.getByIds(anyList())).thenReturn(Flux.empty());
        Mockito.when(petService.deleteAllById(anyList())).thenReturn(Flux.empty());

        webClient.delete()
                .uri(BASE_URL + "/{id}", 1L)
                .header(HttpHeaders.ALLOW, HttpMethod.DELETE.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertNull(entityExchangeResult.getResponseBody());
                });

        verify(ownerService, times(1)).getById(anyLong());
        verify(ownerService, never()).deleteById(anyLong());
        verify(petService, never()).getByIds(anyList());
        verify(petService, never()).deleteAllById(anyList());
    }

    @Test
    void testAlter() {
        PetDto petDtoBefore = new PetDto(1L, "petName1", (short)1, (short)1, (short)11);
        OwnerDto ownerDtoBefore = new OwnerDto(1L, "firstName1", "lastName1", (short)21, List.of(petDtoBefore));

        PetDto petDtoNew = new PetDto(1L, "petName2", (short)1, (short)1, (short)11);
        OwnerDto ownerDtoNew = new OwnerDto(1L, "firstName2", "lastName2", (short)21, List.of(petDtoNew));

        Mockito.when(ownerService.getById(ownerDtoBefore.getId())).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerDtoBefore)));
        Mockito.when(ownerService.alter(OwnerUtility.ownerDtoToOwnerRequest(ownerDtoNew))).thenReturn(Mono.just(OwnerUtility.ownerDtoToOwner(ownerDtoNew)));
        Mockito.when(petService.getByIds(List.of(petDtoBefore.getId()))).thenReturn(Flux.just(petDtoBefore));
        Mockito.when(petService.alterAll(List.of(petDtoNew))).thenReturn(Flux.just(petDtoNew));

        webClient.patch()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.PATCH.toString())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(ownerDtoNew), OwnerDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OwnerDto.class)
                .consumeWith(entityExchangeResult -> {
                    assertEquals(ownerDtoNew.getFirstName(), entityExchangeResult.getResponseBody().getFirstName());
                    assertEquals(petDtoNew.getName(), entityExchangeResult.getResponseBody().getPets().get(0).getName());
                });

        verify(ownerService, times(1)).getById(ownerDtoBefore.getId());
        verify(ownerService, times(1)).alter(OwnerUtility.ownerDtoToOwnerRequest(ownerDtoNew));
        verify(petService, times(1)).getByIds(List.of(petDtoBefore.getId()));
        verify(petService, times(1)).alterAll(List.of(petDtoNew));
    }

    @Test
    void testAlterWhenNoBodyThenBadRequest() {
        Mockito.when(ownerService.getById(anyLong())).thenReturn(Mono.empty());
        Mockito.when(ownerService.alter(any())).thenReturn(Mono.empty());
        Mockito.when(petService.getByIds(anyList())).thenReturn(Flux.empty());
        Mockito.when(petService.alterAll(anyList())).thenReturn(Flux.empty());

        webClient.patch()
                .uri(BASE_URL)
                .header(HttpHeaders.ALLOW, HttpMethod.PATCH.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.empty(), String.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(ownerService, never()).getById(anyLong());
        verify(ownerService, never()).alter(any());
        verify(petService, never()).getByIds(anyList());
        verify(petService, never()).alterAll(anyList());
    }

}
