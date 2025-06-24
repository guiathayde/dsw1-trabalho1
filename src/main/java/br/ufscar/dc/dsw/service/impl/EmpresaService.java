package br.ufscar.dc.dsw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.domain.User;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;

@Service
@Transactional(readOnly = false)
public class EmpresaService implements IEmpresaService {

    @Autowired
    IEmpresaDAO dao;

    @Autowired
    IUsuarioDAO usuarioDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void salvar(Company empresa) {
        if (empresa.getId() == null) {
            empresa.setPassword(passwordEncoder.encode(empresa.getPassword()));
        } else {
            Company empresaExistente = dao.findById(empresa.getId()).orElse(null);
            if (empresa.getPassword() == null || empresa.getPassword().isEmpty()) {
                if (empresaExistente != null) {
                    empresa.setPassword(empresaExistente.getPassword());
                }
            } else {
                empresa.setPassword(passwordEncoder.encode(empresa.getPassword()));
            }
        }
        dao.save(empresa);
    }

    public void validarCamposUnicos(Company empresa, Errors errors) {
        User usuarioPorEmail = usuarioDAO.findByEmail(empresa.getEmail());

        if (empresa.getId() == null) {
            if (usuarioPorEmail != null) {
                errors.rejectValue("email", "Unique.usuario.email", "E-mail já cadastrado.");
            }
        } else {
            if (usuarioPorEmail != null && !usuarioPorEmail.getId().equals(empresa.getId())) {
                errors.rejectValue("email", "Unique.usuario.email", "E-mail já cadastrado.");
            }
        }

        Company empresaPorCnpj = dao.findByCnpj(empresa.getCnpj());

        if (empresa.getId() == null) {
            if (empresaPorCnpj != null) {
                errors.rejectValue("cnpj", "Unique.empresa.cnpj", "CNPJ já cadastrado.");
            }
        } else {
            if (empresaPorCnpj != null && !empresaPorCnpj.getId().equals(empresa.getId())) {
                errors.rejectValue("cnpj", "Unique.empresa.cnpj", "CNPJ já cadastrado.");
            }
        }
    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Company buscarPorId(Long id) {
        return dao.findById(id.longValue());
    }

    @Transactional(readOnly = true)
    public List<Company> buscarTodos() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Company buscarPorEmail(String email) {
        return dao.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Company buscarPorCnpj(String cnpj) {
        return dao.findByCnpj(cnpj);
    }
}