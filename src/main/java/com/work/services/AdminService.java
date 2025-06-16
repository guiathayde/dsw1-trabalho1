package com.work.services;

import com.work.models.Admin;
import java.util.List;
import java.util.Optional;

public interface AdminService {
    Admin save(Admin admin);
    Optional<Admin> findByEmail(String email);
    Admin findById(Long id);
    List<Admin> findAll(); // Though likely only one admin initially
    void delete(Long id); // If needed
}
