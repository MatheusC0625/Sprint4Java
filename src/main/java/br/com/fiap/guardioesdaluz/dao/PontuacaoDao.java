package br.com.fiap.guardioesdaluz.dao;

import br.com.fiap.guardioesdaluz.exception.EntidadeNaoEncontradaException;
import br.com.fiap.guardioesdaluz.factory.ConnectionFactory;
import br.com.fiap.guardioesdaluz.model.OrigemPontuacao;
import br.com.fiap.guardioesdaluz.model.Pontuacao;
import br.com.fiap.guardioesdaluz.model.StatusPontuacao;
import br.com.fiap.guardioesdaluz.model.Usuario;
import br.com.fiap.guardioesdaluz.model.UsuarioMissao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PontuacaoDao {

    private final UsuarioDao usuarioDao;
    private final UsuarioMissaoDao usuarioMissaoDao;

    public PontuacaoDao(UsuarioDao usuarioDao, UsuarioMissaoDao usuarioMissaoDao) {
        this.usuarioDao = usuarioDao;
        this.usuarioMissaoDao = usuarioMissaoDao;
    }

    public Pontuacao cadastrar(Pontuacao pontuacao) throws SQLException {
        String sql = "insert into PONTUACAO (id_usuario, id_execucao, origem, pontos, data_expiracao, status) " +
                "values (?, ?, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id_pontuacao"})) {
            stmt.setInt(1, pontuacao.getUsuario().getIdUsuario());
            if (pontuacao.getExecucao() != null) {
                stmt.setInt(2, pontuacao.getExecucao().getIdExecucao());
            } else {
                stmt.setNull(2, Types.NUMERIC);
            }
            stmt.setString(3, pontuacao.getOrigem().name());
            stmt.setInt(4, pontuacao.getPontos());
            stmt.setDate(5, Date.valueOf(pontuacao.getDataExpiracao()));
            stmt.setString(6, pontuacao.getStatus().name());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    pontuacao.setIdPontuacao(chaves.getInt(1));
                }
            }
        }
        return pontuacao;
    }

    public Pontuacao buscarPorId(int id) throws SQLException {
        String sql = "select * from PONTUACAO where id_pontuacao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntidadeNaoEncontradaException("Lancamento de pontuacao nao encontrado: id " + id);
                }
                return montarPontuacao(resultSet);
            }
        }
    }

    public List<Pontuacao> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "select * from PONTUACAO where id_usuario = ? order by data_geracao desc";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                List<Pontuacao> lista = new ArrayList<>();
                while (resultSet.next()) {
                    lista.add(montarPontuacao(resultSet));
                }
                return lista;
            }
        }
    }

    public List<Pontuacao> listar() throws SQLException {
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("select * from PONTUACAO order by id_pontuacao");
             ResultSet resultSet = stmt.executeQuery()) {

            List<Pontuacao> lista = new ArrayList<>();
            while (resultSet.next()) {
                lista.add(montarPontuacao(resultSet));
            }
            return lista;
        }
    }

    public void atualizarStatus(Pontuacao pontuacao) throws SQLException {
        String sql = "update PONTUACAO set status = ? where id_pontuacao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, pontuacao.getStatus().name());
            stmt.setInt(2, pontuacao.getIdPontuacao());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Lancamento de pontuacao nao encontrado: id " + pontuacao.getIdPontuacao());
            }
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "delete from PONTUACAO where id_pontuacao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Lancamento de pontuacao nao encontrado: id " + id);
            }
        }
    }

    public int somarSaldoAtivo(int usuarioId) throws SQLException {
        String sql = "select nvl(sum(pontos), 0) from PONTUACAO where id_usuario = ? and status = 'ATIVO'";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }

    private Pontuacao montarPontuacao(ResultSet resultSet) throws SQLException {
        Usuario usuario = usuarioDao.buscarPorId(resultSet.getInt("id_usuario"));

        Pontuacao pontuacao = new Pontuacao(
                resultSet.getInt("id_pontuacao"),
                usuario,
                OrigemPontuacao.valueOf(resultSet.getString("origem")),
                resultSet.getInt("pontos"));

        int idExecucao = resultSet.getInt("id_execucao");
        if (!resultSet.wasNull()) {
            UsuarioMissao execucao = usuarioMissaoDao.buscarPorId(idExecucao);
            pontuacao.setExecucao(execucao);
        }

        pontuacao.setDataGeracao(resultSet.getDate("data_geracao").toLocalDate());
        pontuacao.setDataExpiracao(resultSet.getDate("data_expiracao").toLocalDate());
        pontuacao.setStatus(StatusPontuacao.valueOf(resultSet.getString("status")));
        return pontuacao;
    }

}
