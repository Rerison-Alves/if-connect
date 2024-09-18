package com.if_connect.models.enums;

import java.util.Arrays;

public enum TipoAgrupamento {
    GRUPO("Grupo"),
    TURMA("Turma");

    private final String tipo;

    TipoAgrupamento(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public static String[] getTipos() {
        return Arrays.stream(TipoAgrupamento.values())
                .map(TipoAgrupamento::getTipo)
                .toArray(String[]::new);
    }
}
