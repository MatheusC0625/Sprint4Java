package br.com.fiap.guardioesdaluz.dao;

import br.com.fiap.guardioesdaluz.exception.EntidadeNaoEncontradaException;
import br.com.fiap.guardioesdaluz.factory.ConnectionFactory;
import br.com.fiap.guardioesdaluz.model.Missao;
import br.com.fiap.guardioesdaluz.model.StatusExecucao;
import br.com.fiap.guardioesdaluz.model.Usuario;
import br.com.fiap.guardioesdaluz.model.UsuarioMissao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioMissaoDao {

    private final UsuarioDao usuarioDao;
    private final MissaoDao missaoDao;

    public UsuarioMissaoDao(UsuarioDao usuarioDao, MissaoDao missaoDao) {
        this.usuarioDao = usuarioDao;
        this.missaoDao = missaoDao;
    }

    public UsuarioMissao cadastrar(UsuarioMissao execucao) throws SQLException {
        String sql = "insert into USUARIO_MISSAO (id_usuario, id_missao, data_conclusao, status) " +
                "values (?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id_execucao"})) {
            stmt.setInt(1, execucao.getUsuario().getIdUsuario());
            stmt.setInt(2, execucao.getMissao().getIdMissao());
            stmt.setDate(3, Date.valueOf(execucao.getDataConclusao()));
            stmt.setString(4, execucao.getStatus().name());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    execucao.setIdExecucao(chaves.getInt(1));
                }
            }
        }
        return execucao;
    }

    public UsuarioMissao buscarPorId(int id) throws SQLException {
        String sql = "select * from USUARIO_MISSAO where id_execucao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntidadeNaoEncontradaException("Execucao de missao nao encontrada: id " + id);
                }
                return montarExecucao(resultSet);
            }
        }
    }

    public List<UsuarioMissao> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "select * from USUARIO_MISSAO where id_usuario = ? order by data_conclusao desc";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                List<UsuarioMissao> lista = new ArrayList<>();
                while (resultSet.next()) {
                    lista.add(montarExecucao(resultSet));
                }
                return lista;
            }
        }
    }

    public List<UsuarioMissao> listar() throws SQLException {
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("select * from USUARIO_MISSAO order by id_execucao");
             ResultSet resultSet = stmt.executeQuery()) {

            List<UsuarioMissao> lista = new ArrayList<>();
            while (resultSet.next()) {
                lista.add(montarExecucao(resultSet));
            }
            return lista;
        }
    }

    public void atualizarStatus(UsuarioMissao execucao) throws SQLException {
        String sql = "update USUARIO_MISSAO set status = ? where id_execucao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, execucao.getStatus().name());
            stmt.setInt(2, execucao.getIdExecucao());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Execucao de missao nao encontrada: id " + execucao.getIdExecucao());
            }
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "delete from USUARIO_MISSAO where id_execucao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Execucao de missao nao encontrada: id " + id);
            }
        }
    }

    public int contarExecucoes(int usuarioId, int missaoId, StatusExecucao status) throws SQLException {
        String sql = "select count(*) from USUARIO_MISSAO where id_usuario = ? and id_missao = ? and status = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, missaoId);
            stmt.setString(3, status.name());
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }

    public boolean existeExecucaoNoDia(int usuarioId, int missaoId, java.time.LocalDate data) throws SQLException {
        String sql = "select count(*) from USUARIO_MISSAO " +
                "where id_usuario = ? and id_missao = ? and data_conclusao = ? and status <> 'REJEITADA'";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, missaoId);
            stmt.setDate(3, Date.valueOf(data));
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    public List<java.time.LocalDate> listarDatasValidadas(int usuarioId, int missaoId) throws SQLException {
        String sql = "select data_conclusao from USUARIO_MISSAO " +
                "where id_usuario = ? and id_missao = ? and status = 'VALIDADA' order by data_conclusao asc";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, missaoId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                List<java.time.LocalDate> datas = new ArrayList<>();
                while (resultSet.next()) {
                    datas.add(resultSet.getDate("data_conclusao").toLocalDate());
                }
                return datas;
            }
        }
    }

    private UsuarioMissao montarExecucao(ResultSet resultSet) throws SQLException {
        Usuario usuario = usuarioDao.buscarPorId(resultSet.getInt("id_usuario"));
        Missao missao = missaoDao.buscarPorId(resultSet.getInt("id_missao"));

        UsuarioMissao execucao = new UsuarioMissao(
                resultSet.getInt("id_execucao"),
                usuario,
                missao,
                resultSet.getDate("data_conclusao").toLocalDate());
        execucao.setStatus(StatusExecucao.valueOf(resultSet.getString("status")));
        return execucao;
    }

}
