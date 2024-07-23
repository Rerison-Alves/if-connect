package com.if_connect.models;


import com.google.gson.annotations.SerializedName;

public class Local {

    @SerializedName("id")
    private Integer id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("localizacao")
    private String localizacao;

    @SerializedName("iconeBase64")
    private String iconeBase64;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getIconeBase64() {
        return iconeBase64;
    }

    public void setIconeBase64(String iconeBase64) {
        this.iconeBase64 = iconeBase64;
    }
}