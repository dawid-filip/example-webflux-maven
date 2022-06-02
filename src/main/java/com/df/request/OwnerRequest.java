package com.df.request;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.entity.Owner;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class OwnerRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private Short age;
    private List<Long> petIds;

    public OwnerRequest(Owner owner) {
        BeanUtils.copyProperties(owner, this);
    }

    public OwnerRequest(OwnerDto ownerDto) {
        BeanUtils.copyProperties(ownerDto, this);

        if (ownerDto!=null && ownerDto.getPets()!=null) {
            this.petIds = ownerDto.getPets().stream()
                    .map(PetDto::getId)
                    .collect(Collectors.toList());
        }
    }
}
