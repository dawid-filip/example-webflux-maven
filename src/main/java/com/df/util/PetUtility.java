package com.df.util;

import com.df.dto.PetDto;
import com.df.entity.Pet;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
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
        Pet pet = new Pet();
        BeanUtils.copyProperties(petdb, pet);
        BeanUtils.copyProperties(petDto, pet);
        return pet;
    }

}
