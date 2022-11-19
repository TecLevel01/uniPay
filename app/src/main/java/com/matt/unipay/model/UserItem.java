package com.matt.unipay.model;

public class UserItem {
    String uid, fname, lname, email, course, gender, regno, year, sem;
    int paid;

    public int getPaid() {
        return paid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }

    public String getGender() {
        return gender;
    }

    public String getRegno() {
        return regno;
    }

    public String getYear() {
        return year;
    }

    public String getSem() {
        return sem;
    }
}
