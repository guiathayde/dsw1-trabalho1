package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Professional;

@SuppressWarnings("unchecked")
public interface IProfissionalDAO extends CrudRepository<Professional, Long> {
    Professional findById(long id);

    Professional findByCpf(String cpf);

    Professional findByEmail(String email);

    List<Professional> findAll();

    Professional save(Professional profissional);

    void deleteById(Long id);
}
