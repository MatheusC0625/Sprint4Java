package br.com.fiap.guardioesdaluz.dao;

import br.com.fiap.guardioesdaluz.exception.EntidadeNaoEncontradaException;
import br.com.fiap.guardioesdaluz.factory.ConnectionFactory;
import br.com.fiap.guardioesdaluz.model.Categoria;
import br.com.fiap.guardioesdaluz.model.Comunidade;
import br.com.fiap.guardioesdaluz.model.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDao {

    private static final String SELECT_BASE =
            "select u.id_usuario, u.nome, u.email, u.data_cadastro, " +
                    "c.id_comunidade, c.nome as comunidade_nome, c.tema, c.pontuacao_total, c.data_criacao " +
                    "from USUARIO u join COMUNIDADE c on c.id_comunidade = u.id_comunidade ";

    public Usuario cadastrar(Usuario usuario) throws SQLException {
        String sql = "insert into USUARIO (nome, email, id_comunidade) values (?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"id_usuario"})) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getComunidade().getIdComunidade());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    usuario.setIdUsuario(chaves.getInt(1));
                }
            }
        }
        return usuario;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "where u.id_usuario = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntidadeNaoEncontradaException("Usuario nao encontrado: id " + id);
                }
                return montarUsuario(resultSet);
            }
        }
    }

    public List<Usuario> listar() throws SQLException {
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_BASE + "order by u.id_usuario");
             ResultSet resultSet = stmt.executeQuery()) {

            List<Usuario> lista = new ArrayList<>();
            while (resultSet.next()) {
                lista.add(montarUsuario(resultSet));
            }
            return lista;
        }
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "update USUARIO set nome = ?, email = ?, id_comunidade = ? where id_usuario = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getComunidade().getIdComunidade());
            stmt.setInt(4, usuario.getIdUsuario());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuario nao encontrado: id " + usuario.getIdUsuario());
            }
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "delete from USUARIO where id_usuario = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuario nao encontrado: id " + id);
            }
        }
    }

    private Usuario montarUsuario(ResultSet resultSet) throws SQLException {
        Comunidade comunidade = new Comunidade(
                resultSet.getInt("id_comunidade"),
                resultSet.getString("comunidade_nome"),
                Categoria.valueOf(resultSet.getString("tema")));
        comunidade.setPontuacaoTotal(resultSet.getInt("pontuacao_total"));
        Date dataCriacaoComunidade = resultSet.getDate("data_criacao");
        if (dataCriacaoComunidade != null) {
            comunidade.setDataCriacao(dataCriacaoComunidade.toLocalDate());
        }

        Usuario usuario = new Usuario(
                resultSet.getInt("id_usuario"),
                resultSet.getString("nome"),
                resultSet.getString("email"),
                comunidade);
        Date dataCadastro = resultSet.getDate("data_cadastro");
        if (dataCadastro != null) {
            usuario.setDataCadastro(dataCadastro.toLocalDate());
        }
        return usuario;
    }

}
