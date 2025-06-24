package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Company;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private IEmpresaService service;

    @GetMapping("/cadastrar")
    public String cadastrar(Company empresa) {
        return "empresa/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("empresas", service.buscarTodos());
        return "empresa/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Company empresa, BindingResult result, RedirectAttributes attr) {

        service.validarCamposUnicos(empresa, result);

        if (result.hasErrors()) {
            return "empresa/cadastro";
        }

        service.salvar(empresa);
        attr.addFlashAttribute("sucess", "Company inserida com sucesso.");
        return "redirect:/empresas/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("empresa", service.buscarPorId(id));
        return "empresa/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Company empresa, BindingResult result, RedirectAttributes attr) {

        service.validarCamposUnicos(empresa, result);

        service.salvar(empresa);
        attr.addFlashAttribute("sucess", "Company editada com sucesso.");
        return "redirect:/empresas/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, ModelMap model, RedirectAttributes attr) {
        try {
            service.excluir(id);
            attr.addFlashAttribute("sucess", "Company excluída com sucesso.");
        } catch (Exception e) {
            return "redirect:/erro?msg=Não foi possível excluir a empresa. Verifique se ela possui vagas ativas.";
        }
        return "redirect:/empresas/listar";
    }
}