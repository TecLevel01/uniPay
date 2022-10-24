package com.matt.unipay.model;

import com.matt.unipay.util.Util;

public class CourseItem {
    public int tuition;
    String cid;
    String name;

    public CourseItem() {
    }

    public String getName() {
        return name;
    }

    public String getTuition() {
        return Util.PriceFormat(tuition);
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
