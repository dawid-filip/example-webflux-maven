package com.df.startup;

import com.df.dto.PetDto;
import com.df.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PetStartUp {

    final private PetService petService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        petService.create(createPet("petName 1", 1, 1, 10)).subscribe();
        petService.create(createPet("petName 2", 2, 3, 11)).subscribe();
        petService.create(createPet("petName 3", 3, 5, 13)).subscribe();
        petService.create(createPet("petName 4", 4, 6, 15)).subscribe();
        petService.create(createPet("petName 5", 5, 7, 17)).subscribe();
    }
    private PetDto createPet(String petName, int age, int weight, int length) {
       return new PetDto(null, petName, (short)age, (short)weight, (short)length);
    }

}
