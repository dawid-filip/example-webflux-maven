package com.df.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "pet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@ToString(callSuper = true)
public class Pet extends BasicAudit {

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

}
