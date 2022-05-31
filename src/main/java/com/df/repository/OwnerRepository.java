package com.df.repository;

import com.df.entity.Owner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends ReactiveCrudRepository<Owner, Long> {
}
