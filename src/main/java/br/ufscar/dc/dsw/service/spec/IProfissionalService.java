package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import org.springframework.validation.Errors;

import br.ufscar.dc.dsw.domain.Professional;

public interface IProfissionalService {
    Professional buscarPorId(Long id);

    Professional buscarPorCpf(String cpf);

    Professional buscarPorEmail(String email);

    List<Professional> buscarTodos();

    void salvar(Professional profissional);

    void excluir(Long id);

    void validarCamposUnicos(Professional profissional, Errors errors);
}
