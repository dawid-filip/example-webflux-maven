package com.df.rest;

import com.df.entity.Owner;
import com.df.request.OwnerRequest;
import com.df.service.OwnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/owner")
@AllArgsConstructor
public class OwnerRestController {

    final private OwnerService ownerService;

    @GetMapping
    public ResponseEntity<Flux<Owner>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(ownerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Owner>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(ownerService.getById(id));
    }

    @GetMapping("/random")
    public ResponseEntity<Mono<Owner>> getRandom() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(ownerService.getRandom());
    }

    @PostMapping
    public ResponseEntity<Mono<Owner>> create(@RequestBody OwnerRequest ownerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ownerService.create(ownerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Owner>> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(HttpMethod.DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ownerService.deleteById(id));
    }

    @PatchMapping
    public ResponseEntity<Mono<Owner>> alter(@RequestBody OwnerRequest ownerRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(HttpMethod.PATCH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ownerService.alter(ownerRequest));
    }

}
