package com.df.util;

import com.df.columns.OwnerColumn;
import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.entity.Owner;
import com.df.request.OwnerRequest;
import io.r2dbc.spi.Row;
import org.springframework.beans.BeanUtils;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OwnerUtility {

    public static OwnerDto ownerToOwnerDto(Owner owner, List<PetDto> petDtos) {
        OwnerDto ownerDto = OwnerUtility.ownerToOwnerDto(owner);
        ownerDto.setPets(petDtos);
        return ownerDto;
    }

    public static Owner ownerRequestToOwner(OwnerRequest ownerRequest) {
        Owner owner = new Owner();
        BeanUtils.copyProperties(ownerRequest, owner);
        owner.setPetIds(ownerRequest.getPetIds()!=null ? ownerRequest.getPetIds() : List.of());
        return owner;
    }


    public static Owner ownerDtoToOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        BeanUtils.copyProperties(ownerDto, owner);
        owner.setPetIds(getPetIds(ownerDto));
        return owner;
    }

    public static OwnerDto ownerToOwnerDto(Owner owner) {
        OwnerDto ownerDto = new OwnerDto();
        BeanUtils.copyProperties(owner, ownerDto);
        ownerDto.setPets(getPetDtos(owner));
        return ownerDto;
    }

    public static OwnerDto ownerRequestOwnerDto(OwnerRequest ownerRequest) {
        OwnerDto ownerDto = new OwnerDto();
        BeanUtils.copyProperties(ownerRequest, ownerDto);
        ownerDto.setPets(getPetDtos(ownerRequest));
        return ownerDto;
    }

    public static OwnerDto ownerDtoToOwnerDto(OwnerDto ownerDto) {
        OwnerDto ownerDtoResult = new OwnerDto();
        BeanUtils.copyProperties(ownerDto, ownerDtoResult);
        ownerDto.setPets(getPetDtos(ownerDto));
        return ownerDtoResult;
    }

    public static OwnerRequest ownerToOwnerRequest(Owner owner) {
        OwnerRequest ownerRequest = new OwnerRequest();
        BeanUtils.copyProperties(owner, ownerRequest);
        ownerRequest.setPetIds(owner.getPetIds()!=null ? owner.getPetIds() : List.of());
        return ownerRequest;
    }

    public static OwnerRequest ownerDtoToOwnerRequest(OwnerDto ownerDto) {
        OwnerRequest ownerRequest = new OwnerRequest();
        BeanUtils.copyProperties(ownerDto, ownerRequest);
        ownerRequest.setPetIds(getPetIds(ownerDto));
        return ownerRequest;
    }


    private static List<Long> getPetIds(OwnerDto ownerDto) {
        if (ownerDto==null || ownerDto.getPets()==null) return List.of();
        return ownerDto.getPets().stream()
                .filter(p -> p.getId()!=null)
                .map(PetDto::getId)
                .collect(Collectors.toList());
    }
    private static List<PetDto> getPetDtos(Owner owner) {
        if (owner==null || owner.getPetIds()==null) return List.of();
        return owner.getPetIds().stream()
                .map(PetUtility::petIdToPetDto)
                .collect(Collectors.toList());
    }
    private static List<PetDto> getPetDtos(OwnerDto ownerDto) {
        if (ownerDto==null || ownerDto.getPets()==null) return List.of();
        return ownerDto.getPets();
    }
    private static List<PetDto> getPetDtos(OwnerRequest ownerRequest) {
        if (ownerRequest==null || ownerRequest.getPetIds()==null) return List.of();
        return ownerRequest.getPetIds().stream()
                .map(PetUtility::petIdToPetDto)
                .collect(Collectors.toList());
    }

    public static OwnerDto rowToOwnerDto(Row source, @Nullable List<PetDto> petDtos) {
        return OwnerDto.builder()
                .id(source.get(OwnerColumn.ID.toString(), Long.class))
                .firstName(source.get(OwnerColumn.FIRST_NAME.toString(), String.class))
                .lastName(source.get(OwnerColumn.LAST_NAME.toString(), String.class))
                .age(source.get(OwnerColumn.AGE.toString(), Short.class))
                .pets(Optional.of(petDtos).orElse(List.of()))
                .createdAt(source.get(OwnerColumn.CREATED_AT.toString(), LocalDateTime.class))
                .createdBy(source.get(OwnerColumn.CREATED_BY.toString(), String.class))
                .updatedAt(source.get(OwnerColumn.UPDATED_AT.toString(), LocalDateTime.class))
                .updatedBy(source.get(OwnerColumn.UPDATED_BY.toString(), String.class))
                .version(source.get(OwnerColumn.VERSION.toString(), Long.class))
                .build();
    }
}
