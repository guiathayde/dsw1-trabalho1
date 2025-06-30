package br.ufscar.dc.dsw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;

@Service
@Transactional(readOnly = false)
public class CandidaturaService implements ICandidaturaService {

    @Autowired
    ICandidaturaDAO candidaturaDAO;

    @Override
    public Candidatura buscarPorId(Long id) {
        return candidaturaDAO.findById(id).get();
    }

    @Override
    public List<Candidatura> buscarTodos() {
        return candidaturaDAO.findAll();
    }

    @Override
    public void salvar(Candidatura candidatura) {
        candidaturaDAO.save(candidatura);
    }

    @Override
    public void excluir(Long id) {
        candidaturaDAO.deleteById(id);
    }

    @Override
    public List<Candidatura> buscarPorProfissional(Profissional profissional) {
        return candidaturaDAO.findByProfissional(profissional);
    }

    @Override
    public Candidatura buscarPorProfissionalEVaga(Profissional profissional, Vaga vaga) {
        return candidaturaDAO.findByProfissionalAndVaga(profissional, vaga);
    }

    @Override
    public List<Candidatura> buscarCandidaturasPorVaga(Vaga vaga) {
        return candidaturaDAO.findByVaga(vaga);
    }
}
