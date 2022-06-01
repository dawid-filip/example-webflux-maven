package com.df.rest;

import com.df.dto.OwnerDto;
import com.df.entity.Owner;
import com.df.request.OwnerRequest;
import com.df.service.InnkeeperService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/innkeeper")
@AllArgsConstructor
public class InnkeeperRestController {

    final private InnkeeperService innkeeperService;

    @GetMapping
    public ResponseEntity<Flux<OwnerDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(innkeeperService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<OwnerDto>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(innkeeperService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Mono<OwnerDto>> create(@RequestBody OwnerDto ownerDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(innkeeperService.create(ownerDto));
    }

}
