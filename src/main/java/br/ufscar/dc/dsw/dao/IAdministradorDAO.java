package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Admin;

@SuppressWarnings("unchecked")
public interface IAdministradorDAO extends CrudRepository<Admin, Long> {
    Admin findById(long id);

    Admin findByEmail(String email);

    List<Admin> findAll();

    Admin save(Admin administrador);

    void deleteById(Long id);
}
