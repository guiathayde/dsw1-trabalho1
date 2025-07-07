package br.ufscar.dc.dsw.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.spec.IVagaService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = false)
public class VagaService implements IVagaService {

    @Autowired
    IVagaDAO dao;

    @Autowired
    IEmpresaDAO empresaDAO;

    public void salvar(Vaga vaga) {
        dao.save(vaga);
    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Vaga> buscarTodos() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Vaga> buscarPorEmpresa(Empresa empresa) {
        return dao.findByEmpresa(empresa);
    }

    @Transactional(readOnly = true)
    public List<Vaga> buscarVagasAbertas() {
        return dao.findByDataLimiteInscricaoAfter(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<Vaga> buscarVagasAbertasPorCidade(String cidade) {
        return dao.findByDataLimiteInscricaoAfterAndEmpresaCidade(LocalDate.now(), cidade);
    }

    @Transactional(readOnly = true)
    public List<Vaga> buscarVagasAbertasPorEmpresa(Long empresaId) {
        Empresa empresa = empresaDAO.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + empresaId));
        return dao.findByEmpresaAndDataLimiteInscricaoAfter(empresa, LocalDate.now());
    }
}
