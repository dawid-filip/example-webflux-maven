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
                    .map(petDto -> new Pet(petDto))
                    .collect(Collectors.toList())
                : Collections.emptyList();
    }

}
