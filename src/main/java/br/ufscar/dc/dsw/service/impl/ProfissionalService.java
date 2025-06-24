package br.ufscar.dc.dsw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IProfissionalDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Professional;
import br.ufscar.dc.dsw.domain.User;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;

@Service
@Transactional(readOnly = false)
public class ProfissionalService implements IProfissionalService {

    @Autowired
    IProfissionalDAO profissionalDAO;

    @Autowired
    IUsuarioDAO usuarioDAO;

    @Autowired
    ICandidaturaDAO candidaturaDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void salvar(Professional profissional) {
        if (profissional.getId() == null) {
            profissional.setPassword(passwordEncoder.encode(profissional.getPassword()));
        } else {
            Professional profissionalExistente = profissionalDAO.findById(profissional.getId()).orElse(null);
            if (profissional.getPassword() == null || profissional.getPassword().isEmpty()) {
                if (profissionalExistente != null) {
                    profissional.setPassword(profissionalExistente.getPassword());
                }
            } else {
                profissional.setPassword(passwordEncoder.encode(profissional.getPassword()));
            }
        }
        profissionalDAO.save(profissional);
    }

    public void validarCamposUnicos(Professional profissional, Errors errors) {
        User usuarioPorEmail = usuarioDAO.findByEmail(profissional.getEmail());

        if (profissional.getId() == null) {
            if (usuarioPorEmail != null) {
                errors.rejectValue("email", "Unique.usuario.email", "E-mail já cadastrado.");
            }
        } else {
            if (usuarioPorEmail != null && !usuarioPorEmail.getId().equals(profissional.getId())) {
                errors.rejectValue("email", "Unique.usuario.email", "E-mail já cadastrado.");
            }
        }

        Professional profissionalPorCpf = profissionalDAO.findByCpf(profissional.getCpf());

        if (profissional.getId() == null) {
            if (profissionalPorCpf != null) {
                errors.rejectValue("cpf", "Unique.profissional.cpf", "CPF já cadastrado.");
            }
        } else {
            if (profissionalPorCpf != null && !profissionalPorCpf.getId().equals(profissional.getId())) {
                errors.rejectValue("cpf", "Unique.profissional.cpf", "CPF já cadastrado.");
            }
        }
    }

    @Override
    public void excluir(Long id) {
        if (profissionalPossuiCandidaturas(id)) {
            throw new RuntimeException("Professional não pode ser excluído. Possui candidaturas associadas.");
        }
        profissionalDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Professional buscarPorId(Long id) {
        return profissionalDAO.findById(id.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public Professional buscarPorCpf(String cpf) {
        return profissionalDAO.findByCpf(cpf);
    }

    @Override
    @Transactional(readOnly = true)
    public Professional buscarPorEmail(String email) {
        return profissionalDAO.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Professional> buscarTodos() {
        return (List<Professional>) profissionalDAO.findAll();
    }

    @Transactional(readOnly = true)
    public boolean profissionalPossuiCandidaturas(Long id) {
        Professional profissional = profissionalDAO.findById(id).orElse(null);
        return profissional != null && !candidaturaDAO.findByProfessional(profissional).isEmpty();
    }
}