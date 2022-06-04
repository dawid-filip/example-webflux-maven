package com.df.dto;

import com.df.entity.Owner;
import com.df.request.OwnerRequest;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "pets")
@ToString
public class OwnerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Short age;
    private List<PetDto> pets;

    public OwnerDto(Owner owner) {
        BeanUtils.copyProperties(owner, this);
    }
    public OwnerDto(Owner owner, List<PetDto> petDtos) {
        BeanUtils.copyProperties(owner, this);
        this.setPets(petDtos);
    }

    public OwnerDto(OwnerRequest ownerRequest) {
        BeanUtils.copyProperties(ownerRequest, this);
    }

    public OwnerDto(OwnerDto ownerDto) {
        BeanUtils.copyProperties(ownerDto, this);
    }
}
