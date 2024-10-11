package com.example.samples.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;


/*

user:
id=>id
username=>username
first_name=>first index of 'name' split with space
last_name=>remaining of 'name' after first index of 'name' split with space
email=>email
address=>concatenate with space delimitted, (address.street, address.suite, address.city, address.zipcode) with address.geo.lat+':'+address.geo.lng
company_id=>company_id 'id' FK for 'company' table
record_date=>current server date

*/


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String address;
    private Long company_id;
    private Date record_date;

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
