package com.df.util;

import com.df.columns.PetColumns;
import com.df.dto.PetDto;
import com.df.entity.Pet;
import io.r2dbc.spi.Row;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PetUtility {

    public static List<Pet> petDtosToPets(List<PetDto> petDtos) {
        return petDtos != null && !petDtos.isEmpty()
                ? petDtos.stream()
                .map(PetUtility::petDtoToPet)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public static List<Long> petDtosToPetIds(List<PetDto> petDtos) {
        return petDtos != null && !petDtos.isEmpty()
                ? petDtos.stream()
                .map(PetDto::getId)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public static Pet petDtoToPet(PetDto petDto) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDto, pet);
        pet.setId(petDto.getId());
        return pet;
    }

    public static PetDto petToPetDto(Pet pet) {
        PetDto petDto = new PetDto();
        BeanUtils.copyProperties(pet, petDto);
        petDto.setId(pet.getId());
        return petDto;
    }

    public static PetDto petDtoToPetDto(PetDto PetDto) {
        PetDto petDtoResult = new PetDto();
        BeanUtils.copyProperties(PetDto, petDtoResult);
        return petDtoResult;
    }

    public static PetDto petIdToPetDto(Long petId) {
        PetDto petDto = new PetDto();
        petDto.setId(petId);
        return petDto;
    }

    public static Pet preparePetFromPetAndPetDto(Pet petdb, PetDto petDto) {
        petdb.setName(petDto.getName());
        petdb.setAge(petDto.getAge());
        petdb.setLength(petDto.getLength());
        petdb.setWeight(petDto.getWeight());
        return petdb;
    }

    public static PetDto getNullWhenAllPetDtoFieldsNull(PetDto petDto) {
        if (Objects.isNull(petDto.getId()) && Objects.isNull(petDto.getName()) &&
                Objects.isNull(petDto.getAge()) && Objects.isNull(petDto.getWeight()) &&
                Objects.isNull(petDto.getLength()) && Objects.isNull(petDto.getVersion()) &&
                Objects.isNull(petDto.getCreatedAt()) && Objects.isNull(petDto.getCreatedBy()) &&
                Objects.isNull(petDto.getUpdatedAt()) && Objects.isNull(petDto.getUpdatedBy())) {
            return null;
        }
        return petDto;
    }

    public static PetDto rowToPetDto(Map<String, Object> row) {
        return PetDto.builder()
                .id((Long) row.get(PetColumns.ID.toString()))
                .name((String) row.get(PetColumns.NAME.toString()))
                .age((Short) row.get(PetColumns.AGE.toString()))
                .weight((Short) row.get(PetColumns.WEIGHT.toString()))
                .length((Short) row.get(PetColumns.LENGTH.toString()))
                .createdAt((LocalDateTime) row.get((PetColumns.CREATED_AT.toString())))
                .createdBy((String) row.get((PetColumns.CREATED_BY.toString())))
                .updatedAt((LocalDateTime) row.get((PetColumns.UPDATED_AT.toString())))
                .updatedBy((String) row.get((PetColumns.UPDATED_BY.toString())))
                .version((Long) row.get((PetColumns.VERSION.toString())))
                .build();
    }

    public static PetDto rowToPetDto(Row source) {
        return PetDto.builder()
                .id(source.get(PetColumns.ID.toString(), Long.class))
                .name(source.get(PetColumns.NAME.toString(), String.class))
                .age(source.get(PetColumns.AGE.toString(), Short.class))
                .weight(source.get(PetColumns.WEIGHT.toString(), Short.class))
                .length(source.get(PetColumns.LENGTH.toString(), Short.class))
                .createdAt(source.get(PetColumns.CREATED_AT.toString(), LocalDateTime.class))
                .createdBy(source.get(PetColumns.CREATED_BY.toString(), String.class))
                .updatedAt(source.get(PetColumns.UPDATED_AT.toString(), LocalDateTime.class))
                .updatedBy(source.get(PetColumns.UPDATED_BY.toString(), String.class))
                .version(source.get(PetColumns.VERSION.toString(), Long.class))
                .build();
    }


}
