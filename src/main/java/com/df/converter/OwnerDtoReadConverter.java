package com.df.converter;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;
import java.util.Optional;

@ReadingConverter
public class OwnerDtoReadConverter implements Converter<Row, OwnerDto> {

    @Override
    public OwnerDto convert(Row source) {

        List<PetDto> petDtos = List.of(
                PetDto.builder()
                        .id(Optional.of(source.get("pet_id", Long.class)).orElse(0L))
                        .name(source.get("pet_name", String.class))
                        .age(source.get("pet_age", Short.class))
                        .weight(source.get("pet_weight", Short.class))
                        .length(source.get("pet_length", Short.class))
                        .build()
        );

        return OwnerDto.builder()
                .id(Optional.of(source.get("owner_id", Long.class)).orElse(0L))
                .firstName(source.get("owner_first_name", String.class))
                .lastName(source.get("owner_last_name", String.class))
                .age(source.get("owner_age", Short.class))
                .pets(petDtos)
                .build();
    }

}
