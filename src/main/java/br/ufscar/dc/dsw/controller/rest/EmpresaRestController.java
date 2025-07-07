package br.ufscar.dc.dsw.controller.rest;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaRestController {

    @Autowired
    private IEmpresaService empresaService;

    @GetMapping(path = "")
    public ResponseEntity<List<Empresa>> lista() {
        List<Empresa> empresas = empresaService.buscarTodos();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Empresa> busca(@PathVariable Long id) {
        Empresa empresa = empresaService.buscarPorId(id);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(empresa);
    }

    @GetMapping(path = "/cidades/{nome}")
    public ResponseEntity<List<Empresa>> listaPorCidade(@PathVariable String nome) {
        List<Empresa> empresas = empresaService.buscarPorCidade(nome);
        if (empresas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(empresas);
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Empresa> atualiza(@PathVariable Long id, @Valid @RequestBody Empresa empresa) {
        Empresa e = empresaService.buscarPorId(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            empresa.setId(id);
            empresa.setRole("ROLE_EMPRESA");
            empresaService.salvar(empresa);
            return ResponseEntity.ok(empresa);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(path = "")
    @ResponseBody
    public ResponseEntity<Empresa> cria(@Valid @RequestBody Empresa empresa) {
        try {
            empresa.setRole("ROLE_EMPRESA");
            empresaService.salvar(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> exclui(@PathVariable Long id) {
        Empresa empresa = empresaService.buscarPorId(id);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        empresaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
