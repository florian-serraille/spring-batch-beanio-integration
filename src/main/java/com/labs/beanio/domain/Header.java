package com.labs.beanio.domain;

import java.util.Date;

public class Header implements Register {
    private String recordType;
    private Date fileDate;

    @Override
    public String toString() {
        return "Header{" +
                "recordType='" + recordType + '\'' +
                ", hireDate=" + fileDate +
                '}';
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }
}
