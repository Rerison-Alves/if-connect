package com.if_connect.request.requestbody;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.models.enums.Role;
import com.if_connect.models.Aluno;
import com.if_connect.models.Professor;
import com.if_connect.utils.typeadapters.DateAdapter;

import java.util.Date;


public class RegisterRequest {

  @SerializedName("nome")
  private String nome;

  @SerializedName("email")
  private String email;

  @SerializedName("fotoPerfilBase64")
  private String fotoPerfilBase64;

  @SerializedName("password")
  private String password;

  @SerializedName("dataNasc")
  @JsonAdapter(DateAdapter.class)
  private Date dataNasc;

  @SerializedName("aluno")
  private Aluno aluno;

  @SerializedName("professor")
  private Professor professor;

  @SerializedName("role")
  private Role role;

  public RegisterRequest(String nome, String email, String fotoPerfilBase64, String password, Date dataNasc, Aluno aluno, Professor professor, Role role) {
    this.nome = nome;
    this.email = email;
    this.fotoPerfilBase64 = fotoPerfilBase64;
    this.password = password;
    this.dataNasc = dataNasc;
    this.aluno = aluno;
    this.professor = professor;
    this.role = role;
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

  public String getFotoPerfilBase64() {
    return fotoPerfilBase64;
  }

  public void setFotoPerfilBase64(String fotoPerfilBase64) {
    this.fotoPerfilBase64 = fotoPerfilBase64;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
