package com.df.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(exclude = "pets")
@ToString//(callSuper = true)
public class OwnerDto extends BasicAuditDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Short age;
    private List<PetDto> pets;

}
