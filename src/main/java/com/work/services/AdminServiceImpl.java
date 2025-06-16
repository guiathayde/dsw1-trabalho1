package com.work.services;

import com.work.models.Admin;
import com.work.repositories.AdminRepository;
import com.work.exceptions.ResourceNotFoundException;
import com.work.exceptions.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Admin save(Admin admin) {
        if (admin.getId() == null && adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Admin with email " + admin.getEmail() + " already exists.");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }
}
