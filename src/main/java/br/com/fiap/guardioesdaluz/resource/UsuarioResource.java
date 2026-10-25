package br.com.fiap.guardioesdaluz.resource;

import br.com.fiap.guardioesdaluz.bo.UsuarioBO;
import br.com.fiap.guardioesdaluz.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private final UsuarioBO usuarioBO;

    public UsuarioResource(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }

    @GetMapping
    public List<Usuario> listar() throws SQLException {
        return usuarioBO.listar();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable int id) throws SQLException {
        return usuarioBO.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws SQLException {
        Usuario salvo = usuarioBO.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable int id, @RequestBody Usuario usuario) throws SQLException {
        return usuarioBO.atualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException {
        usuarioBO.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
