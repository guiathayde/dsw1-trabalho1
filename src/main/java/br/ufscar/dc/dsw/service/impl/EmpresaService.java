package br.ufscar.dc.dsw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = false)
public class EmpresaService implements IEmpresaService {

    @Autowired
    IEmpresaDAO dao;

    public void salvar(Empresa empresa) {
        dao.save(empresa);
    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Empresa buscarPorId(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Empresa> buscarTodos() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Empresa> buscarPorCidade(String cidade) {
        return dao.findByCidade(cidade);
    }
}
