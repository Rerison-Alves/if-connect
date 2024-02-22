package com.if_connect.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Aluno{

    @SerializedName("curso")
    private Curso curso;

    @SerializedName("matricula")
    private String matricula;

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

}
