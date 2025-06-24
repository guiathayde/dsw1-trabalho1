package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Application;
import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.domain.Professional;
import br.ufscar.dc.dsw.domain.Status;
import br.ufscar.dc.dsw.domain.Vacancy;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;
import java.util.Date;
import br.ufscar.dc.dsw.service.spec.IEmailService;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;
import br.ufscar.dc.dsw.service.spec.IVagaService;

@Controller
@RequestMapping("/candidaturas")
public class CandidaturaController {

    @Autowired
    private ICandidaturaService candidaturaService;

    @Autowired
    private IProfissionalService profissionalService;

    @Autowired
    private IVagaService vagaService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IEmpresaService empresaService;

    // Exibir formulário de candidatura para uma vaga específica
    @GetMapping("/candidatar/{idVaga}")
    public String exibirFormularioCandidatura(@PathVariable("idVaga") Long idVaga, ModelMap model, Principal principal,
            RedirectAttributes attr) {
        // Encontra a vaga pelo ID
        Vacancy vaga = vagaService.buscarPorId(idVaga);

        // Verifica se a vaga existe
        if (vaga == null) {
            attr.addFlashAttribute("fail", "Vacancy não encontrada.");
            return "redirect:/erro?msg=Vacancy não encontrada.";
        }
        // Verifica se o usuário está logado
        if (principal == null) {
            attr.addFlashAttribute("fail", "Você precisa estar logado para se candidatar.");
            return "redirect:/acessoNegado";
        }
        // Tenta buscar o profissional pelo email do usuário autenticado
        // Se o usuário não for um profissional ou nao estiver logado, redireciona com
        // mensagem de erro
        Professional profissional = profissionalService.buscarPorEmail(principal.getName());
        if (profissional == null) {
            attr.addFlashAttribute("fail", "Você precisa ser um profissional para se candidatar.");
            return "redirect:/acessoNegado";
        }

        // Verifica se o profissional já se candidatou a esta vaga
        if (candidaturaService.jaCandidatou(profissional, vaga)) {
            attr.addFlashAttribute("fail", "Você já se candidatou a esta vaga.");
            return "redirect:/erro?msg=Você já se candidatou para essa vaga!";
        }

        model.addAttribute("vaga", vaga);

        return "candidatura/cadastro";
    }

    @PostMapping("/salvar/{idVaga}")
    public String candidatar(@PathVariable("idVaga") Long idVaga, @RequestParam("file") MultipartFile file,
            RedirectAttributes attr, Principal principal) {

        Professional profissional = profissionalService.buscarPorEmail(principal.getName());
        Vacancy vaga = vagaService.buscarPorId(idVaga);

        if (profissional == null || vaga == null) {
            attr.addFlashAttribute("fail", "Professional ou Vacancy não encontrados.");
            return "redirect:/erro?msg=Não encontramos essa vaga ou profissional";
        }

        try {
            // Verifica se o arquivo não está vazio e se é um PDF
            if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
                attr.addFlashAttribute("fail", "Por favor, anexe um currículo em formato PDF.");
                return "redirect:/candidaturas/candidatar/" + idVaga;
            }
            Application candidatura = new Application();
            candidatura.setProfessional(profissional);
            candidatura.setVacancy(vaga);
            candidatura.setStatus(Status.OPEN);

            candidatura.setResume(file.getBytes());

            candidaturaService.salvar(candidatura);
            attr.addFlashAttribute("sucess", "Application realizada com sucesso!");
        } catch (IOException e) {
            attr.addFlashAttribute("fail", "Ocorreu um erro ao processar o seu currículo.");
            return "redirect:/candidaturas/candidatar/" + idVaga;
        }
        return "redirect:/candidaturas/minhasCandidaturas";
    }

    @GetMapping("/curriculo/{id}")
    public ResponseEntity<Resource> baixarCurriculo(@PathVariable("id") Long id) {
        Application candidatura = candidaturaService.buscarPorId(id);

        if (candidatura == null || candidatura.getResume() == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new ByteArrayResource(candidatura.getResume());

        String filename = "curriculo-" + candidatura.getProfessional().getName().replace(" ", "_") + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                // O header "Content-Disposition" faz download
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/minhasCandidaturas")
    public String minhasCandidaturas(ModelMap model, Principal principal) {
        Professional profissional = profissionalService.buscarPorEmail(principal.getName());
        model.addAttribute("candidaturas", candidaturaService.buscarPorProfissional(profissional));
        return "candidatura/minhasCandidaturas";
    }

    @GetMapping("/gerenciar/{idVaga}")
    public String gerenciarCandidaturas(@PathVariable("idVaga") Long idVaga, ModelMap model, Principal principal) {
        Company empresa = empresaService.buscarPorEmail(principal.getName());
        Vacancy vaga = vagaService.buscarPorId(idVaga);

        if (vaga == null || !vaga.getCompany().getId().equals(empresa.getId())) {
            model.addAttribute("fail", "Acesso não autorizado ou vaga não encontrada.");
            return "redirect:/acessoNegado";
        }

        boolean podeClassificar = vaga.getRegistrationDeadline().before(new Date());
        model.addAttribute("podeClassificar", podeClassificar);
        model.addAttribute("vaga", vaga);
        model.addAttribute("candidaturas", candidaturaService.buscarPorVaga(vaga));
        model.addAttribute("statusOptions", Status.values());
        return "candidatura/gerenciar";
    }

    @PostMapping("/atualizarStatus")
    public String atualizarStatus(@RequestParam("idCandidatura") Long idCandidatura,
            @RequestParam("status") Status status,
            @RequestParam(value = "linkEntrevista", required = false) String linkEntrevista,
            RedirectAttributes attr, Principal principal) {
        Application candidatura = candidaturaService.buscarPorId(idCandidatura);

        Company empresa = empresaService.buscarPorEmail(principal.getName());
        if (candidatura == null || !candidatura.getVacancy().getCompany().getId().equals(empresa.getId())) {
            attr.addFlashAttribute("fail", "Acesso não autorizado ou candidatura não encontrada.");
            return "redirect:/vagas/minhasVagas";
        }

        candidatura.setStatus(status);
        candidaturaService.salvar(candidatura);

        String emailCandidato = candidatura.getProfessional().getEmail();
        String nomeCandidato = candidatura.getProfessional().getName();
        String tituloVaga = candidatura.getVacancy().getTitle();
        String nomeEmpresa = empresa.getName();
        String assunto = "Atualização sobre sua candidatura para a vaga: " + tituloVaga;

        if (status == Status.INTERVIEW) {
            String corpoEmail = String.format(
                    "Olá, %s!\n\nÓtimas notícias! A empresa %s gostaria de te convidar para uma entrevista para a vaga de %s.\n\nA entrevista será realizada através do seguinte link: %s\n\nBoa sorte!\n\nAtenciosamente,\nEquipe Betwin Vagas",
                    nomeCandidato, nomeEmpresa, tituloVaga, linkEntrevista);
            emailService.sendEmail(emailCandidato, assunto, corpoEmail);
            attr.addFlashAttribute("sucess", "Status atualizado para ENTREVISTA e e-mail enviado ao candidato.");

        } else if (status == Status.NOT_SELECTED) {
            String corpoEmail = String.format(
                    "Olá, %s.\n\nAgradecemos seu interesse na vaga de %s na empresa %s. No momento, optamos por seguir com outros candidatos.\n\nAgradecemos sua participação e desejamos sucesso em sua busca por novas oportunidades.\n\nAtenciosamente,\nEquipe Betwin Vagas",
                    nomeCandidato, tituloVaga, nomeEmpresa);
            emailService.sendEmail(emailCandidato, assunto, corpoEmail);
            attr.addFlashAttribute("sucess", "Status atualizado para NÃO SELECIONADO e e-mail de notificação enviado.");

        } else {
            attr.addFlashAttribute("sucess", "Status da candidatura atualizado para " + status.name() + ".");
        }

        return "redirect:/candidaturas/gerenciar/" + candidatura.getVacancy().getId();
    }
}