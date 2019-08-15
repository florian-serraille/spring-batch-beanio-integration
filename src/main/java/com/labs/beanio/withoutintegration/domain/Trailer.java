package com.labs.beanio.withoutintegration.domain;

import lombok.Data;

@Data
public class Trailer implements Register {
    private String recordType;
    private Integer recordCount;
}
