package com.work.services;

import com.work.models.Company;
import com.work.repositories.CompanyRepository;
import com.work.exceptions.ResourceNotFoundException;
import com.work.exceptions.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Company register(Company company) {
        if (companyRepository.findByEmail(company.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Company with email " + company.getEmail() + " already exists.");
        }
        if (companyRepository.findByCnpj(company.getCnpj()).isPresent()) {
            throw new DuplicateResourceException("Company with CNPJ " + company.getCnpj() + " already exists.");
        }
        company.setPassword(passwordEncoder.encode(company.getPassword()));
        return companyRepository.save(company);
    }

    @Transactional
    @Override
    public Company update(Long id, Company companyDetails) {
        Company existingCompany = findById(id);

        if (!existingCompany.getEmail().equals(companyDetails.getEmail()) &&
            companyRepository.findByEmail(companyDetails.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email " + companyDetails.getEmail() + " is already in use.");
        }
        if (!existingCompany.getCnpj().equals(companyDetails.getCnpj()) &&
            companyRepository.findByCnpj(companyDetails.getCnpj()).isPresent()) {
            throw new DuplicateResourceException("CNPJ " + companyDetails.getCnpj() + " is already in use.");
        }

        existingCompany.setEmail(companyDetails.getEmail());
        existingCompany.setName(companyDetails.getName());
        existingCompany.setDescription(companyDetails.getDescription());
        existingCompany.setCity(companyDetails.getCity());
        if (companyDetails.getPassword() != null && !companyDetails.getPassword().isEmpty()) {
            existingCompany.setPassword(passwordEncoder.encode(companyDetails.getPassword()));
        }
        // CNPJ is usually not updatable
        // existingCompany.setCnpj(companyDetails.getCnpj());

        return companyRepository.save(existingCompany);
    }

    @Override
    public Optional<Company> findByEmail(String email) {
        return companyRepository.findByEmail(email);
    }

    @Override
    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }
}
