package com.labs.beanio.annotation.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import java.util.Date;

@Data
@NoArgsConstructor
@Record
public class Employee implements Register {
    @Field(rid = true, literal = "Detail")
    private String recordType;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private String title;
    @Field
    private int salary;
    @Field(format = "MMddyyyy")
    private Date hireDate;
}