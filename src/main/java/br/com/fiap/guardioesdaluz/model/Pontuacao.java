package br.com.fiap.guardioesdaluz.model;

import java.time.LocalDate;

public class Pontuacao {

    private Integer idPontuacao;
    private Usuario usuario;
    private UsuarioMissao execucao;
    private OrigemPontuacao origem;
    private Integer pontos;
    private LocalDate dataGeracao;
    private LocalDate dataExpiracao;
    private StatusPontuacao status = StatusPontuacao.ATIVO;

    public Pontuacao() {
    }

    public Pontuacao(Integer idPontuacao, Usuario usuario, OrigemPontuacao origem, Integer pontos) {
        this.idPontuacao = idPontuacao;
        this.usuario = usuario;
        this.origem = origem;
        this.pontos = pontos;
    }

    public boolean estaExpirado() {
        return dataExpiracao != null && LocalDate.now().isAfter(dataExpiracao);
    }

    public Integer getIdPontuacao() {
        return idPontuacao;
    }

    public void setIdPontuacao(Integer idPontuacao) {
        this.idPontuacao = idPontuacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioMissao getExecucao() {
        return execucao;
    }

    public void setExecucao(UsuarioMissao execucao) {
        this.execucao = execucao;
    }

    public OrigemPontuacao getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemPontuacao origem) {
        this.origem = origem;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDate dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public StatusPontuacao getStatus() {
        return status;
    }

    public void setStatus(StatusPontuacao status) {
        this.status = status;
    }
}
