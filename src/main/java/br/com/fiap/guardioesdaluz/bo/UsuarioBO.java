package br.com.fiap.guardioesdaluz.bo;

import br.com.fiap.guardioesdaluz.dao.PontuacaoDao;
import br.com.fiap.guardioesdaluz.dao.UsuarioDao;
import br.com.fiap.guardioesdaluz.exception.RegraDeNegocioException;
import br.com.fiap.guardioesdaluz.model.Usuario;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UsuarioBO {

    private final UsuarioDao usuarioDao;
    private final PontuacaoDao pontuacaoDao;

    public UsuarioBO(UsuarioDao usuarioDao, PontuacaoDao pontuacaoDao) {
        this.usuarioDao = usuarioDao;
        this.pontuacaoDao = pontuacaoDao;
    }

    public Usuario cadastrar(Usuario usuario) throws SQLException {
        validar(usuario);
        Usuario cadastrado = usuarioDao.cadastrar(usuario);
        cadastrado.setSaldoAtual(0);
        return cadastrado;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        Usuario usuario = usuarioDao.buscarPorId(id);
        return comSaldo(usuario);
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = usuarioDao.listar();
        for (Usuario usuario : usuarios) {
            comSaldo(usuario);
        }
        return usuarios;
    }

    public Usuario atualizar(int id, Usuario usuario) throws SQLException {
        validar(usuario);
        usuario.setIdUsuario(id);
        usuarioDao.atualizar(usuario);
        return comSaldo(usuario);
    }

    public void deletar(int id) throws SQLException {
        usuarioDao.deletar(id);
    }

    private Usuario comSaldo(Usuario usuario) throws SQLException {
        usuario.setSaldoAtual(pontuacaoDao.somarSaldoAtivo(usuario.getIdUsuario()));
        return usuario;
    }

    private void validar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new RegraDeNegocioException("Nome do usuario e obrigatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new RegraDeNegocioException("Email do usuario e obrigatorio");
        }
        if (usuario.getComunidade() == null || usuario.getComunidade().getIdComunidade() == null) {
            throw new RegraDeNegocioException("Comunidade do usuario e obrigatoria");
        }
    }

}
