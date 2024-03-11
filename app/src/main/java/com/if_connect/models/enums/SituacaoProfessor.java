package com.if_connect.models.enums;

public enum SituacaoProfessor {
    ATIVO("Ativo"),
    INATIVO("Inativo"),
    AFASTADO("Afastado"),
    LICENCA("Licença");

    private final String descricao;

    SituacaoProfessor(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}