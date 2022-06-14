package com.df.repository;

import com.df.entity.Audit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface AuditRepository extends ReactiveCrudRepository<Audit, Long> {

    @Query(SqlQuery.SELECT_AUDIT_BETWEEN_IDS)
    Flux<Audit> findBetweenIds(Long startId, Long endId);

    @Query(SqlQuery.SELECT_AUDIT_LIKE_ENTITY_CLASS)
    Flux<Audit> findLikeEntityClass(String entityClass);

    @Query(SqlQuery.SELECT_AUDIT_BETWEEN_AUDITED_ONS)
    Flux<Audit> findBetweenAuditedOns(LocalDateTime startAuditedOn, LocalDateTime endAuditedOn);

}
