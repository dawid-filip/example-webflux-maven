package com.df.rest;

import com.df.dto.PetDto;
import com.df.service.PetService;
import com.df.util.RequestUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PetHandler {

    private final PetService petService;

    public Mono<ServerResponse> alterAll(ServerRequest serverRequest) {
        Flux<PetDto> petDtoFlux = serverRequest.bodyToFlux(PetDto.class);

        return petDtoFlux
                .collectList()
                .flatMap(petDtos -> {
                    if (Objects.isNull(petDtos) || petDtos.isEmpty()) {
                        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .body(Mono.just("Invalid petDtos " + petDtos + " entities!"), List.class);
                    }

                    return ServerResponse.status(HttpStatus.OK)
                            .body(petService.alterAll(petDtos), List.class);
                })
                .map(p -> p);
    }

    public Mono<ServerResponse> alter(ServerRequest serverRequest) {
        Mono<PetDto> petDtoMono = serverRequest.bodyToMono(PetDto.class);

        return petDtoMono.flatMap(petDto -> {
                    if (Objects.isNull(petDto.getName())) {
                        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .body(Mono.just("Invalid petDto " + petDto + " entity!"), PetDto.class);
                    }

                    return ServerResponse.status(HttpStatus.OK)
                            .body(petService.alter(petDto), PetDto.class);
                })
                .map(p -> p);
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        String textId = serverRequest.pathVariable("id");
        Long id = RequestUtility.convertTextToNumber(textId);

        if (id == null) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .body(Mono.just("Id [" + textId + "] is not a number!"), String.class);
        }

        return ServerResponse.ok()
                .body(petService.deleteById(id), PetDto.class);
    }

    public Mono<ServerResponse> createAll(ServerRequest serverRequest) {
        Flux<PetDto> petDtoFlux = serverRequest.bodyToFlux(PetDto.class);

        return petDtoFlux
                .collectList()
                .flatMap(petDtos -> {
                    if (Objects.isNull(petDtos) || petDtos.isEmpty()) {
                        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .body(Mono.just("Invalid petDtos " + petDtos + " entities!"), List.class);
                    }

                    return ServerResponse.status(HttpStatus.CREATED)
                            .body(petService.createAll(petDtos), List.class);
                })
                .map(p -> p);
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<PetDto> petDtoMono = serverRequest.bodyToMono(PetDto.class);

        return petDtoMono.flatMap(petDto -> {
                    if (Objects.isNull(petDto.getName())) {
                        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .body(Mono.just("Invalid petDto " + petDto + " entity!"), PetDto.class);
                    }

                    return ServerResponse.status(HttpStatus.CREATED)
                            .body(petService.create(petDto), PetDto.class);
                })
                .map(p -> p);
    }


    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(petService.getAll(), List.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        String textId = serverRequest.pathVariable("id");
        Long id = RequestUtility.convertTextToNumber(textId);

        if (id == null) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .body(Mono.just("Id [" + textId + "] is not a number!"), String.class);
        }

        return ServerResponse.ok()
                .body(petService.getById(id), PetDto.class);
    }

    public Mono<ServerResponse> getRandom(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(petService.getRandom(), PetDto.class);
    }

    public Mono<ServerResponse> getByIds(ServerRequest serverRequest) {
        List<String> textIds = serverRequest.queryParams().get("id");
        List<Long> ids = RequestUtility.covertTextsToNumbers(textIds);

        if (ids.isEmpty()) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .body(Mono.just("Ids " + textIds + " is are not a numbers!"), String.class);
        }

        return ServerResponse.ok()
                .body(petService.getByIds(ids), List.class);
    }

}
