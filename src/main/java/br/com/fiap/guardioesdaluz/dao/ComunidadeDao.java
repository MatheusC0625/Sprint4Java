package br.com.fiap.guardioesdaluz.dao;

import br.com.fiap.guardioesdaluz.exception.EntidadeNaoEncontradaException;
import br.com.fiap.guardioesdaluz.factory.ConnectionFactory;
import br.com.fiap.guardioesdaluz.model.Categoria;
import br.com.fiap.guardioesdaluz.model.Comunidade;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ComunidadeDao {

    public Comunidade cadastrar(Comunidade comunidade) throws SQLException {
        String sql = "insert into COMUNIDADE (nome, tema, pontuacao_total) values (?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id_comunidade"})) {
            stmt.setString(1, comunidade.getNome());
            stmt.setString(2, comunidade.getTema().name());
            stmt.setInt(3, comunidade.getPontuacaoTotal());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    comunidade.setIdComunidade(chaves.getInt(1));
                }
            }
        }
        return comunidade;
    }

    public Comunidade buscarPorId(int id) throws SQLException {
        String sql = "select * from COMUNIDADE where id_comunidade = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntidadeNaoEncontradaException("Comunidade nao encontrada: id " + id);
                }
                return montarComunidade(resultSet);
            }
        }
    }

    public List<Comunidade> listar() throws SQLException {
        String sql = "select * from COMUNIDADE order by pontuacao_total desc";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            List<Comunidade> lista = new ArrayList<>();
            while (resultSet.next()) {
                lista.add(montarComunidade(resultSet));
            }
            return lista;
        }
    }

    public void atualizar(Comunidade comunidade) throws SQLException {
        String sql = "update COMUNIDADE set nome = ?, tema = ?, pontuacao_total = ? where id_comunidade = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, comunidade.getNome());
            stmt.setString(2, comunidade.getTema().name());
            stmt.setInt(3, comunidade.getPontuacaoTotal());
            stmt.setInt(4, comunidade.getIdComunidade());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Comunidade nao encontrada: id " + comunidade.getIdComunidade());
            }
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "delete from COMUNIDADE where id_comunidade = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Comunidade nao encontrada: id " + id);
            }
        }
    }

    private Comunidade montarComunidade(ResultSet resultSet) throws SQLException {
        Comunidade comunidade = new Comunidade(
                resultSet.getInt("id_comunidade"),
                resultSet.getString("nome"),
                Categoria.valueOf(resultSet.getString("tema")));
        comunidade.setPontuacaoTotal(resultSet.getInt("pontuacao_total"));
        Date dataCriacao = resultSet.getDate("data_criacao");
        if (dataCriacao != null) {
            comunidade.setDataCriacao(dataCriacao.toLocalDate());
        }
        return comunidade;
    }

}
