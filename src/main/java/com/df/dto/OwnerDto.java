package com.df.dto;

import com.df.entity.Owner;
import com.df.entity.Pet;
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

    private List<Pet> pets;

    public OwnerDto(Owner owner) {
        BeanUtils.copyProperties(owner, this);
    }

    public OwnerDto(OwnerRequest ownerRequest) {
        BeanUtils.copyProperties(ownerRequest, this);
    }
}
