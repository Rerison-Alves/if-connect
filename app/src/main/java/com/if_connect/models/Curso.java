package com.if_connect.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.models.enums.Turno;
import com.if_connect.utils.typeadapters.TurnoAdapter;

public class Curso {

    @SerializedName("id")
    private Integer id;

    @SerializedName("iconeBase64")
    private String iconeBase64;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("observacao")
    private String observacao;

    @SerializedName("status")
    private Boolean status;

    @JsonAdapter(TurnoAdapter.class)
    @SerializedName("turno")
    private Turno turno;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIconeBase64() {
        return iconeBase64;
    }

    public void setIconeBase64(String iconeBase64) {
        this.iconeBase64 = iconeBase64;
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
