package br.ufscar.dc.dsw.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.domain.Vacancy;
import br.ufscar.dc.dsw.service.spec.IVagaService;

@Service
@Transactional(readOnly = false)
public class VagaService implements IVagaService {

    @Autowired
    IVagaDAO dao;

    public void salvar(Vacancy vaga) {
        dao.save(vaga);
    }

    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Transactional
    public void desativarVagasExpiradas() {
        Date hoje = Date.valueOf(LocalDate.now());
        List<Vacancy> vagasExpiradasAtivas = dao.findByActiveTrueAndRegistrationDeadlineBefore(hoje);

        for (Vacancy vaga : vagasExpiradasAtivas) {
            vaga.setActive(false);
            dao.save(vaga);
        }
        System.out.println("Desativadas " + vagasExpiradasAtivas.size() + " vagas expiradas.");
    }

    @Transactional(readOnly = true)
    public Vacancy buscarPorId(Long id) {
        return dao.findById(id.longValue());
    }

    @Transactional(readOnly = true)
    public List<Vacancy> buscarTodos() {
        desativarVagasExpiradas();
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Vacancy> buscarPorEmpresa(Company empresa) {
        return dao.findByCompany(empresa);
    }

    @Transactional(readOnly = true)
    public List<Vacancy> buscarTodasVagasEmAberto() {
        desativarVagasExpiradas();
        return dao.findByRegistrationDeadlineAfter(Date.valueOf(LocalDate.now()));
    }

    @Transactional(readOnly = true)
    public List<Vacancy> buscarVagasEmAbertoPorCidade(String cidade) {
        desativarVagasExpiradas();
        return dao.findByRegistrationDeadlineAfterAndCityLikeIgnoreCase(Date.valueOf(LocalDate.now()), cidade);
    }

    @Transactional(readOnly = true)
    public long contarVagasAtivasPorEmpresa(Company empresa) {
        desativarVagasExpiradas();
        return dao.countByCompanyAndActiveTrue(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vacancy> buscarVagasAbertasPorEmpresa(Company empresa) {
        return dao.findByCompanyAndRegistrationDeadlineAfter(empresa, Date.valueOf(LocalDate.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vacancy> buscarVagasExpiradasPorEmpresa(Company empresa) {
        desativarVagasExpiradas();
        return dao.findByCompanyAndRegistrationDeadlineBefore(empresa, Date.valueOf(LocalDate.now()));
    }
}