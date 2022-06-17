package com.df.startup;

import com.df.dto.OwnerDto;
import com.df.service.OwnerDtoService;
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
public class OwnerDtoServiceOnStartup {

    private final OwnerDtoService ownerDtoService;

    @EventListener(ContextRefreshedEvent.class)
    public void doOnContextRefreshedEvent() {
        startGetAll();
    }

    private void startGetAll() {
        ownerDtoService.getAll()
                .collectList()
                .map(ownerMapper::apply)
                .map(resultOwnerRequest -> printOwnerMapper.apply("\nOwnerDtoServiceOnStartup::startGetAll()=" + resultOwnerRequest))
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