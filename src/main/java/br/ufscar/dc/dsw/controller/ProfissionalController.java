package br.ufscar.dc.dsw.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;

@Controller
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private IProfissionalService service;

    @Autowired
    private ICandidaturaService candidaturaService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/cadastrar")
    public String cadastrar(Profissional profissional) {
        return "profissional/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("profissionais", service.buscarTodos());
        return "profissional/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Profissional profissional, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "profissional/cadastro";
        }

        profissional.setRole("ROLE_PROFISSIONAL");
        profissional.setPassword(encoder.encode(profissional.getPassword()));
        service.salvar(profissional);
        attr.addFlashAttribute("sucess", "profissional.create.sucess");
        return "redirect:/profissionais/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("profissional", service.buscarPorId(id));
        return "profissional/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Profissional profissional, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "profissional/cadastro";
        }

        service.salvar(profissional);
        attr.addFlashAttribute("sucess", "profissional.edit.sucess");
        return "redirect:/profissionais/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        service.excluir(id);
        attr.addFlashAttribute("sucess", "profissional.delete.sucess");
        return "redirect:/profissionais/listar";
    }

    @GetMapping("/minhasCandidaturas")
    public String listarMinhasCandidaturas(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
        Profissional profissional = (Profissional) userDetails.getUsuario();
        model.addAttribute("candidaturas", candidaturaService.buscarPorProfissional(profissional));
        return "profissional/minhasCandidaturas";
    }
}
