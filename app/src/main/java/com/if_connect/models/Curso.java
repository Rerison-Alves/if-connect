package com.if_connect.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.enums.Turno;
import com.if_connect.utils.typeadapters.TurnoAdapter;

public class Curso {

    @SerializedName("id")
    private int id;
    @SerializedName("descricao")
    private String descricao;
    @SerializedName("observacao")
    private String observacao;
    @SerializedName("status")
    private Boolean status;
    @SerializedName("turno")
    @JsonAdapter(TurnoAdapter.class)
    private Turno turno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}
