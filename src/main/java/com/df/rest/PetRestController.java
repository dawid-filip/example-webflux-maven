package com.df.rest;

import com.df.dto.PetDto;
import com.df.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/pet")
@AllArgsConstructor
public class PetRestController {

    final private PetService petService;

    @GetMapping
    public ResponseEntity<Flux<PetDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(petService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<PetDto>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(petService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Mono<PetDto>> create(@RequestBody PetDto petDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(petService.create(petDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<PetDto>> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(HttpMethod.DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(petService.deleteById(id));
    }

    @PatchMapping
    public ResponseEntity<Mono<PetDto>> alter(@RequestBody PetDto petDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(HttpMethod.PATCH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(petService.alter(petDto));
    }

}
