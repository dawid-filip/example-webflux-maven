package com.df.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
@ToString
public abstract class BasicAudit {

    @Column("created_at")
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdAt;

    @Column("updated_at")
    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedAt;

    @Column("created_by")
    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @Column("updated_by")
    @LastModifiedBy
    @JsonIgnore
    private String updatedBy;

    @Column("version")
    @Version
    @JsonIgnore
    private Long version;

}
