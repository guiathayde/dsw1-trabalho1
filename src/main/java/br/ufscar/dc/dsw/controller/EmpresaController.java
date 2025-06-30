package br.ufscar.dc.dsw.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private IEmpresaService service;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/cadastrar")
    public String cadastrar(Empresa empresa) {
        return "empresa/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("empresas", service.buscarTodos());
        return "empresa/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Empresa empresa, BindingResult result, RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("empresa", empresa);
            return "empresa/cadastro";
        }

        empresa.setPassword(encoder.encode(empresa.getPassword()));
        service.salvar(empresa);
        attr.addFlashAttribute("sucess", "empresa.create.sucess");
        return "redirect:/empresas/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("empresa", service.buscarPorId(id));
        return "empresa/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Empresa empresa, BindingResult result, RedirectAttributes attr, ModelMap model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("empresa", empresa);
            return "empresa/cadastro";
        }

        service.salvar(empresa);
        attr.addFlashAttribute("sucess", "empresa.edit.sucess");
        return "redirect:/empresas/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        service.excluir(id);
        attr.addFlashAttribute("sucess", "empresa.delete.sucess");
        return "redirect:/empresas/listar";
    }
}
