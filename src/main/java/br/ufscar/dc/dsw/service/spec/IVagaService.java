package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.domain.Vacancy;

public interface IVagaService {
    Vacancy buscarPorId(Long id);

    List<Vacancy> buscarTodos();

    List<Vacancy> buscarPorEmpresa(Company empresa);

    List<Vacancy> buscarTodasVagasEmAberto();

    List<Vacancy> buscarVagasEmAbertoPorCidade(String cidade);

    long contarVagasAtivasPorEmpresa(Company empresa);

    void desativarVagasExpiradas();

    void salvar(Vacancy vaga);

    void excluir(Long id);

    List<Vacancy> buscarVagasAbertasPorEmpresa(Company empresa);

    List<Vacancy> buscarVagasExpiradasPorEmpresa(Company empresa);
}
