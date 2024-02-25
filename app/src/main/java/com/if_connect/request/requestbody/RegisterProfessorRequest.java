package com.if_connect.request.requestbody;

import com.if_connect.enums.Role;
import com.if_connect.enums.SituacaoProfessor;

import java.util.Date;


public class RegisterProfessorRequest {

  private String nome;
  private String email;
  private String password;
  private Date dataNasc;
  private Role role;

  private String siape;
  private SituacaoProfessor situacao;
}
