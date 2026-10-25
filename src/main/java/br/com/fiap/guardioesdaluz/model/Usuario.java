package br.com.fiap.guardioesdaluz.model;

import java.time.LocalDate;

public class Usuario {

    private Integer idUsuario;
    private String nome;
    private String email;
    private Comunidade comunidade;
    private LocalDate dataCadastro;

    private Integer saldoAtual;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String nome, String email, Comunidade comunidade) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.comunidade = comunidade;
    }

    public double calcularCreditoReais() {
        int saldo = saldoAtual != null ? saldoAtual : 0;
        return saldo / 100.0;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Comunidade getComunidade() {
        return comunidade;
    }

    public void setComunidade(Comunidade comunidade) {
        this.comunidade = comunidade;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Integer getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(Integer saldoAtual) {
        this.saldoAtual = saldoAtual;
    }
}
