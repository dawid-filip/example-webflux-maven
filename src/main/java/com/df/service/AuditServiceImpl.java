package com.df.service;

import com.df.entity.Audit;
import com.df.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    public Mono<?> create(Object entityValue) {
        Audit audit = Audit.builder()
                .id(null)
                .entityClass(entityValue.getClass().getName())
                .entityValue(entityValue.toString())
                .auditedOn(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return auditRepository.save(audit)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(createdAuditEntity -> log.info("Created " + createdAuditEntity + " audit."))
                .doOnError(e -> log.info("Failed to create audit " + entityValue + ".", e));
    }

}
