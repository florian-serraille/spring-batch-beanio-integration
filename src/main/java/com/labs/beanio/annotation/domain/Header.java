package com.labs.beanio.annotation.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import java.util.Date;

@Data
@NoArgsConstructor
@Record
public class Header implements Register {
    @Field(rid = true, literal = "Header")
    private String recordType;
    @Field(format = "MMddyyyy")
    private Date fileDate;
}
