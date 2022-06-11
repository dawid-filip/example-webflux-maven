package com.df.repository;

import com.df.dto.OwnerDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OwnerDtoRepository extends R2dbcRepository<OwnerDto, Long> {

    @Query(SqlQuery.SELECT_JOIN_AS_OWNER_DTO)
    Flux<OwnerDto> getAll();

}
