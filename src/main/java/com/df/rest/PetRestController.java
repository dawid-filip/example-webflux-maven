package com.df.rest;

import com.df.dto.PetDto;
import com.df.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                .body(petService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<PetDto>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(petService.getById(id));
    }

}
