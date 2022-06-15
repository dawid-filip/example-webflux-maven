package com.df.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@ToString//(callSuper = true)
@JsonPropertyOrder({"id", "name", "age", "weight", "length"})
public class PetDto extends BasicAuditDto {

    private Long id;
    private String name;
    private Short age;
    private Short weight;
    private Short length;

}
