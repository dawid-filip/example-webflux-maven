package com.df.startup;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.service.InnkeeperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "example.app.startup", name = "enable", matchIfMissing = true)
@Log4j2
public class InnkeeperServiceOnStartup {

    private final InnkeeperService innkeeperService;

    @EventListener(ContextRefreshedEvent.class)
    public void doOnContextRefreshedEvent() {
        startCreate();
//        startAlter();
        startGetAll();
        startGetById(1L);
        startDeleteById(4L);
    }

    private void startCreate() {
        PetDto petDto = PetDto.builder().id(null).name("petNameCreate10")
                .age((short)7).weight((short)7).length((short)27).build();
        OwnerDto ownerDto = OwnerDto.builder().id(null)
                .firstName("firstNameCreate10").lastName("lastNameCreate10").age((short)27).pets(List.of(petDto)).build();

        innkeeperService.create(ownerDto)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nInnkeeperServiceOnStartup::startCreate()=" + resultOwnerRequest))
                .subscribe();
    }

    private void startAlter() {
        PetDto petDto = PetDto.builder().id(7L).name("petNameAlter14")
                .age((short)8).weight((short)8).length((short)28).build();
        OwnerDto ownerDto = OwnerDto.builder().id(4L)
                .firstName("firstNameCreate14").lastName("lastNameAlter14").age((short)24).pets(List.of(petDto)).build();

        innkeeperService.create(ownerDto)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nInnkeeperServiceOnStartup::startAlter()=" + resultOwnerRequest))
                .subscribe();
    }

    private void startGetAll() {
        innkeeperService.getAll()
                .collectList()
                .map(ownerMapper::apply)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nInnkeeperServiceOnStartup::startGetAll()=" + resultOwnerRequest))
                .subscribe();
    }

    private void startGetById(Long id) {
        innkeeperService.getById(id)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nInnkeeperServiceOnStartup::startGetById(" + id + ")=" + resultOwnerRequest))
                .subscribe();
    }

    private void startDeleteById(Long id) {
        innkeeperService.deleteById(id)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nInnkeeperServiceOnStartup::startDeleteById(" + id + ")=" + resultOwnerRequest))
                .subscribe();
    }


    private Function<List<OwnerDto>, String> ownerMapper = petDtos ->
            petDtos.stream()
                    .map(petDto -> petDto.toString())
                    .collect(Collectors.joining("\n", "{\n", "}"));

    private Function<String, String> printOwnerMapper = textLogMessage -> {
        log.info(textLogMessage);
        return textLogMessage;
    };

}
