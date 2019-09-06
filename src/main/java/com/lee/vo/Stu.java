package com.lee.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

public class Stu implements Serializable {
    private String name;
    private int age;
    private Date date;
    private BigDecimal b;
    private Duration d;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    public Duration getD() {
        return d;
    }

    public void setD(Duration d) {
        this.d = d;
    }
}