package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Company;

@SuppressWarnings("unchecked")
public interface IEmpresaDAO extends CrudRepository<Company, Long> {
    Company findById(long id);

    Company findByEmail(String email);

    Company findByCnpj(String cnpj);

    List<Company> findAll();

    Company save(Company empresa);

    void deleteById(Long id);
}
