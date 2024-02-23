package com.if_connect.models;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.utils.typeadapters.DateAdapter;

import java.util.Date;

public abstract class Usuario {

    @SerializedName("id")
    private Integer id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("dataNasc")
    @JsonAdapter(DateAdapter.class)
    private Date dataNasc;

    @SerializedName("aluno")
    private Aluno aluno;

    @SerializedName("professor")
    private Professor professor;

    public Usuario(String nome, String email, Date dataNasc, Aluno aluno, Professor professor) {
        this.nome = nome;
        this.email = email;
        this.dataNasc = dataNasc;
        this.aluno = aluno;
        this.professor = professor;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
