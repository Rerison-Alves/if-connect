package com.if_connect.models;


import com.google.gson.annotations.SerializedName;

public class Encontro {

    @SerializedName("id")
    private Integer id;

    @SerializedName("tema")
    private String tema;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("grupo")
    private Grupo grupo;

    @SerializedName("turma")
    private Turma turma;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
}
