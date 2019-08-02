package com.labs.beanio.annotation.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

@Data
@NoArgsConstructor
@Record
public class Trailer implements Register {
    @Field(rid = true, literal = "Trailer")
    private String recordType;
    @Field
    private Integer recordCount;
}
