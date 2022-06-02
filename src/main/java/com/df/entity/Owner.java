package com.df.entity;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.request.OwnerRequest;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.stream.Collectors;

@Table(name = "owner")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Owner {

    @Id
    @Column("id")
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("age")
    private Short age;

    @Column("id_pet")
    private List<Long> petIds;

    public Owner(OwnerDto ownerDto) {
        BeanUtils.copyProperties(ownerDto, this);

        if (ownerDto!=null && ownerDto.getPets()!=null) {
            this.petIds = ownerDto.getPets().stream()
                    .map(PetDto::getId)
                    .collect(Collectors.toList());
        }
    }
    public Owner(OwnerRequest ownerRequest) {
        BeanUtils.copyProperties(ownerRequest, this);
    }

    public Owner(OwnerDto ownerDto, List<PetDto> petDto) {
        BeanUtils.copyProperties(ownerDto, this);

        if (petDto!=null) {
            this.petIds = petDto.stream()
                    .map(PetDto::getId)
                    .collect(Collectors.toList());
        }
    }
}
