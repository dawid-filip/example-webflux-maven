package com.df.util;

import com.df.dto.PetDto;
import com.df.entity.Pet;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PetUtility {

    public static List<Pet> petDtosToPets(List<PetDto> petDtos) {
        return petDtos!=null
                ? petDtos.stream()
                    .map(PetUtility::petDtoToPet)
                    .collect(Collectors.toList())
                : Collections.emptyList();
    }
    public static List<Long> petDtosToPetIds(List<PetDto> petDtos) {
        return petDtos!=null
                ? petDtos.stream()
                    .map(PetDto::getId)
                    .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public static Pet petDtoToPet(PetDto petDto) {
        return new Pet(petDto);
    }

    public static PetDto petToPetDto(Pet Pet) {
        return new PetDto(Pet);
    }
}
