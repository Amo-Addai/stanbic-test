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

}
