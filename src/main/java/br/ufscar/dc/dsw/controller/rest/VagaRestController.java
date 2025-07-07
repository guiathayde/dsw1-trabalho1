package br.ufscar.dc.dsw.controller.rest;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.service.spec.IVagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaRestController {

    @Autowired
    private IVagaService vagaService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Vaga> busca(@PathVariable Long id) {
        Vaga vaga = vagaService.buscarPorId(id);
        if (vaga == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vaga);
    }

    @GetMapping(path = "/empresas/{id}")
    public ResponseEntity<List<Vaga>> listaPorEmpresa(@PathVariable Long id) {
        List<Vaga> vagas = vagaService.buscarVagasAbertasPorEmpresa(id);
        if (vagas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vagas);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Vaga>> lista() {
        List<Vaga> vagas = vagaService.buscarTodos();
        return ResponseEntity.ok(vagas);
    }
}
