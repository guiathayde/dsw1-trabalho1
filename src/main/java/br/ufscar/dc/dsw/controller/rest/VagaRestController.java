package br.ufscar.dc.dsw.controller.rest;

import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.service.spec.IVagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaRestController {

    @Autowired
    private IVagaService vagaService;

    @PostMapping(path = "")
    @ResponseBody
    public ResponseEntity<Vaga> cria(@Valid @RequestBody Vaga vaga) {
        try {
            vagaService.salvar(vaga);
            return ResponseEntity.status(HttpStatus.CREATED).body(vaga);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Vaga> atualiza(@PathVariable Long id, @Valid @RequestBody Vaga vaga) {
        try {
            Vaga vagaExistente = vagaService.buscarPorId(id);
            if (vagaExistente == null) {
                return ResponseEntity.notFound().build();
            }
            vaga.setId(id);
            vagaService.salvar(vaga);
            return ResponseEntity.ok(vaga);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> exclui(@PathVariable Long id) {
        try {
            Vaga vaga = vagaService.buscarPorId(id);
            if (vaga == null) {
                return ResponseEntity.notFound().build();
            }
            vagaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

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
