package br.com.fiap.guardioesdaluz.bo;

import br.com.fiap.guardioesdaluz.dao.ComunidadeDao;
import br.com.fiap.guardioesdaluz.dao.PontuacaoDao;
import br.com.fiap.guardioesdaluz.exception.RegraDeNegocioException;
import br.com.fiap.guardioesdaluz.model.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PontuacaoBO {

    private static final int VALIDADE_EM_DIAS = 60;
    private static final int REACOES_POR_BLOCO = 10;
    private static final int PONTOS_POR_BLOCO = 5;

    private final PontuacaoDao pontuacaoDao;
    private final ComunidadeDao comunidadeDao;

    public PontuacaoBO(PontuacaoDao pontuacaoDao, ComunidadeDao comunidadeDao) {
        this.pontuacaoDao = pontuacaoDao;
        this.comunidadeDao = comunidadeDao;
    }

    public List<Pontuacao> listarExtrato(int usuarioId) throws SQLException {
        return pontuacaoDao.listarPorUsuario(usuarioId);
    }

    public int saldoAtual(int usuarioId) throws SQLException {
        return pontuacaoDao.somarSaldoAtivo(usuarioId);
    }

    public Pontuacao gerarPontuacaoPorMissao(UsuarioMissao execucao, int pontos) throws SQLException {
        Pontuacao pontuacao = new Pontuacao(null, execucao.getUsuario(), OrigemPontuacao.MISSAO, pontos);
        pontuacao.setExecucao(execucao);
        pontuacao.setDataGeracao(LocalDate.now());
        pontuacao.setDataExpiracao(LocalDate.now().plusDays(VALIDADE_EM_DIAS));
        pontuacao.setStatus(StatusPontuacao.ATIVO);
        pontuacaoDao.cadastrar(pontuacao);

        Comunidade comunidade = execucao.getUsuario().getComunidade();
        comunidade.adicionarPontos(pontos);
        comunidadeDao.atualizar(comunidade);

        return pontuacao;
    }

    public Pontuacao gerarBonusSocial(Usuario usuario, int reacoes) throws SQLException {
        if (reacoes < 0) {
            throw new RegraDeNegocioException("Numero de reacoes nao pode ser negativo");
        }
        int pontos = (reacoes / REACOES_POR_BLOCO) * PONTOS_POR_BLOCO;
        if (pontos <= 0) {
            throw new RegraDeNegocioException("Reacoes insuficientes para gerar bonus social (minimo " + REACOES_POR_BLOCO + ")");
        }

        Pontuacao pontuacao = new Pontuacao(null, usuario, OrigemPontuacao.BONUS_SOCIAL, pontos);
        pontuacao.setDataGeracao(LocalDate.now());
        pontuacao.setDataExpiracao(LocalDate.now().plusDays(VALIDADE_EM_DIAS));
        pontuacao.setStatus(StatusPontuacao.ATIVO);
        return pontuacaoDao.cadastrar(pontuacao);
    }

    public Pontuacao lancarAjuste(Usuario usuario, int pontos) throws SQLException {
        if (pontos <= 0 || pontos > 1000) {
            throw new RegraDeNegocioException("Ajuste deve ser um valor entre 1 e 1000 SoulCoins");
        }
        Pontuacao pontuacao = new Pontuacao(null, usuario, OrigemPontuacao.AJUSTE, pontos);
        pontuacao.setDataGeracao(LocalDate.now());
        pontuacao.setDataExpiracao(LocalDate.now().plusDays(VALIDADE_EM_DIAS));
        pontuacao.setStatus(StatusPontuacao.ATIVO);
        return pontuacaoDao.cadastrar(pontuacao);
    }

    public void deletar(int id) throws SQLException {
        pontuacaoDao.deletar(id);
    }

}
