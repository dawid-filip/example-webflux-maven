package com.df.service;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.repository.OwnerDtoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerDtoServiceImpl implements OwnerDtoService {

    private final OwnerDtoRepository ownerDtoRepository;

    @Override
    public Flux<OwnerDto> getAll() {
        return ownerDtoRepository
                .getAll()
                .switchIfEmpty(Flux.empty())
                .collectList()
                .map(ownerDtos -> {
                    List<Long> ownerDtoIds = ownerDtos.stream()
                            .map(OwnerDto::getId)
                            .distinct()
                            .collect(Collectors.toList());
                    List<OwnerDto> linkedOwnerDtos = new ArrayList<>(ownerDtoIds.size());

                    ownerDtoIds.stream()
                            .map(ownerDtoId -> {
                                List<PetDto> extractedPetDtos = extractPetDtos(ownerDtos, ownerDtoId);
                                OwnerDto extractedOwnerDto = extractOwnerDto(ownerDtos, ownerDtoId);
                                extractedOwnerDto.setPets(extractedPetDtos);
                                linkedOwnerDtos.add(extractedOwnerDto);
                                return linkedOwnerDtos;
                            })
                            .collect(Collectors.toList());

                    return linkedOwnerDtos;
                })
                .flatMapMany(Flux::fromIterable)
                .map(o -> o);
    }

    private OwnerDto extractOwnerDto(List<OwnerDto> ownerDtos, Long ownerId) {
        return ownerDtos.stream()
                .filter(ownerDto -> ownerDto.getId() == ownerId)
                .findFirst()
                .get();
    }

    private List<PetDto> extractPetDtos(List<OwnerDto> ownerDtos, Long ownerId) {
        return ownerDtos.stream()
                .filter(ownerDto -> ownerDto.getId() == ownerId)
                .map(OwnerDto::getPets)
                .filter(petDtos -> Objects.nonNull(petDtos))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


}
