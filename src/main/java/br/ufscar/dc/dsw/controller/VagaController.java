package br.ufscar.dc.dsw.controller;

import jakarta.validation.Valid;

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
    private IVagaService vagaService;

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
        vagaService.salvar(vaga);
        attr.addFlashAttribute("sucess", "vaga.create.sucess");
        return "redirect:/vagas/listar";
    }

    @GetMapping("/listar")
    public String listarMinhasVagas(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
        Empresa empresa = (Empresa) userDetails.getUsuario();
        model.addAttribute("vagas", vagaService.buscarPorEmpresa(empresa));
        return "vaga/lista";
    }

    @GetMapping("/candidaturas/{id}")
    public String listarCandidaturasPorVaga(@PathVariable("id") Long id, ModelMap model) {
        Vaga vaga = vagaService.buscarPorId(id);
        model.addAttribute("vaga", vaga);
        model.addAttribute("candidaturas", candidaturaService.buscarCandidaturasPorVaga(vaga));
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

        if ("ENTREVISTA".equals(status)) {
            body += "Sua candidatura para a vaga de " + candidatura.getVaga().getDescricao()
                    + " foi selecionada para entrevista.\n";
            body += "Horário da entrevista: " + horarioEntrevista + "\n";
            body += "Link da entrevista: " + linkEntrevista + "\n\n";
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
