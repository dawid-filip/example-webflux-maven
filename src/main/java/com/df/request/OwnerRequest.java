package com.df.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class OwnerRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private Short age;
    private List<Long> petIds;

}
