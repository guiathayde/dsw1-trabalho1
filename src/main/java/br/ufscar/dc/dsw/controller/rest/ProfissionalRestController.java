package br.ufscar.dc.dsw.controller.rest;

import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalRestController {

    @Autowired
    private IProfissionalService profissionalService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    

    @GetMapping(path = "")
    public ResponseEntity<List<Profissional>> lista() {
        List<Profissional> profissionais = profissionalService.buscarTodos();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Profissional> busca(@PathVariable Long id) {
        Profissional profissional = profissionalService.buscarPorId(id);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profissional);
    }

    @PostMapping(path = "")
    @ResponseBody
    public ResponseEntity<Profissional> cria(@Valid @RequestBody Profissional profissional) {
        try {
            profissional.setPassword(encoder.encode(profissional.getPassword()));
            profissional.setRole("ROLE_PROFISSIONAL");
            profissionalService.salvar(profissional);
            return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Profissional> atualiza(@PathVariable Long id, @Valid @RequestBody Profissional profissional) {
        Profissional p = profissionalService.buscarPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            profissional.setId(id);
            profissional.setRole("ROLE_PROFISSIONAL");
            if (profissional.getPassword() == null || profissional.getPassword().isEmpty()) {
                profissional.setPassword(p.getPassword());
            } else {
                profissional.setPassword(encoder.encode(profissional.getPassword()));
            }
            profissionalService.salvar(profissional);
            return ResponseEntity.ok(profissional);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> exclui(@PathVariable Long id) {
        Profissional profissional = profissionalService.buscarPorId(id);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }
        profissionalService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
