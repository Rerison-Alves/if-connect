package com.if_connect.models;


import com.google.gson.annotations.SerializedName;
import com.if_connect.models.enums.TipoAgrupamento;
import com.if_connect.models.enums.Turno;

import java.util.List;

public class Turma extends Agrupamento{

    @SerializedName("disciplina")
    private String disciplina;

    @SerializedName("turno")
    private Turno turno;

    @SerializedName("usuarios")
    private List<Usuario> usuarios;

    public Turma(Usuario admin, String nome, String descricao, Curso curso, TipoAgrupamento tipoAgrupamento, String disciplina, Turno turno, List<Usuario> usuarios) {
        super(admin, nome, descricao, curso, tipoAgrupamento);
        this.disciplina = disciplina;
        this.turno = turno;
        this.usuarios = usuarios;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
