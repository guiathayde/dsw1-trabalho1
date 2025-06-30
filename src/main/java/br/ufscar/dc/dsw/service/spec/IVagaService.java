package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.domain.Empresa;

public interface IVagaService {

    Vaga buscarPorId(Long id);

    List<Vaga> buscarTodos();

    void salvar(Vaga vaga);

    void excluir(Long id);

    List<Vaga> buscarPorEmpresa(Empresa empresa);

    List<Vaga> buscarVagasAbertas();

    List<Vaga> buscarVagasAbertasPorCidade(String cidade);
}
