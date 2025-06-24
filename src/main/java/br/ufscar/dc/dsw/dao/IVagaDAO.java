package br.ufscar.dc.dsw.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.domain.Vacancy;

@SuppressWarnings("unchecked")
public interface IVagaDAO extends CrudRepository<Vacancy, Long> {
    Vacancy findById(long id);

    List<Vacancy> findAll();

    List<Vacancy> findByCompany(Company company);

    List<Vacancy> findByCompanyAndRegistrationDeadlineAfter(Company company, Date date);

    List<Vacancy> findByCompanyAndRegistrationDeadlineBefore(Company company, Date date);

    List<Vacancy> findByRegistrationDeadlineAfter(Date date);

    List<Vacancy> findByRegistrationDeadlineAfterAndCityLikeIgnoreCase(Date date, String city);

    List<Vacancy> findByActiveTrueAndRegistrationDeadlineBefore(Date date);

    long countByCompanyAndActiveTrue(Company company);

    List<Vacancy> findByTitle(String title);

    Vacancy save(Vacancy vacancy);

    void deleteById(Long id);
}
