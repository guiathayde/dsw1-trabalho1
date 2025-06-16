package com.work.services;

import com.work.models.Company;
import com.work.models.Vacancy;
import com.work.repositories.VacancyRepository;
import com.work.exceptions.ResourceNotFoundException;
import com.work.exceptions.UnauthorizedOperationException; // New Exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Transactional
    @Override
    public Vacancy createVacancy(Vacancy vacancy, Company company) {
        vacancy.setCompany(company);
        // Additional validation for vacancy fields can be done here or by Bean
        // Validation
        return vacancyRepository.save(vacancy);
    }

    @Transactional
    @Override
    public Vacancy updateVacancy(Long vacancyId, Vacancy vacancyDetails, Company company) {
        Vacancy existingVacancy = findVacancyById(vacancyId);
        if (!Objects.equals(existingVacancy.getCompany().getId(), company.getId())) {
            throw new UnauthorizedOperationException("Company does not own this vacancy.");
        }
        existingVacancy.setDescription(vacancyDetails.getDescription());
        existingVacancy.setRemuneration(vacancyDetails.getRemuneration());
        existingVacancy.setApplicationDeadline(vacancyDetails.getApplicationDeadline());
        existingVacancy.setCity(vacancyDetails.getCity());
        // Add other updatable fields
        return vacancyRepository.save(existingVacancy);
    }

    @Transactional
    @Override
    public void deleteVacancy(Long vacancyId, Company company) {
        Vacancy existingVacancy = findVacancyById(vacancyId);
        if (!Objects.equals(existingVacancy.getCompany().getId(), company.getId())) {
            throw new UnauthorizedOperationException("Company does not own this vacancy.");
        }
        vacancyRepository.delete(existingVacancy);
    }

    @Override
    public Vacancy findVacancyById(Long vacancyId) {
        return vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy not found with id: " + vacancyId));
    }

    @Override
    public List<Vacancy> findAllActiveVacancies() { // R4
        return vacancyRepository.findByApplicationDeadlineGreaterThanEqual(LocalDate.now());
    }

    @Override
    public List<Vacancy> findActiveVacanciesByCity(String city) { // R4
        return vacancyRepository.findByCityAndApplicationDeadlineGreaterThanEqual(city, LocalDate.now());
    }

    @Override
    public List<Vacancy> findAllVacanciesByCompany(Company company) { // R6
        return vacancyRepository.findByCompany(company);
    }

    @Override
    public List<Vacancy> findAllActiveVacanciesByCompany(Company company) {
        return vacancyRepository.findByCompanyAndApplicationDeadlineGreaterThanEqual(company, LocalDate.now());
    }
}
