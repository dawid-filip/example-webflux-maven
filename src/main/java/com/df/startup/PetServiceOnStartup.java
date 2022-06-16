package com.df.startup;

import com.df.dto.PetDto;
import com.df.service.PetService;
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
public class PetServiceOnStartup {

    private final PetService petService;

    @EventListener(ContextRefreshedEvent.class)
    public void doOnContextRefreshedEvent() {
        startCreate();
        startCreateAll();
        startAlter();
        startAlterAll();
        startGetAll();
        startGetById(1L);
        startDeleteById(7L);
        startDeleteByIds(List.of(8L, 9L));
        startGetByIds(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));
    }

    public void startDeleteById(Long id) {
        petService.deleteById(id)
                .map(petDto -> printPetMapper.apply("\nPetServiceOnStartup::startDeleteById(" + id + ")=" + petDto))
                .subscribe();
    }

    public void startDeleteByIds(List<Long> ids) {
        petService.deleteByIds(ids)
                .collectList()
                .map(petMapper::apply)
                .map(petDto -> printPetMapper.apply("\nPetServiceOnStartup::startDeleteByIds(" + ids + ")=" + petDto))
                .subscribe();
    }

    public void startAlter() {
        PetDto petDto = PetDto.builder().id(1L).name("petNameAlter1")
                .age((short)4).weight((short)5).length((short)20).build();

        petService.alter(petDto)
                .map(resultPetDto -> printPetMapper.apply("\nPetServiceOnStartup::startAlter()=" + resultPetDto))
                .subscribe();
    }

    public void startAlterAll() {
        PetDto petDto1 = PetDto.builder().id(2L).name("petNameAlterAll2")
                .age((short)4).weight((short)5).length((short)21).build();
        PetDto petDto2 = PetDto.builder().id(3L).name("petNameAlterAll3")
                .age((short)6).weight((short)7).length((short)22).build();

        petService.alterAll(List.of(petDto1, petDto2))
                .collectList()
                .map(petMapper::apply)
                .map(resultPetDto -> printPetMapper.apply("\nPetServiceOnStartup::startAlterAll()=" + resultPetDto))
                .subscribe();
    }

    public void startCreate() {
        PetDto petDto = PetDto.builder().id(null).name("petNameCreate7")
                .age((short)4).weight((short)5).length((short)20).build();

        petService.create(petDto)
                .map(resultPetDto -> printPetMapper.apply("\nPetServiceOnStartup::startCreate()=" + resultPetDto))
                .subscribe();
    }

    public void startCreateAll() {
        PetDto petDto1 = PetDto.builder().id(null).name("petNameCreate8")
                .age((short)4).weight((short)5).length((short)21).build();
        PetDto petDto2 = PetDto.builder().id(null).name("petNameCreate9")
                .age((short)6).weight((short)7).length((short)22).build();

        petService.createAll(List.of(petDto1, petDto2))
                .collectList()
                .map(petMapper::apply)
                .map(resultPetDto -> printPetMapper.apply("\nPetServiceOnStartup::startCreateAll()=" + resultPetDto))
                .subscribe();
    }

    public void startGetById(Long id) {
        petService.getById(id)
                .map(petDto -> printPetMapper.apply("\nPetServiceOnStartup::startGetAll(" + id + ")=" + petDto))
                .subscribe();
    }

    public void startGetByIds(List<Long> ids) {
        petService.getByIds(ids)
                .collectList()
                .map(petMapper::apply)
                .map(petDto -> printPetMapper.apply("\nPetServiceOnStartup::startGetByIds(" + ids + ")=" + petDto))
                .subscribe();
    }

    public void startGetAll() {
        petService.getAll()
                .collectList()
                .map(petMapper::apply)
                .map(petDto -> printPetMapper.apply("\nPetServiceOnStartup::startGetAll()=" + petDto))
                .subscribe();
    }

    private Function<List<PetDto>, String> petMapper = petDtos ->
                    petDtos.stream()
                        .map(petDto -> petDto.toString())
                        .collect(Collectors.joining("\n", "{\n", "}"));

    private Function<String, String> printPetMapper = text -> {
                        log.info(text);
                        return text;
                    };


}
