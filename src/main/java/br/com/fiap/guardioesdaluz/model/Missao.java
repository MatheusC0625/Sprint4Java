package br.com.fiap.guardioesdaluz.model;

public class Missao {

    private Integer idMissao;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private TipoMissao tipoMissao;
    private Integer recompensaBase;
    private boolean ativa = true;

    public Missao() {
    }

    public Missao(Integer idMissao, String titulo, String descricao, Categoria categoria,
                  TipoMissao tipoMissao, Integer recompensaBase) {
        this.idMissao = idMissao;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.tipoMissao = tipoMissao;
        this.recompensaBase = recompensaBase;
    }

    public int calcularRecompensa(int streakAtual) {
        int recompensa = recompensaBase;
        if (tipoMissao == TipoMissao.DIARIA && streakAtual > 0 && streakAtual % 7 == 0) {
            recompensa += 50;
        }
        return recompensa;
    }

    public Integer getIdMissao() {
        return idMissao;
    }

    public void setIdMissao(Integer idMissao) {
        this.idMissao = idMissao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public TipoMissao getTipoMissao() {
        return tipoMissao;
    }

    public void setTipoMissao(TipoMissao tipoMissao) {
        this.tipoMissao = tipoMissao;
    }

    public Integer getRecompensaBase() {
        return recompensaBase;
    }

    public void setRecompensaBase(Integer recompensaBase) {
        this.recompensaBase = recompensaBase;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}
