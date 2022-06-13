package com.df.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class PetRoute {

    private final PetHandler petHandler;

    private static final String PET_URL = "/api/v2/pet";

    @Bean
    public RouterFunction<ServerResponse> petRouteV2() {
        RouterFunction<ServerResponse> petRoute = RouterFunctions
                .nest(path(PET_URL),
                        route(GET("/")
                                        .and(method(GET)), petHandler::getAll)
                                .andRoute(GET("/{id}")
                                        .and(method(GET)), petHandler::getById)
                                .andRoute(GET("/many/ids")
                                        .and(queryParam("id", id -> true))
                                        .and(method(GET)), petHandler::getByIds)
                                .andRoute(POST("/")
                                        .and(accept(APPLICATION_JSON))
                                        .and(contentType(APPLICATION_JSON))
                                        .and(method(POST)), petHandler::create)
                                .andRoute(POST("/many")
                                        .and(accept(APPLICATION_JSON))
                                        .and(contentType(APPLICATION_JSON))
                                        .and(method(POST)), petHandler::createAll)
                                .andRoute(DELETE("/{id}")
                                        .and(method(DELETE)), petHandler::deleteById)
                                .andRoute(PATCH("/")
                                        .and(accept(APPLICATION_JSON))
                                        .and(contentType(APPLICATION_JSON))
                                        .and(method(PATCH)), petHandler::alter)
                                .andRoute(PATCH("/many")
                                        .and(accept(APPLICATION_JSON))
                                        .and(contentType(APPLICATION_JSON))
                                        .and(method(PATCH)), petHandler::alterAll)
                );
        return petRoute;
    }

}
