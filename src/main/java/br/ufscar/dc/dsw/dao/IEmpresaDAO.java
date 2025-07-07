package br.ufscar.dc.dsw.dao;

import br.ufscar.dc.dsw.domain.Empresa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEmpresaDAO extends CrudRepository<Empresa, Long> {
    Empresa findById(long id);

    List<Empresa> findAll();

    Empresa save(Empresa empresa);

    void deleteById(Long id);

    @Query("SELECT e FROM Empresa e WHERE e.username = :username")
    Empresa findByUsername(@Param("username") String username);

    @Query("SELECT e FROM Empresa e WHERE e.cnpj = :cnpj")
    Empresa findByCnpj(@Param("cnpj") String cnpj);

    List<Empresa> findByCidade(String cidade);
}