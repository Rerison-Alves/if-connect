package com.if_connect.models;


import com.google.gson.annotations.SerializedName;

public abstract class Agrupamento {

    @SerializedName("id")
    private Integer id;

    @SerializedName("admin")
    private Usuario admin;

    @SerializedName("nome")
    private String nome;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("curso")
    private Curso curso;

    public Agrupamento(Usuario admin, String nome, String descricao, Curso curso) {
        this.admin = admin;
        this.nome = nome;
        this.descricao = descricao;
        this.curso = curso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getAdmin() {
        return admin;
    }

    public void setAdmin(Usuario admin) {
        this.admin = admin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}