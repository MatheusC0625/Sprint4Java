package br.com.fiap.guardioesdaluz.dao;

import br.com.fiap.guardioesdaluz.exception.EntidadeNaoEncontradaException;
import br.com.fiap.guardioesdaluz.factory.ConnectionFactory;
import br.com.fiap.guardioesdaluz.model.Categoria;
import br.com.fiap.guardioesdaluz.model.Missao;
import br.com.fiap.guardioesdaluz.model.TipoMissao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MissaoDao {

    public Missao cadastrar(Missao missao) throws SQLException {
        String sql = "insert into MISSAO (titulo, descricao, categoria, tipo_missao, recompensa_base, ativa) " +
                "values (?, ?, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id_missao"})) {
            stmt.setString(1, missao.getTitulo());
            stmt.setString(2, missao.getDescricao());
            stmt.setString(3, missao.getCategoria().name());
            stmt.setString(4, missao.getTipoMissao().name());
            stmt.setInt(5, missao.getRecompensaBase());
            stmt.setString(6, missao.isAtiva() ? "S" : "N");
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    missao.setIdMissao(chaves.getInt(1));
                }
            }
        }
        return missao;
    }

    public Missao buscarPorId(int id) throws SQLException {
        String sql = "select * from MISSAO where id_missao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntidadeNaoEncontradaException("Missao nao encontrada: id " + id);
                }
                return montarMissao(resultSet);
            }
        }
    }

    public List<Missao> listar() throws SQLException {
        String sql = "select * from MISSAO order by id_missao";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            List<Missao> lista = new ArrayList<>();
            while (resultSet.next()) {
                lista.add(montarMissao(resultSet));
            }
            return lista;
        }
    }

    public void atualizar(Missao missao) throws SQLException {
        String sql = "update MISSAO set titulo = ?, descricao = ?, categoria = ?, tipo_missao = ?, " +
                "recompensa_base = ?, ativa = ? where id_missao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, missao.getTitulo());
            stmt.setString(2, missao.getDescricao());
            stmt.setString(3, missao.getCategoria().name());
            stmt.setString(4, missao.getTipoMissao().name());
            stmt.setInt(5, missao.getRecompensaBase());
            stmt.setString(6, missao.isAtiva() ? "S" : "N");
            stmt.setInt(7, missao.getIdMissao());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Missao nao encontrada: id " + missao.getIdMissao());
            }
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "delete from MISSAO where id_missao = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Missao nao encontrada: id " + id);
            }
        }
    }

    private Missao montarMissao(ResultSet resultSet) throws SQLException {
        Missao missao = new Missao(
                resultSet.getInt("id_missao"),
                resultSet.getString("titulo"),
                resultSet.getString("descricao"),
                Categoria.valueOf(resultSet.getString("categoria")),
                TipoMissao.valueOf(resultSet.getString("tipo_missao")),
                resultSet.getInt("recompensa_base"));
        missao.setAtiva("S".equals(resultSet.getString("ativa")));
        return missao;
    }

}
