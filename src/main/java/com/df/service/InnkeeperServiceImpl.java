package com.df.service;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class InnkeeperServiceImpl implements InnkeeperService {

    private final OwnerService ownerService;
    private final PetService petService;

    @Override
    public Flux<OwnerDto> getAll() {
        return Mono.zip(ownerService.getAll().collectList(), petService.getAll().collectList())
                .map(tuple -> {
                    List<Owner> owners = tuple.getT1();
                    List<PetDto> petDtos = tuple.getT2();

                    return owners.stream().map(owner ->
                                    new OwnerDto(owner, getPetDtosByIds(owner.getPetIds(), petDtos)))
                            .collect(Collectors.toList());
                })
                .flatMapMany(Flux::fromIterable)
                .map(o -> o);
    }

    @Override
    public Mono<OwnerDto> getById(Long id) {
        return ownerService.getById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(owner ->
                        petService.getByIds(owner.getPetIds())
                                .collectList()
                                .map(petDtos ->
                                        new OwnerDto(owner, getPetDtosByIds(owner.getPetIds(), petDtos))
                                )
                                .map(p -> p)
                )
                .map(o -> o);
    }

    private List<PetDto> getPetDtosByIds(List<Long> petIds, List<PetDto> petDtos) {
        if (petIds == null || petDtos == null) return null;

        return petDtos.stream()
                .filter(petDto -> petIds.contains(petDto.getId()))
                .collect(Collectors.toList());
    }

}
