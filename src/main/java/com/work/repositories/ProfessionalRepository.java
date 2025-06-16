package com.work.repositories;

import com.work.models.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    Optional<Professional> findByEmail(String email);
    Optional<Professional> findByCpf(String cpf);
}
