package com.df.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@ToString//(callSuper = true)
public class PetDto extends BasicAuditDto {

    private Long id;
    private String name;
    private Short age;
    private Short weight;
    private Short length;

}
