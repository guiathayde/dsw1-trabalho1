package br.ufscar.dc.dsw.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.User;

@SuppressWarnings("unchecked")

public interface IUsuarioDAO extends CrudRepository<User, Long> {
    User findById(long id);

    User findByEmail(String email);

    List<User> findAll();

    User save(User usuario);

    void deleteById(Long id);
}
