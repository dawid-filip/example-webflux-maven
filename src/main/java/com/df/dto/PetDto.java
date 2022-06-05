package com.df.dto;

import lombok.*;

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

}
