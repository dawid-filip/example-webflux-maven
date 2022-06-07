package com.df.repository;

import com.df.dto.OwnerDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OwnerDtoRepository extends R2dbcRepository<OwnerDto, Long> {

    @Query("SELECT owner.id as owner_id, owner.first_name as owner_first_name, owner.last_name as owner_last_name, owner.age as owner_age, owner.id_pet as owner_id_pet, " +
            "pet.id as pet_id, pet.name as pet_name, pet.age as pet_age, pet.weight as pet_weight, pet.length as pet_length " +
            "FROM owner LEFT JOIN pet ON ARRAY_CONTAINS(owner.id_pet, pet.id); ")
    Flux<OwnerDto> getAll();

}
