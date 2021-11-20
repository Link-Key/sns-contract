package com.peifeng.be.model;

import java.util.Date;

public class Account {
    private Integer id;

    private String address;

    private Date createtime;

    private Boolean lssued;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Boolean getLssued() {
        return lssued;
    }

    public void setLssued(Boolean lssued) {
        this.lssued = lssued;
    }
}