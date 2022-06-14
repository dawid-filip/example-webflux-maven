package com.df.rest;

import com.df.entity.Audit;
import com.df.service.AuditService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.df.util.RequestUtility.getBasicResponseEntity;

@RestController
@RequestMapping("/api/v1/audit")
@AllArgsConstructor
@Log4j2
public class AuditRestController {

    final private AuditService auditService;

    @GetMapping
    public ResponseEntity<Flux<Audit>> getAll() {
        return getBasicResponseEntity(auditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<Audit>> getById(@PathVariable("id") Long id) {
        return getBasicResponseEntity(auditService.getById(id));
    }

    @GetMapping("/between-ids")
    public ResponseEntity<Flux<Audit>> getBetweenIds(@RequestParam("startId") Long startId,
                                                     @RequestParam("endId") Long endId) {
        return getBasicResponseEntity(auditService.getBetweenIds(startId, endId));
    }

    @GetMapping("/like")
    public ResponseEntity<Flux<Audit>> getLikeEntityClass(@RequestParam("entityClass") String entityClass) {
        return getBasicResponseEntity(auditService.getLikeEntityClass(entityClass));
    }

    // example: http://localhost:8080/api/v1/audit/between-audited-ons?startAuditedOn=2022-01-01T00:00:01.1&endAuditedOn=2022-01-01T00:00:02.1
    @GetMapping("/between-audited-ons")
    public ResponseEntity<Flux<Audit>> getBetweenAuditedOns(@RequestParam("startAuditedOn") LocalDateTime startAuditedOn,
                                                            @RequestParam("endAuditedOn") LocalDateTime endAuditedOn) {
        return getBasicResponseEntity(auditService.getBetweenAuditedOns(startAuditedOn, endAuditedOn));
    }

}
