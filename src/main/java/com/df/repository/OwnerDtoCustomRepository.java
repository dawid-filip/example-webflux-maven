package com.df.repository;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OwnerDtoCustomRepository {

    private final DatabaseClient databaseClient;

    public Flux<OwnerDto> getAll() {
        return databaseClient.sql(SqlQuery.SELECT_JOIN_AS_OWNER_DTO)
                .fetch()
                .all()
                .bufferUntilChanged(row -> row.get("owner_id"))
                .map(rows -> OwnerDto.builder()
                        .id((Long) rows.get(0).get("owner_id"))
                        .firstName(String.valueOf(rows.get(0).get("owner_first_name")))
                        .lastName(String.valueOf(rows.get(0).get("owner_last_name")))
                        .age((Short) rows.get(0).get("owner_age"))
                        .pets(rows.stream()
                                .map(subRow -> PetDto.builder()
                                        .id((Long) subRow.get("pet_id"))
                                        .name((String) subRow.get("pet_name"))
                                        .age((Short) subRow.get("pet_age"))
                                        .weight((Short) subRow.get("pet_weight"))
                                        .length((Short) subRow.get("pet_length"))
                                        .build()
                                )
                                .collect(Collectors.toList())
                        )
                        .build()
                )
                .map(p -> p);
    }

}
