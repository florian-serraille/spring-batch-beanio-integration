package com.labs.beanio.withoutintegration.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Header implements Register {
    private String recordType;
    private Date fileDate;
}
