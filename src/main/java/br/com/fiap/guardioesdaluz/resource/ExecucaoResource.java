package br.com.fiap.guardioesdaluz.resource;

import br.com.fiap.guardioesdaluz.bo.UsuarioMissaoBO;
import br.com.fiap.guardioesdaluz.model.UsuarioMissao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/execucoes")
public class ExecucaoResource {

    private final UsuarioMissaoBO usuarioMissaoBO;

    public ExecucaoResource(UsuarioMissaoBO usuarioMissaoBO) {
        this.usuarioMissaoBO = usuarioMissaoBO;
    }

    @GetMapping
    public List<UsuarioMissao> listar() throws SQLException {
        return usuarioMissaoBO.listar();
    }

    @GetMapping("/{id}")
    public UsuarioMissao buscarPorId(@PathVariable int id) throws SQLException {
        return usuarioMissaoBO.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<UsuarioMissao> listarPorUsuario(@PathVariable int usuarioId) throws SQLException {
        return usuarioMissaoBO.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<UsuarioMissao> registrar(@RequestBody RegistrarExecucaoRequest request) throws SQLException {
        LocalDate data = request.dataConclusao() != null ? request.dataConclusao() : LocalDate.now();
        UsuarioMissao execucao = usuarioMissaoBO.registrarExecucao(request.usuarioId(), request.missaoId(), data);
        return ResponseEntity.status(HttpStatus.CREATED).body(execucao);
    }

    @PutMapping("/{id}/avaliar")
    public UsuarioMissao avaliar(@PathVariable int id, @RequestParam boolean aprovada) throws SQLException {
        return usuarioMissaoBO.avaliarExecucaoDeCampo(id, aprovada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException {
        usuarioMissaoBO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    public record RegistrarExecucaoRequest(Integer usuarioId, Integer missaoId, LocalDate dataConclusao) {
    }

}
