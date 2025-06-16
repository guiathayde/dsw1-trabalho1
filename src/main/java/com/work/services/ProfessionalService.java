package com.work.services;

import com.work.models.Professional;
import java.util.List;
import java.util.Optional;

public interface ProfessionalService {
    Professional register(Professional professional);
    Professional update(Long id, Professional professionalDetails);
    Optional<Professional> findByEmail(String email);
    Professional findById(Long id);
    List<Professional> findAll();
    void delete(Long id);
}
