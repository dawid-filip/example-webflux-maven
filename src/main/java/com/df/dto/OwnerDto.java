package com.df.dto;

import lombok.*;

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

}
