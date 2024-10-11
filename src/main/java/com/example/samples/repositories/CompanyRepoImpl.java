package com.example.samples.repositories;

import com.example.samples.models.Company;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CompanyRepoImpl implements CompanyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Company save(Company company) {
        if (company != null) {
            entityManager.persist(company); // Insert new entity
        } else {
            entityManager.merge(company);   // Update existing entity
        }
        return company;
    }

    @Override
    public Company findById(Long id) {
        return entityManager.find(Company.class, id);
    }

    @Override
    public List<Company> findAll() {
        return entityManager.createQuery("from Company", Company.class).getResultList();
    }

}
