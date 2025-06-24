package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Application;
import br.ufscar.dc.dsw.domain.Professional;
import br.ufscar.dc.dsw.domain.Vacancy;

@SuppressWarnings("unchecked")
public interface ICandidaturaDAO extends CrudRepository<Application, Long> {
    Application findById(long id);

    List<Application> findByProfessional(Professional professional);

    Application findByProfessionalAndVacancy(Professional professional, Vacancy vacancy);

    List<Application> findByVacancy(Vacancy vacancy);

    Application save(Application application);

    void deleteById(Long id);
}
