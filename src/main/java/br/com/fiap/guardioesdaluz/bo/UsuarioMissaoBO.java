package br.com.fiap.guardioesdaluz.bo;

import br.com.fiap.guardioesdaluz.dao.MissaoDao;
import br.com.fiap.guardioesdaluz.dao.UsuarioDao;
import br.com.fiap.guardioesdaluz.dao.UsuarioMissaoDao;
import br.com.fiap.guardioesdaluz.exception.RegraDeNegocioException;
import br.com.fiap.guardioesdaluz.model.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioMissaoBO {

    private final UsuarioMissaoDao usuarioMissaoDao;
    private final UsuarioDao usuarioDao;
    private final MissaoDao missaoDao;
    private final PontuacaoBO pontuacaoBO;

    public UsuarioMissaoBO(UsuarioMissaoDao usuarioMissaoDao, UsuarioDao usuarioDao,
                            MissaoDao missaoDao, PontuacaoBO pontuacaoBO) {
        this.usuarioMissaoDao = usuarioMissaoDao;
        this.usuarioDao = usuarioDao;
        this.missaoDao = missaoDao;
        this.pontuacaoBO = pontuacaoBO;
    }

    public UsuarioMissao buscarPorId(int id) throws SQLException {
        return usuarioMissaoDao.buscarPorId(id);
    }

    public List<UsuarioMissao> listar() throws SQLException {
        return usuarioMissaoDao.listar();
    }

    public List<UsuarioMissao> listarPorUsuario(int usuarioId) throws SQLException {
        return usuarioMissaoDao.listarPorUsuario(usuarioId);
    }

    public void deletar(int id) throws SQLException {
        usuarioMissaoDao.deletar(id);
    }

    public UsuarioMissao registrarExecucao(int usuarioId, int missaoId, LocalDate dataConclusao) throws SQLException {
        Missao missao = missaoDao.buscarPorId(missaoId);
        if (!missao.isAtiva()) {
            throw new RegraDeNegocioException("Missao inativa: " + missao.getTitulo());
        }
        Usuario usuario = usuarioDao.buscarPorId(usuarioId);

        validarRegraDeRepeticao(usuario, missao, dataConclusao);

        UsuarioMissao execucao = new UsuarioMissao(null, usuario, missao, dataConclusao);
        execucao.setStatus(missao.getTipoMissao() == TipoMissao.CAMPO
                ? StatusExecucao.PENDENTE
                : StatusExecucao.VALIDADA);

        usuarioMissaoDao.cadastrar(execucao);

        if (execucao.getStatus() == StatusExecucao.VALIDADA) {
            gerarRecompensa(execucao);
        }

        return execucao;
    }

    public UsuarioMissao avaliarExecucaoDeCampo(int idExecucao, boolean aprovada) throws SQLException {
        UsuarioMissao execucao = usuarioMissaoDao.buscarPorId(idExecucao);

        if (execucao.getMissao().getTipoMissao() != TipoMissao.CAMPO) {
            throw new RegraDeNegocioException("Somente missoes do tipo CAMPO passam por validacao manual");
        }
        if (execucao.getStatus() != StatusExecucao.PENDENTE) {
            throw new RegraDeNegocioException("Esta execucao ja foi avaliada anteriormente");
        }

        execucao.setStatus(aprovada ? StatusExecucao.VALIDADA : StatusExecucao.REJEITADA);
        usuarioMissaoDao.atualizarStatus(execucao);

        if (aprovada) {
            gerarRecompensa(execucao);
        }

        return execucao;
    }

    private void gerarRecompensa(UsuarioMissao execucao) throws SQLException {
        int streakAtual = execucao.getMissao().getTipoMissao() == TipoMissao.DIARIA
                ? calcularStreakAtual(execucao.getUsuario().getIdUsuario(), execucao.getMissao().getIdMissao())
                : 0;
        int pontos = execucao.getMissao().calcularRecompensa(streakAtual);
        pontuacaoBO.gerarPontuacaoPorMissao(execucao, pontos);
    }

    private void validarRegraDeRepeticao(Usuario usuario, Missao missao, LocalDate dataConclusao) throws SQLException {
        switch (missao.getTipoMissao()) {
            case ONBOARDING -> {
                boolean jaConcluida = usuarioMissaoDao.contarExecucoes(
                        usuario.getIdUsuario(), missao.getIdMissao(), StatusExecucao.VALIDADA) > 0;
                if (jaConcluida) {
                    throw new RegraDeNegocioException("Missao de onboarding ja foi concluida por este usuario");
                }
            }
            case DIARIA -> {
                boolean jaFeitaHoje = usuarioMissaoDao.existeExecucaoNoDia(
                        usuario.getIdUsuario(), missao.getIdMissao(), dataConclusao);
                if (jaFeitaHoje) {
                    throw new RegraDeNegocioException("Missao diaria ja registrada nesta data para este usuario");
                }
            }
            default -> { }
        }
    }

    private int calcularStreakAtual(int usuarioId, int missaoId) throws SQLException {
        List<LocalDate> datas = usuarioMissaoDao.listarDatasValidadas(usuarioId, missaoId);
        if (datas.isEmpty()) {
            return 0;
        }

        int streak = 1;
        for (int i = datas.size() - 1; i > 0; i--) {
            if (datas.get(i).minusDays(1).isEqual(datas.get(i - 1))) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

}
