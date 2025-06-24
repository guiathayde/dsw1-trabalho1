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

import br.ufscar.dc.dsw.domain.Professional;
import br.ufscar.dc.dsw.domain.Gender;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private IProfissionalService profissionalService;

    @GetMapping("/cadastrar")
    public String cadastrar(Professional profissional, ModelMap model) {
        model.addAttribute("sexos", Gender.values());
        return "profissional/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("profissionais", profissionalService.buscarTodos());
        return "profissional/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Professional profissional, BindingResult result, RedirectAttributes attr,
            ModelMap model) {

        profissionalService.validarCamposUnicos(profissional, result);

        if (result.hasErrors()) {
            model.addAttribute("sexos", Gender.values());
            return "profissional/cadastro";
        }
        profissionalService.salvar(profissional);
        attr.addFlashAttribute("sucess", "Professional inserido com sucesso.");
        return "redirect:/profissionais/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("profissional", profissionalService.buscarPorId(id));
        model.addAttribute("sexos", Gender.values());
        return "profissional/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Professional profissional, BindingResult result, RedirectAttributes attr,
            ModelMap model) {
        profissionalService.validarCamposUnicos(profissional, result);
        if (result.hasErrors()) {
            model.addAttribute("sexos", Gender.values());
            return "profissional/cadastro";
        }
        profissionalService.salvar(profissional);

        attr.addFlashAttribute("sucess", "Professional editado com sucesso.");
        return "redirect:/profissionais/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        try {
            profissionalService.excluir(id);
            attr.addFlashAttribute("sucess", "Professional excluído com sucesso.");
        } catch (Exception e) {
            return "redirect:/erro?msg=Não foi possível excluir o profissional. Verifique as relações.";
        }
        return "redirect:/profissionais/listar";
    }
}