package com.df.repository;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.util.PetUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OwnerDtoCustomRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator transactionalOperator;

    public Flux<OwnerDto> getAll() {
        return databaseClient.sql(SqlQuery.SELECT_JOIN_AS_OWNER_DTO)
                .fetch()
                .all()
                .bufferUntilChanged(row -> row.get("owner_id"))
                .map(rows -> {
                    OwnerDto ownerDto = OwnerDto.builder()
                            .id((Long) rows.get(0).get("owner_id"))
                            .firstName(String.valueOf(rows.get(0).get("owner_first_name")))
                            .lastName(String.valueOf(rows.get(0).get("owner_last_name")))
                            .age((Short) rows.get(0).get("owner_age"))
                            .createdAt((LocalDateTime) rows.get(0).get("owner_created_at"))
                            .createdBy((String) rows.get(0).get("owner_created_by"))
                            .updatedAt((LocalDateTime) rows.get(0).get("owner_updated_at"))
                            .updatedBy((String) rows.get(0).get("owner_updated_by"))
                            .version((Long) rows.get(0).get("owner_version"))
                            .pets(rows.stream()
                                    .map(PetUtility::rowToPetDto)
                                    .collect(Collectors.toList())
                            )
                            .build();
                     return ownerDto;
                })
                .map(p -> p);
    }

    public Flux<PetDto> getTransactionalAllPetDtos() {
        Flux<PetDto> petDtos = databaseClient.sql(SqlQuery.SELECT_PET)
                .fetch()
                .all()
                .map(PetUtility::rowToPetDto)
                .map(p -> p);
        return this.transactionalOperator.transactional(petDtos);
    }

}
