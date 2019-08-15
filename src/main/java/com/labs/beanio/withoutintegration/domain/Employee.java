package com.labs.beanio.withoutintegration.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Employee implements Register {
    private String recordType;
    private String firstName;
    private String lastName;
    private String title;
    private int salary;
    private Date hireDate;
}