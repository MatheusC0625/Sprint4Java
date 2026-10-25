package br.com.fiap.guardioesdaluz.resource;

import br.com.fiap.guardioesdaluz.bo.ComunidadeBO;
import br.com.fiap.guardioesdaluz.model.Comunidade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/comunidades")
public class ComunidadeResource {

    private final ComunidadeBO comunidadeBO;

    public ComunidadeResource(ComunidadeBO comunidadeBO) {
        this.comunidadeBO = comunidadeBO;
    }

    @GetMapping
    public List<Comunidade> listar() throws SQLException {
        return comunidadeBO.listar();
    }

    @GetMapping("/{id}")
    public Comunidade buscarPorId(@PathVariable int id) throws SQLException {
        return comunidadeBO.buscarPorId(id);
    }

    @GetMapping("/vencedoras")
    public List<Comunidade> vencedoras() throws SQLException {
        return comunidadeBO.calcularVencedoras();
    }

    @PostMapping
    public ResponseEntity<Comunidade> cadastrar(@RequestBody Comunidade comunidade) throws SQLException {
        Comunidade salva = comunidadeBO.cadastrar(comunidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    public Comunidade atualizar(@PathVariable int id, @RequestBody Comunidade comunidade) throws SQLException {
        return comunidadeBO.atualizar(id, comunidade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException {
        comunidadeBO.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
