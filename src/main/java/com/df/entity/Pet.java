package com.df.entity;

import com.df.dto.PetDto;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "pet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Pet {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("age")
    private Short age;

    @Column("weight")
    private Short weight;

    @Column("length")
    private Short length;

    public Pet(PetDto petDto) {
        BeanUtils.copyProperties(petDto, this);
    }
}
