package com.if_connect.models;

import com.google.gson.annotations.SerializedName;

public class Aluno{

    @SerializedName("curso")
    private Curso curso;

    @SerializedName("matricula")
    private String matricula;

    public Aluno(Curso curso, String matricula) {
        this.curso = curso;
        this.matricula = matricula;
    }

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
