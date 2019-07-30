package com.labs.beanio.annotation.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Employee implements Register {
    private String recordType;
    private String firstName;
    private String lastName;
    private String title;
    private int salary;
    private Date hireDate;
}