package com.if_connect.request.requestbody;

import com.if_connect.enums.Role;
import com.if_connect.models.Curso;

import java.util.Date;


public class RegisterAlunoRequest {

  private String nome;
  private String email;
  private String password;
  private Date dataNasc;
  private Role role;

  private Curso curso;
  private String matricula;
}
