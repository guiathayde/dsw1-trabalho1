package br.ufscar.dc.dsw.controller.rest;

import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.service.spec.ICandidaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candidaturas")
public class CandidaturaRestController {

    @Autowired
    private ICandidaturaService candidaturaService;

    @GetMapping(path = "/vagas/{id}")
    public ResponseEntity<List<Candidatura>> listaPorVaga(@PathVariable Long id) {
        List<Candidatura> candidaturas = candidaturaService.buscarCandidaturasPorVagaId(id);
        if (candidaturas == null || candidaturas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidaturas);
    }
}
