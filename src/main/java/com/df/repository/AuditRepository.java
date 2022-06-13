package com.df.repository;

import com.df.entity.Audit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuditRepository extends ReactiveCrudRepository<Audit, Long> {

    @Query(SqlQuery.SELECT_AUDIT_BETWEEN)
    Flux<Audit> findBetweenIds(Long startId, Long endId);

    @Query(SqlQuery.SELECT_AUDIT_LIKE_ENTITY_CLASS)
    Flux<Audit> findLikeEntityClass(String entityClass);

}
