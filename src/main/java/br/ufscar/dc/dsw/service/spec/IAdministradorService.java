package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Admin;

public interface IAdministradorService {
    Admin buscarPorId(Long id);

    List<Admin> buscarTodos();

    void salvar(Admin administrador);

    void excluir(Long id);

    Admin buscarPorEmail(String email);
}
