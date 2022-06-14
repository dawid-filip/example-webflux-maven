package com.df.rest;

import com.df.entity.Audit;
import com.df.service.AuditService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/audit")
@AllArgsConstructor
@Log4j2
public class AuditRestController {

    final private AuditService auditService;

    @GetMapping
    public ResponseEntity<Flux<Audit>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(auditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Audit>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(auditService.getById(id));
    }

    @GetMapping("/between-ids")
    public ResponseEntity<Flux<Audit>> getBetweenIds(@RequestParam("startId") Long startId,
                                                     @RequestParam("endId") Long endId) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(auditService.getBetweenIds(startId, endId));
    }

    @GetMapping("/like")
    public ResponseEntity<Flux<Audit>> getLikeEntityClass(@RequestParam("entityClass") String entityClass) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(auditService.getLikeEntityClass(entityClass));
    }

    @GetMapping("/between-audited-ons")
    public ResponseEntity<Flux<Audit>> getBetweenAuditedOns(@RequestParam("startAuditedOn") String startAuditedOn,
                                                  @RequestParam("endAuditedOn") String endAuditedOn) {
        LocalDateTime ldtStartAuditedOn = LocalDateTime.parse(startAuditedOn);
        LocalDateTime ldtEndAuditedOn = LocalDateTime.parse(endAuditedOn);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .allow(HttpMethod.GET)
                .body(auditService.getBetweenAuditedOns(ldtStartAuditedOn, ldtEndAuditedOn));
    }

}
