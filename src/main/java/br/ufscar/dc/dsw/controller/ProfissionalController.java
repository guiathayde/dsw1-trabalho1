package br.ufscar.dc.dsw.controller;

import jakarta.validation.Valid;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;

@Controller
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ICandidaturaService candidaturaService;

    @GetMapping("/cadastrar")
    public String cadastrar(Profissional profissional) {
        return "profissional/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        String url = "http://localhost:8080/api/profissionais";
        Profissional[] profissionais = restTemplate.getForObject(url, Profissional[].class);
        model.addAttribute("profissionais", Arrays.asList(profissionais));
        return "profissional/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Profissional profissional, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "profissional/cadastro";
        }

        String url = "http://localhost:8080/api/profissionais";
        try {
            restTemplate.postForObject(url, profissional, Profissional.class);
            attr.addFlashAttribute("sucess", "profissional.create.sucess");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                attr.addFlashAttribute("fail", "profissional.create.fail.cnpj");
            } else {
                attr.addFlashAttribute("fail", "profissional.create.fail");
            }
        }
        return "redirect:/profissionais/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        String url = "http://localhost:8080/api/profissionais/{id}";
        Profissional profissional = restTemplate.getForObject(url, Profissional.class, id);
        model.addAttribute("profissional", profissional);
        return "profissional/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Profissional profissional, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "profissional/cadastro";
        }

        String url = "http://localhost:8080/api/profissionais/{id}";
        try {
            restTemplate.put(url, profissional, profissional.getId());
            attr.addFlashAttribute("sucess", "profissional.edit.sucess");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                attr.addFlashAttribute("fail", "profissional.edit.fail.cnpj");
            } else {
                attr.addFlashAttribute("fail", "profissional.edit.fail");
            }
        }
        return "redirect:/profissionais/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        String url = "http://localhost:8080/api/profissionais/{id}";
        try {
            restTemplate.delete(url, id);
            attr.addFlashAttribute("sucess", "profissional.delete.sucess");
        } catch (HttpClientErrorException e) {
            attr.addFlashAttribute("fail", "profissional.delete.fail");
        }
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
