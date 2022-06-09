package com.df.converter;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.util.PetUtility;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;
import java.util.List;

@ReadingConverter
public class OwnerDtoReadConverter implements Converter<Row, OwnerDto> {

    @Override
    public OwnerDto convert(Row source) {

        PetDto petDto = PetDto.builder()
                .id(source.get("pet_id", Long.class))
                .name(source.get("pet_name", String.class))
                .age(source.get("pet_age", Short.class))
                .weight(source.get("pet_weight", Short.class))
                .length(source.get("pet_length", Short.class))
                .createdAt(source.get("pet_created_at", LocalDateTime.class))
                .createdBy(source.get("pet_created_by", String.class))
                .updatedAt(source.get("pet_updated_at", LocalDateTime.class))
                .updatedBy(source.get("pet_updated_by", String.class))
                .version(source.get("pet_version", Long.class))
                .build();
        petDto = PetUtility.getNullWhenAllPetDtoFieldsNull(petDto);
        List<PetDto> petDtos = petDto!=null ? List.of(petDto) : List.of();

        return OwnerDto.builder()
                .id(source.get("owner_id", Long.class))
                .firstName(source.get("owner_first_name", String.class))
                .lastName(source.get("owner_last_name", String.class))
                .age(source.get("owner_age", Short.class))
                .pets(petDtos)
                .createdAt(source.get("owner_created_at", LocalDateTime.class))
                .createdBy(source.get("owner_created_by", String.class))
                .updatedAt(source.get("owner_updated_at", LocalDateTime.class))
                .updatedBy(source.get("owner_updated_by", String.class))
                .version(source.get("owner_version", Long.class))
                .build();
    }

}
