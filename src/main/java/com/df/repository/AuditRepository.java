package com.df.repository;

import com.df.entity.Audit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends ReactiveCrudRepository<Audit, Long> {
}
