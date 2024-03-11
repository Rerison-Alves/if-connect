package com.if_connect.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.utils.typeadapters.DateTimeAdapter;

import java.util.Date;

public class Mensagem {

    @SerializedName("id")
    private Integer id;

    @SerializedName("encontro")
    private Encontro encontro;

    @SerializedName("usuario")
    private Usuario usuario;

    @SerializedName("texto")
    private String texto;

    @SerializedName("pdfBase64")
    private String pdfBase64;

    @SerializedName("data")
    @JsonAdapter(DateTimeAdapter.class)
    private Date data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Encontro getEncontro() {
        return encontro;
    }

    public void setEncontro(Encontro encontro) {
        this.encontro = encontro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getPdfBase64() {
        return pdfBase64;
    }

    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}