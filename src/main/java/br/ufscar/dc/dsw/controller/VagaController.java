package br.ufscar.dc.dsw.controller;

import jakarta.validation.Valid;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;
import br.ufscar.dc.dsw.service.spec.IEmailService;
import br.ufscar.dc.dsw.service.spec.IVagaService;
import br.ufscar.dc.dsw.security.UsuarioDetails;

@Controller
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ICandidaturaService candidaturaService;

    @Autowired
    private IEmailService emailService;

    @GetMapping("/cadastrar")
    public String cadastrar(Vaga vaga, ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
        Empresa empresa = (Empresa) userDetails.getUsuario();
        model.addAttribute("empresa", empresa);
        return "vaga/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Vaga vaga, BindingResult result, RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
            Empresa empresa = (Empresa) userDetails.getUsuario();
            model.addAttribute("empresa", empresa);
            return "vaga/cadastro";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
        Empresa empresaLogada = (Empresa) userDetails.getUsuario();
        vaga.setEmpresa(empresaLogada);
        String url = "http://localhost:8080/api/vagas";
        try {
            restTemplate.postForObject(url, vaga, Vaga.class);
            attr.addFlashAttribute("sucess", "vaga.create.sucess");
        } catch (HttpClientErrorException e) {
            attr.addFlashAttribute("fail", "vaga.create.fail");
        }
        return "redirect:/vagas/listar";
    }

    @GetMapping("/listar")
    public String listarMinhasVagas(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
        Empresa empresa = (Empresa) userDetails.getUsuario();
        String url = "http://localhost:8080/api/vagas/empresas/{id}";
        Vaga[] vagas = restTemplate.getForObject(url, Vaga[].class, empresa.getId());
        model.addAttribute("vagas", Arrays.asList(vagas));
        return "vaga/lista";
    }

    @GetMapping("/candidaturas/{id}")
    public String listarCandidaturasPorVaga(@PathVariable("id") Long id, ModelMap model) {
        String vagaUrl = "http://localhost:8080/api/vagas/{id}";
        Vaga vaga = restTemplate.getForObject(vagaUrl, Vaga.class, id);
        model.addAttribute("vaga", vaga);

        String candidaturasUrl = "http://localhost:8080/api/candidaturas/vagas/{id}";
        Candidatura[] candidaturas = restTemplate.getForObject(candidaturasUrl, Candidatura[].class, id);
        model.addAttribute("candidaturas", Arrays.asList(candidaturas));
        return "vaga/candidaturas";
    }

    @PostMapping("/candidaturas/atualizarStatus")
    public String atualizarStatusCandidatura(@RequestParam("candidaturaId") Long candidaturaId,
            @RequestParam("status") String status,
            @RequestParam(value = "horarioEntrevista", required = false) String horarioEntrevista,
            @RequestParam(value = "linkEntrevista", required = false) String linkEntrevista,
            RedirectAttributes attr) {
        Candidatura candidatura = candidaturaService.buscarPorId(candidaturaId);
        if (candidatura == null) {
            attr.addFlashAttribute("fail", "Candidatura não encontrada.");
            return "redirect:/vagas/candidaturas/" + candidatura.getVaga().getId();
        }

        candidatura.setStatus(status);
        candidaturaService.salvar(candidatura);

        String subject = "Atualização da sua candidatura para a vaga de " + candidatura.getVaga().getDescricao();
        String body = "Prezado(a) " + candidatura.getProfissional().getName() + ",\n\n";

        if ("ENTREVISTA".equals(status) && horarioEntrevista != null && linkEntrevista != null) {
            body += "Sua candidatura para a vaga de " + candidatura.getVaga().getDescricao()
                    + " foi selecionada para entrevista.\n";
            body += "Horário da entrevista: " + horarioEntrevista + "\n";
            body += "Link da entrevista: " + linkEntrevista + "\n\n";
            body += "Aguardamos você!\n\n";
        } else if ("ENTREVISTA".equals(status) && horarioEntrevista == null && linkEntrevista == null) {
            body += "Sua candidatura para a vaga de " + candidatura.getVaga().getDescricao()
                    + " foi selecionada para entrevista.\n"
                    + "Logo você receberá um e-mail com mais informações sobre a data e horário da entrevista.\n\n";
            body += "Aguardamos você!\n\n";
        } else if ("NAO_SELECIONADO".equals(status)) {
            body += "Informamos que sua candidatura para a vaga de " + candidatura.getVaga().getDescricao()
                    + " não foi selecionada neste momento.\n\n";
            body += "Agradecemos seu interesse e desejamos sucesso em suas próximas candidaturas.\n\n";
        }
        body += "Atenciosamente,\n" + candidatura.getVaga().getEmpresa().getName();

        emailService.sendEmail(candidatura.getProfissional().getUsername(), subject, body);

        attr.addFlashAttribute("sucess", "vaga.update.success");
        return "redirect:/vagas/candidaturas/" + candidatura.getVaga().getId();
    }
}
