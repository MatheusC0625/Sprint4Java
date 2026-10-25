package br.com.fiap.guardioesdaluz.resource;

import br.com.fiap.guardioesdaluz.bo.PontuacaoBO;
import br.com.fiap.guardioesdaluz.bo.UsuarioBO;
import br.com.fiap.guardioesdaluz.model.Pontuacao;
import br.com.fiap.guardioesdaluz.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pontuacoes")
public class PontuacaoResource {

    private final PontuacaoBO pontuacaoBO;
    private final UsuarioBO usuarioBO;

    public PontuacaoResource(PontuacaoBO pontuacaoBO, UsuarioBO usuarioBO) {
        this.pontuacaoBO = pontuacaoBO;
        this.usuarioBO = usuarioBO;
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pontuacao> extrato(@PathVariable int usuarioId) throws SQLException {
        return pontuacaoBO.listarExtrato(usuarioId);
    }

    @GetMapping("/usuario/{usuarioId}/saldo")
    public Map<String, Object> saldo(@PathVariable int usuarioId) throws SQLException {
        int saldo = pontuacaoBO.saldoAtual(usuarioId);
        return Map.of(
                "usuarioId", usuarioId,
                "saldoSoulCoins", saldo,
                "creditoReais", saldo / 100.0
        );
    }

    @PostMapping("/bonus-social")
    public ResponseEntity<Pontuacao> bonusSocial(@RequestBody BonusSocialRequest request) throws SQLException {
        Usuario usuario = usuarioBO.buscarPorId(request.usuarioId());
        Pontuacao gerada = pontuacaoBO.gerarBonusSocial(usuario, request.reacoes());
        return ResponseEntity.status(HttpStatus.CREATED).body(gerada);
    }

    @PostMapping("/ajuste")
    public ResponseEntity<Pontuacao> ajuste(@RequestBody AjusteRequest request) throws SQLException {
        Usuario usuario = usuarioBO.buscarPorId(request.usuarioId());
        Pontuacao gerada = pontuacaoBO.lancarAjuste(usuario, request.pontos());
        return ResponseEntity.status(HttpStatus.CREATED).body(gerada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException {
        pontuacaoBO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    public record BonusSocialRequest(Integer usuarioId, int reacoes) {
    }

    public record AjusteRequest(Integer usuarioId, int pontos) {
    }

}
