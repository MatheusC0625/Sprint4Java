package br.com.fiap.guardioesdaluz.bo;

import br.com.fiap.guardioesdaluz.dao.MissaoDao;
import br.com.fiap.guardioesdaluz.exception.RegraDeNegocioException;
import br.com.fiap.guardioesdaluz.model.Missao;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MissaoBO {

    private final MissaoDao missaoDao;

    public MissaoBO(MissaoDao missaoDao) {
        this.missaoDao = missaoDao;
    }

    public Missao cadastrar(Missao missao) throws SQLException {
        validar(missao);
        return missaoDao.cadastrar(missao);
    }

    public Missao buscarPorId(int id) throws SQLException {
        return missaoDao.buscarPorId(id);
    }

    public List<Missao> listar() throws SQLException {
        return missaoDao.listar();
    }

    public Missao atualizar(int id, Missao missao) throws SQLException {
        validar(missao);
        missao.setIdMissao(id);
        missaoDao.atualizar(missao);
        return missao;
    }

    public void deletar(int id) throws SQLException {
        missaoDao.deletar(id);
    }

    private void validar(Missao missao) {
        if (missao.getTitulo() == null || missao.getTitulo().isBlank()) {
            throw new RegraDeNegocioException("Titulo da missao e obrigatorio");
        }
        if (missao.getCategoria() == null) {
            throw new RegraDeNegocioException("Categoria da missao e obrigatoria");
        }
        if (missao.getTipoMissao() == null) {
            throw new RegraDeNegocioException("Tipo da missao e obrigatorio");
        }
        if (missao.getRecompensaBase() == null || missao.getRecompensaBase() <= 0) {
            throw new RegraDeNegocioException("Recompensa base deve ser maior que zero");
        }
    }

}
