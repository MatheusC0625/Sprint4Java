package br.com.fiap.guardioesdaluz.resource;

import br.com.fiap.guardioesdaluz.bo.MissaoBO;
import br.com.fiap.guardioesdaluz.model.Missao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/missoes")
public class MissaoResource {

    private final MissaoBO missaoBO;

    public MissaoResource(MissaoBO missaoBO) {
        this.missaoBO = missaoBO;
    }

    @GetMapping
    public List<Missao> listar() throws SQLException {
        return missaoBO.listar();
    }

    @GetMapping("/{id}")
    public Missao buscarPorId(@PathVariable int id) throws SQLException {
        return missaoBO.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Missao> cadastrar(@RequestBody Missao missao) throws SQLException {
        Missao salva = missaoBO.cadastrar(missao);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    public Missao atualizar(@PathVariable int id, @RequestBody Missao missao) throws SQLException {
        return missaoBO.atualizar(id, missao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException {
        missaoBO.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
