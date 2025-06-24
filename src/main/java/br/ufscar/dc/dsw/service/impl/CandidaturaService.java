package br.ufscar.dc.dsw.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.domain.Application;
import br.ufscar.dc.dsw.domain.Professional;
import br.ufscar.dc.dsw.domain.Vacancy;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;

@Service
@Transactional(readOnly = false)
public class CandidaturaService implements ICandidaturaService {

    @Autowired
    ICandidaturaDAO dao;

    @Override
    public void salvar(Application candidatura) {
        dao.save(candidatura);
    }

    @Override
    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Application buscarPorId(Long id) {
        return dao.findById(id.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> buscarPorProfissional(Professional profissional) {
        return dao.findByProfessional(profissional);
    }

    @Override
    @Transactional(readOnly = true)
    public Application buscarPorProfissionalEVaga(Professional profissional, Vacancy vaga) {
        return dao.findByProfessionalAndVacancy(profissional, vaga);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> buscarPorVaga(Vacancy vaga) {
        return dao.findByVacancy(vaga);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean jaCandidatou(Professional profissional, Vacancy vaga) {
        return dao.findByProfessionalAndVacancy(profissional, vaga) != null;
    }
}