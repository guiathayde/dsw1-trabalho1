package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import org.springframework.validation.Errors;

import br.ufscar.dc.dsw.domain.Company;

public interface IEmpresaService {
    Company buscarPorId(Long id);

    List<Company> buscarTodos();

    void salvar(Company empresa);

    void excluir(Long id);

    Company buscarPorEmail(String email);

    Company buscarPorCnpj(String cnpj);

    void validarCamposUnicos(Company empresa, Errors errors);
}
