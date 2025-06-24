package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.User;

public interface IUsuarioService {
    User buscarPorId(Long id);

    User buscarPorEmail(String email);

    List<User> buscarTodos();

    void salvar(User usuario);

    void excluir(Long id);
}
