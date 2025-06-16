package com.work.services;

import com.work.models.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company register(Company company);
    Company update(Long id, Company companyDetails);
    Optional<Company> findByEmail(String email);
    Company findById(Long id);
    List<Company> findAll();
    void delete(Long id);
}
