package com.work.services;

import com.work.models.Professional;
import com.work.repositories.ProfessionalRepository;
import com.work.exceptions.ResourceNotFoundException;
import com.work.exceptions.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfessionalServiceImpl(ProfessionalRepository professionalRepository, PasswordEncoder passwordEncoder) {
        this.professionalRepository = professionalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Professional register(Professional professional) {
        if (professionalRepository.findByEmail(professional.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Professional with email " + professional.getEmail() + " already exists.");
        }
        if (professionalRepository.findByCpf(professional.getCpf()).isPresent()) {
            throw new DuplicateResourceException("Professional with CPF " + professional.getCpf() + " already exists.");
        }
        professional.setPassword(passwordEncoder.encode(professional.getPassword()));
        return professionalRepository.save(professional);
    }

    @Transactional
    @Override
    public Professional update(Long id, Professional professionalDetails) {
        Professional existingProfessional = findById(id);

        // Check for email conflict if email is being changed
        if (!existingProfessional.getEmail().equals(professionalDetails.getEmail()) &&
            professionalRepository.findByEmail(professionalDetails.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email " + professionalDetails.getEmail() + " is already in use.");
        }
        // Check for CPF conflict if CPF is being changed
         if (!existingProfessional.getCpf().equals(professionalDetails.getCpf()) &&
            professionalRepository.findByCpf(professionalDetails.getCpf()).isPresent()) {
            throw new DuplicateResourceException("CPF " + professionalDetails.getCpf() + " is already in use.");
        }

        existingProfessional.setEmail(professionalDetails.getEmail());
        existingProfessional.setName(professionalDetails.getName());
        existingProfessional.setPhone(professionalDetails.getPhone());
        existingProfessional.setGender(professionalDetails.getGender());
        existingProfessional.setBirthDate(professionalDetails.getBirthDate());
        if (professionalDetails.getPassword() != null && !professionalDetails.getPassword().isEmpty()) {
            existingProfessional.setPassword(passwordEncoder.encode(professionalDetails.getPassword()));
        }
        // CPF is usually not updatable, but if it is:
        // existingProfessional.setCpf(professionalDetails.getCpf());

        return professionalRepository.save(existingProfessional);
    }

    @Override
    public Optional<Professional> findByEmail(String email) {
        return professionalRepository.findByEmail(email);
    }

    @Override
    public Professional findById(Long id) {
        return professionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + id));
    }

    @Override
    public List<Professional> findAll() {
        return professionalRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!professionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Professional not found with id: " + id);
        }
        professionalRepository.deleteById(id);
    }
}
