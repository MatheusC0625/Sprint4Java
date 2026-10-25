package br.com.fiap.guardioesdaluz.model;

import java.time.LocalDate;

public class UsuarioMissao {

    private Integer idExecucao;
    private Usuario usuario;
    private Missao missao;
    private LocalDate dataConclusao;
    private StatusExecucao status = StatusExecucao.PENDENTE;

    public UsuarioMissao() {
    }

    public UsuarioMissao(Integer idExecucao, Usuario usuario, Missao missao, LocalDate dataConclusao) {
        this.idExecucao = idExecucao;
        this.usuario = usuario;
        this.missao = missao;
        this.dataConclusao = dataConclusao;
    }

    public Integer getIdExecucao() {
        return idExecucao;
    }

    public void setIdExecucao(Integer idExecucao) {
        this.idExecucao = idExecucao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Missao getMissao() {
        return missao;
    }

    public void setMissao(Missao missao) {
        this.missao = missao;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public StatusExecucao getStatus() {
        return status;
    }

    public void setStatus(StatusExecucao status) {
        this.status = status;
    }
}
