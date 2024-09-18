package com.if_connect.models.enums;

public enum TipoUsuario {
    ALUNO("Aluno"),
    PROFESSOR("Professor");

    private final String tipo;

    TipoUsuario(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoUsuario() {
        return tipo;
    }
}
