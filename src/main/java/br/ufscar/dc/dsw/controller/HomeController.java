package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.service.spec.IVagaService;

@Controller
public class HomeController {

    @Autowired
    private IVagaService vagaService;

    @GetMapping("/home")
    public String home(ModelMap model, @RequestParam(required = false) String cidade) {
        List<Vaga> vagas;
        if (cidade != null && !cidade.isEmpty()) {
            vagas = vagaService.buscarVagasAbertasPorCidade(cidade);
        } else {
            vagas = vagaService.buscarVagasAbertas();
        }
        model.addAttribute("vagas", vagas);
        return "home";
    }
}
