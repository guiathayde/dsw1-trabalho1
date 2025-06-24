package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Admin;
import br.ufscar.dc.dsw.service.spec.IAdministradorService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private IAdministradorService service;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder encoder;

    @GetMapping("/cadastrar")
    public String cadastrar(Admin administrador) {
        return "administrador/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("administradores", service.buscarTodos());
        return "administrador/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Admin administrador, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "administrador/cadastro";
        }
        if (administrador.getId() == null) {
            administrador.setPassword(encoder.encode(administrador.getPassword()));
        }
        service.salvar(administrador);
        attr.addFlashAttribute("sucess", "Admin inserido com sucesso.");
        return "redirect:/administradores/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("administrador", service.buscarPorId(id));
        return "administrador/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Admin administrador, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "administrador/cadastro";
        }
        Admin administradorExistente = service.buscarPorId(administrador.getId());
        if (administrador.getPassword() == null || administrador.getPassword().isEmpty()) {
            administrador.setPassword(administradorExistente.getPassword());
        } else {
            administrador.setPassword(encoder.encode(administrador.getPassword()));
        }
        service.salvar(administrador);
        attr.addFlashAttribute("sucess", "Admin editado com sucesso.");
        return "redirect:/administradores/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, ModelMap model) {
        service.excluir(id);
        model.addAttribute("sucess", "Admin excluído com sucesso.");
        return listar(model);
    }
}