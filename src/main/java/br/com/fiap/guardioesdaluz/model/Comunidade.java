package br.com.fiap.guardioesdaluz.model;

import java.time.LocalDate;

public class Comunidade {

    private Integer idComunidade;
    private String nome;
    private Categoria tema;
    private Integer pontuacaoTotal = 0;
    private LocalDate dataCriacao;

    public Comunidade() {
    }

    public Comunidade(Integer idComunidade, String nome, Categoria tema) {
        this.idComunidade = idComunidade;
        this.nome = nome;
        this.tema = tema;
    }

    public void adicionarPontos(int pontos) {
        if (pontos < 0)
            throw new IllegalArgumentException("Pontos nao podem ser negativos");
        this.pontuacaoTotal += pontos;
    }

    public Integer getIdComunidade() {
        return idComunidade;
    }

    public void setIdComunidade(Integer idComunidade) {
        this.idComunidade = idComunidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getTema() {
        return tema;
    }

    public void setTema(Categoria tema) {
        this.tema = tema;
    }

    public Integer getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(Integer pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
