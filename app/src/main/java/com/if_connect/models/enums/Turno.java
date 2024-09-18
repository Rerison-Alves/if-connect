package com.if_connect.models.enums;

import java.util.Arrays;

public enum Turno {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");

    private final String descricao;

    Turno(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static String[] getDescricoes() {
        return Arrays.stream(Turno.values())
                .map(Turno::getDescricao)
                .toArray(String[]::new);
    }
}
