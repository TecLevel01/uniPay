package com.matt.unipay.model;

import java.util.Date;

public class PaymentItem {
    Date timestamp;
    String year, sem;
    int paid;

    public PaymentItem() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getYear() {
        return year;
    }

    public String getSem() {
        return sem;
    }

    public int getPaid() {
        return paid;
    }
}
