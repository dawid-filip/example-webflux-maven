package com.df.dto;

import com.df.entity.Pet;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PetDto {
    private Long id;
    private String name;
    private Short age;
    private Short weight;
    private Short length;

    public PetDto(Pet pet) {
        BeanUtils.copyProperties(pet, this);
    }

}
