package com.example.samples.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;


/*

company:
id=>system generated id
name=>company.name
info=>company.catchPhrase
code=>company.bs
record_date=>company.record_date

*/


@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String info;
    private String code;
    private Date record_date;

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
