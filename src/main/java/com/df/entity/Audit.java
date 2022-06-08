package com.df.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "audit_entity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Audit {

    @Id
    @Column("id")
    private Long id;

    @Column("entity_value")
    private String entityValue;

    @Column("entity_class")
    private String entityClass;

    @Column("audited_on")
    private LocalDateTime auditedOn;

}