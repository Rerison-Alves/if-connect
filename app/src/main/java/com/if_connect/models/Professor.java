package com.if_connect.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.enums.SituacaoProfessor;
import com.if_connect.utils.typeadapters.SituacaoProfessorAdapter;

import java.util.Date;

public class Professor{
    @SerializedName("siape")
    private String siape;

    @JsonAdapter(SituacaoProfessorAdapter.class)
    @SerializedName("situacao")
    private SituacaoProfessor situacao;

    public Professor(String siape, SituacaoProfessor situacao) {
        this.siape = siape;
        this.situacao = situacao;
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public SituacaoProfessor getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoProfessor situacao) {
        this.situacao = situacao;
    }
}
