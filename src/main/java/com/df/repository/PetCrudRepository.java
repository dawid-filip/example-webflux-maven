package com.df.repository;

import com.df.entity.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetCrudRepository extends ReactiveCrudRepository<Pet, Long> {
}
