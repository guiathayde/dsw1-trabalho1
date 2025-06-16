package com.work.repositories;

import com.work.models.Vacancy;
import com.work.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    // R4: Listagem de todas as vagas (em aberto)
    // "Em aberto" means applicationDeadline >= today
    List<Vacancy> findByApplicationDeadlineGreaterThanEqual(LocalDate currentDate);

    // R4: Filtrar vagas (em aberto) por cidade
    List<Vacancy> findByCityAndApplicationDeadlineGreaterThanEqual(String city, LocalDate currentDate);

    // R6: Listagem de todas as vagas de uma empresa
    List<Vacancy> findByCompany(Company company);

    // Overload for R6 that also considers "em aberto" status if needed, or combine in service layer
    List<Vacancy> findByCompanyAndApplicationDeadlineGreaterThanEqual(Company company, LocalDate currentDate);

    // Example of a more complex query if needed, e.g., search by keyword in description
    @Query("SELECT v FROM Vacancy v WHERE LOWER(v.description) LIKE LOWER(concat('%', :keyword, '%')) AND v.applicationDeadline >= :currentDate")
    List<Vacancy> searchActiveByKeyword(@Param("keyword") String keyword, @Param("currentDate") LocalDate currentDate);
}
