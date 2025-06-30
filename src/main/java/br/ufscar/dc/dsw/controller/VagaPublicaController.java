package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;
import br.ufscar.dc.dsw.service.spec.IVagaService;

@Controller
@RequestMapping("/vagasPublicas")
public class VagaPublicaController {

    @Autowired
    private IVagaService vagaService;

    @Autowired
    private ICandidaturaService candidaturaService;

    private static String UPLOAD_DIR = "src/main/resources/static/curriculos/";

    @GetMapping("/listar")
    public String listar(ModelMap model, @RequestParam(required = false) String cidade) {
        List<Vaga> vagas;
        if (cidade != null && !cidade.isEmpty()) {
            vagas = vagaService.buscarVagasAbertasPorCidade(cidade);
        } else {
            vagas = vagaService.buscarVagasAbertas();
        }
        model.addAttribute("vagas", vagas);
        return "vaga/listaPublica";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable("id") Long id, ModelMap model) {
        Vaga vaga = vagaService.buscarPorId(id);
        model.addAttribute("vaga", vaga);
        return "vaga/detalhes";
    }

    @PostMapping("/candidatar/{id}")
    public String candidatar(@PathVariable("id") Long id, @RequestParam("curriculo") MultipartFile curriculo,
            RedirectAttributes attr) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UsuarioDetails)) {
            attr.addFlashAttribute("fail", "Para se candidatar, você precisa estar logado como profissional.");
            return "redirect:/login";
        }

        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFISSIONAL"))) {
            attr.addFlashAttribute("fail", "Apenas profissionais podem se candidatar a vagas.");
            return "redirect:/vagasPublicas/listar";
        }

        Profissional profissional = (Profissional) userDetails.getUsuario();
        Vaga vaga = vagaService.buscarPorId(id);

        if (vaga == null) {
            attr.addFlashAttribute("fail", "Vaga não encontrada.");
            return "redirect:/vagasPublicas/listar";
        }

        // Verifica se o profissional já se candidatou a esta vaga
        Candidatura candidaturaExistente = candidaturaService.buscarPorProfissionalEVaga(profissional, vaga);
        if (candidaturaExistente != null) {
            attr.addFlashAttribute("fail", "vaga.error.alreadyApplied");
            return "redirect:/vagasPublicas/listar";
        }

        // Verifica o tipo de arquivo do currículo
        if (!"application/pdf".equals(curriculo.getContentType())) {
            attr.addFlashAttribute("fail", "curriculo.error.invalidFormat");
            return "redirect:/vagasPublicas/detalhes/" + id;
        }

        // Salva o currículo
        String fileName = profissional.getCPF() + "_" + vaga.getId() + ".pdf";
        Path path = Paths.get(UPLOAD_DIR + fileName);
        try {
            Files.write(path, curriculo.getBytes());
        } catch (IOException e) {
            attr.addFlashAttribute("fail", "Erro ao fazer upload do currículo: " + e.getMessage());
            return "redirect:/vagasPublicas/listar";
        }

        // Cria e salva a candidatura
        Candidatura candidatura = new Candidatura();
        candidatura.setProfissional(profissional);
        candidatura.setVaga(vaga);
        candidatura.setCurriculoPath(fileName);
        candidatura.setStatus("ABERTO"); // Status inicial da candidatura

        candidaturaService.salvar(candidatura);

        attr.addFlashAttribute("sucess", "vaga.apply.success");
        return "redirect:/vagasPublicas/listar";
    }
}
