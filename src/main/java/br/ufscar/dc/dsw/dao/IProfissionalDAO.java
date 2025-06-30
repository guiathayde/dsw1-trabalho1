package br.ufscar.dc.dsw.dao;

import br.ufscar.dc.dsw.domain.Profissional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProfissionalDAO extends CrudRepository<Profissional, Long> {
    Profissional findById(long id);

    List<Profissional> findAll();

    Profissional save(Profissional profissional);

    void deleteById(Long id);

    @Query("SELECT p FROM Profissional p WHERE p.username = :username")
    Profissional findByUsername(@Param("username") String username);
}