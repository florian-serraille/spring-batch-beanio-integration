package com.labs.beanio.xml.domain;

public class Trailer implements Register {
    private String recordType;
    private Integer recordCount;

    @Override
    public String toString() {
        return "Trailer{" +
                "recordType='" + recordType + '\'' +
                ", recordCount=" + recordCount +
                '}';
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
}
