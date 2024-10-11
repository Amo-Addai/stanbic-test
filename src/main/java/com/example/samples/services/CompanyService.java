package com.example.samples.services;

import com.example.samples.models.Company;
import com.example.samples.models.Company;
import com.example.samples.repositories.CompanyRepository;
import com.example.samples.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company save(Company company) {
        Company company = new Company(); // todo: name, email
        return companyRepository.save(company);
    }

    public Company find(Long id) {
        return companyRepository.findById(id);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

}
