package com.if_connect.models;

import com.google.gson.annotations.SerializedName;
import com.if_connect.models.enums.TipoAgrupamento;

import java.util.List;

public class Grupo extends Agrupamento{

    @SerializedName("areadeEstudo")
    private String areadeEstudo;

    @SerializedName("usuarios")
    private List<Usuario> usuarios;

    public Grupo(Usuario admin, String nome, String descricao, Curso curso, TipoAgrupamento tipoAgrupamento, String areadeEstudo, List<Usuario> usuarios) {
        super(admin, nome, descricao, curso, tipoAgrupamento);
        this.areadeEstudo = areadeEstudo;
        this.usuarios = usuarios;
    }

    public String getAreadeEstudo() {
        return areadeEstudo;
    }

    public void setAreadeEstudo(String areadeEstudo) {
        this.areadeEstudo = areadeEstudo;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}