package com.df.converter;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.util.OwnerUtility;
import com.df.util.PetUtility;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;

@ReadingConverter
public class OwnerDtoReadConverter implements Converter<Row, OwnerDto> {

    @Override
    public OwnerDto convert(Row source) {

        PetDto petDto = PetUtility.rowToPetDto(source);

        petDto = PetUtility.getNullWhenAllPetDtoFieldsNull(petDto);
        List<PetDto> petDtos = petDto!=null ? List.of(petDto) : List.of();

        return OwnerUtility.rowToOwnerDto(source, petDtos);
    }

}
