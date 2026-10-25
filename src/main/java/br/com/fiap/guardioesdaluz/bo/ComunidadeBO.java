package br.com.fiap.guardioesdaluz.bo;

import br.com.fiap.guardioesdaluz.dao.ComunidadeDao;
import br.com.fiap.guardioesdaluz.exception.RegraDeNegocioException;
import br.com.fiap.guardioesdaluz.model.Comunidade;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComunidadeBO {

    private final ComunidadeDao comunidadeDao;

    public ComunidadeBO(ComunidadeDao comunidadeDao) {
        this.comunidadeDao = comunidadeDao;
    }

    public Comunidade cadastrar(Comunidade comunidade) throws SQLException {
        validar(comunidade);
        return comunidadeDao.cadastrar(comunidade);
    }

    public Comunidade buscarPorId(int id) throws SQLException {
        return comunidadeDao.buscarPorId(id);
    }

    public List<Comunidade> listar() throws SQLException {
        return comunidadeDao.listar();
    }

    public Comunidade atualizar(int id, Comunidade comunidade) throws SQLException {
        validar(comunidade);
        comunidade.setIdComunidade(id);
        comunidadeDao.atualizar(comunidade);
        return comunidade;
    }

    public void deletar(int id) throws SQLException {
        comunidadeDao.deletar(id);
    }

    public List<Comunidade> calcularVencedoras() throws SQLException {
        List<Comunidade> ranking = comunidadeDao.listar();
        List<Comunidade> vencedoras = new ArrayList<>();
        int maior = 0;

        for (Comunidade c : ranking) {
            if (c.getPontuacaoTotal() > maior) {
                maior = c.getPontuacaoTotal();
                vencedoras.clear();
                vencedoras.add(c);
            } else if (c.getPontuacaoTotal() == maior && maior > 0) {
                vencedoras.add(c);
            }
        }
        return vencedoras;
    }

    private void validar(Comunidade comunidade) {
        if (comunidade.getNome() == null || comunidade.getNome().isBlank()) {
            throw new RegraDeNegocioException("Nome da comunidade e obrigatorio");
        }
        if (comunidade.getTema() == null) {
            throw new RegraDeNegocioException("Tema da comunidade e obrigatorio");
        }
    }

}
