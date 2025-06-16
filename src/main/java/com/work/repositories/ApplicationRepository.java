package com.work.repositories;

import com.work.models.Application;
import com.work.models.Professional;
import com.work.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // R7: Listagem de todas as candidaturas de um profissional
    List<Application> findByProfessional(Professional professional);

    // Find by professional and vacancy to check for existing application (R5)
    Optional<Application> findByProfessionalAndVacancy(Professional professional, Vacancy vacancy);

    // Find applications for a specific vacancy (useful for R8)
    List<Application> findByVacancy(Vacancy vacancy);
}
