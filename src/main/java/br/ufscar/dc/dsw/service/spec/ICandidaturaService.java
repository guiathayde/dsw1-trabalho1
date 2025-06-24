package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Application;
import br.ufscar.dc.dsw.domain.Professional;
import br.ufscar.dc.dsw.domain.Vacancy;

public interface ICandidaturaService {
    Application buscarPorId(Long id);

    List<Application> buscarPorProfissional(Professional profissional);

    Application buscarPorProfissionalEVaga(Professional profissional, Vacancy vaga);

    List<Application> buscarPorVaga(Vacancy vaga);

    void salvar(Application candidatura);

    void excluir(Long id);

    boolean jaCandidatou(Professional profissional, Vacancy vaga);
}
