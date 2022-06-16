package com.df.startup;

import com.df.entity.Owner;
import com.df.request.OwnerRequest;
import com.df.service.OwnerService;
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
public class OwnerServiceOnStartup {

    private final OwnerService ownerService;

    @EventListener(ContextRefreshedEvent.class)
    public void doOnContextRefreshedEvent() {
        startCreate();
//        startAlter();
        startGetAll();
        startGetById(1L);
        startDeleteById(4L);
    }

    private void startCreate() {
        OwnerRequest ownerRequest = OwnerRequest.builder().id(null)
                .firstName("firstNameCreate4").lastName("lastNameCreate4").age((short)24).petIds(List.of(1L)).build();

        ownerService.create(ownerRequest)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nOwnerServiceOnStartup::startCreate()=" + resultOwnerRequest))
                .subscribe();
    }

    private void startAlter() {
        OwnerRequest ownerRequest = OwnerRequest.builder().id(1L)
                .firstName("firstNameAlter1").lastName("lastNameAlter1").age((short)21).petIds(List.of(1L)).build();

        ownerService.alter(ownerRequest)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nOwnerServiceOnStartup::startAlter()=" + resultOwnerRequest))
                .subscribe();
    }

    private void startGetAll() {
        ownerService.getAll()
                .collectList()
                .map(ownerMapper::apply)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nOwnerServiceOnStartup::startGetAll()=" + resultOwnerRequest))
                .subscribe();
    }

    private void startGetById(Long id) {
        ownerService.getById(id)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nOwnerServiceOnStartup::startGetById(" + id + ")=" + resultOwnerRequest))
                .subscribe();
    }

    private void startDeleteById(Long id) {
        ownerService.deleteById(id)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nOwnerServiceOnStartup::startDeleteById(" + id + ")=" + resultOwnerRequest))
                .subscribe();
    }

    private Function<List<Owner>, String> ownerMapper = petDtos ->
            petDtos.stream()
                    .map(petDto -> petDto.toString())
                    .collect(Collectors.joining("\n", "{\n", "}"));

    private Function<String, String> printOwnerMapper = textLogMessage -> {
        log.info(textLogMessage);
        return textLogMessage;
    };

}
