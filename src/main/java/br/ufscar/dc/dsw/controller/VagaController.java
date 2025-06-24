package br.ufscar.dc.dsw.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.domain.Vacancy;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;
import br.ufscar.dc.dsw.service.spec.IVagaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private IVagaService vagaService;

    @Autowired
    private IEmpresaService empresaService;

    @GetMapping("/listar")
    public String listar(@RequestParam(value = "cidade", required = false) String cidade, ModelMap model) {
        List<Vacancy> vagas;
        if (cidade != null && !cidade.isEmpty()) {
            vagas = vagaService.buscarVagasEmAbertoPorCidade(cidade);
        } else {
            vagas = vagaService.buscarTodasVagasEmAberto();
        }

        Map<Long, Long> empresaVagasCountMap = new HashMap<>();
        for (Vacancy vaga : vagas) {
            Long empresaId = vaga.getCompany().getId();
            if (!empresaVagasCountMap.containsKey(empresaId)) {
                long count = vagaService.contarVagasAtivasPorEmpresa(vaga.getCompany());
                empresaVagasCountMap.put(empresaId, count);
            }
        }

        model.addAttribute("vagas", vagas);
        model.addAttribute("empresaVagasCountMap", empresaVagasCountMap);
        return "vaga/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Vacancy vaga) {
        return "vaga/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Vacancy vaga, BindingResult result, RedirectAttributes attr, Principal principal) {
        if (result.hasErrors()) {
            return "vaga/cadastro";
        }

        Company empresa = empresaService.buscarPorEmail(principal.getName());
        if (empresa == null) {
            attr.addFlashAttribute("fail", "Erro: Company não encontrada para o usuário logado.");
            return "redirect:/vagas/listar";
        }

        vaga.setCompany(empresa);
        vaga.setActive(true);
        vaga.setRegistrationDeadline(vaga.getRegistrationDeadline());

        vagaService.salvar(vaga);
        attr.addFlashAttribute("sucess", "Vacancy cadastrada com sucesso!");
        return "redirect:/vagas/minhasVagas";
    }

    @GetMapping("/minhasVagas")
    public String minhasVagas(ModelMap model, Principal principal) {
        Company empresa = empresaService.buscarPorEmail(principal.getName());
        List<Vacancy> vagasAtivas = vagaService.buscarVagasAbertasPorEmpresa(empresa);
        List<Vacancy> vagasExpiradas = vagaService.buscarVagasExpiradasPorEmpresa(empresa);

        model.addAttribute("vagasAtivas", vagasAtivas);
        model.addAttribute("vagasExpiradas", vagasExpiradas);
        return "vaga/minhasVagas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        try {
            vagaService.excluir(id);
            attr.addFlashAttribute("sucess", "Vacancy excluída com sucesso!");
        } catch (Exception e) {
            return "redirect:/erro?msg=Não foi possível excluir a vaga. Verifique se ela possui candidaturas ativas.";
        }
        return "redirect:/vagas/minhasVagas";
    }
}