package br.ufscar.dc.dsw.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.domain.Empresa;

@SuppressWarnings("unchecked")
public interface IVagaDAO extends CrudRepository<Vaga, Long> {

    Vaga findById(long id);

    List<Vaga> findAll();

    Vaga save(Vaga vaga);

    void deleteById(Long id);

    List<Vaga> findByEmpresa(Empresa empresa);

    List<Vaga> findByDataLimiteInscricaoAfter(LocalDate data);

    List<Vaga> findByDataLimiteInscricaoAfterAndEmpresaCidade(LocalDate data, String cidade);

    List<Vaga> findByEmpresaAndDataLimiteInscricaoAfter(Empresa empresa, LocalDate data);

    Vaga findByDescricao(String descricao);
}
