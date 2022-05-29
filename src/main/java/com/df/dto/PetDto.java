package com.df.dto;

import com.df.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
