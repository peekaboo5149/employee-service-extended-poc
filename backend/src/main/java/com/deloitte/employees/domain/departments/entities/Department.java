package com.deloitte.employees.domain.departments.entities;


import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Department {

    private String id;
    private String title;
    private String description;

}
