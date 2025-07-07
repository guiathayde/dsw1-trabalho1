package br.ufscar.dc.dsw.controller;

import jakarta.validation.Valid;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/cadastrar")
    public String cadastrar(Empresa empresa) {
        return "empresa/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        String url = "http://localhost:8080/api/empresas";
        Empresa[] empresas = restTemplate.getForObject(url, Empresa[].class);
        model.addAttribute("empresas", Arrays.asList(empresas));
        return "empresa/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Empresa empresa, BindingResult result, RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("empresa", empresa);
            return "empresa/cadastro";
        }

        String url = "http://localhost:8080/api/empresas";
        try {
            restTemplate.postForObject(url, empresa, Empresa.class);
            attr.addFlashAttribute("sucess", "empresa.create.sucess");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                attr.addFlashAttribute("fail", "empresa.create.fail.cnpj");
            } else {
                attr.addFlashAttribute("fail", "empresa.create.fail");
            }
        }
        return "redirect:/empresas/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        String url = "http://localhost:8080/api/empresas/{id}";
        Empresa empresa = restTemplate.getForObject(url, Empresa.class, id);
        model.addAttribute("empresa", empresa);
        return "empresa/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Empresa empresa, BindingResult result, RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("empresa", empresa);
            return "empresa/cadastro";
        }

        String url = "http://localhost:8080/api/empresas/{id}";
        try {
            restTemplate.put(url, empresa, empresa.getId());
            attr.addFlashAttribute("sucess", "empresa.edit.sucess");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                attr.addFlashAttribute("fail", "empresa.edit.fail.cnpj");
            } else {
                attr.addFlashAttribute("fail", "empresa.edit.fail");
            }
        }
        return "redirect:/empresas/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        String url = "http://localhost:8080/api/empresas/{id}";
        try {
            restTemplate.delete(url, id);
            attr.addFlashAttribute("sucess", "empresa.delete.sucess");
        } catch (HttpClientErrorException e) {
            attr.addFlashAttribute("fail", "empresa.delete.fail");
        }
        return "redirect:/empresas/listar";
    }
}
