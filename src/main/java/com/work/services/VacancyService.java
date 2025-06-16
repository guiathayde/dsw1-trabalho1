package com.work.services;

import com.work.models.Company;
import com.work.models.Vacancy;
import java.util.List;

public interface VacancyService {
    Vacancy createVacancy(Vacancy vacancy, Company company);
    Vacancy updateVacancy(Long vacancyId, Vacancy vacancyDetails, Company company); // Ensure company owns vacancy
    void deleteVacancy(Long vacancyId, Company company); // Ensure company owns vacancy
    Vacancy findVacancyById(Long vacancyId);
    List<Vacancy> findAllActiveVacancies(); // R4
    List<Vacancy> findActiveVacanciesByCity(String city); // R4
    List<Vacancy> findAllVacanciesByCompany(Company company); // R6
    List<Vacancy> findAllActiveVacanciesByCompany(Company company); // Variation of R6
}
