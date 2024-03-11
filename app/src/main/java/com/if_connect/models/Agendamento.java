package com.if_connect.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.utils.typeadapters.DateTimeAdapter;

import java.util.Date;

public class Agendamento {

    @SerializedName("id")
    private Integer id;

    @SerializedName("encontro")
    private Encontro encontro;

    @SerializedName("local")
    private Local local;

    @JsonAdapter(DateTimeAdapter.class)
    @SerializedName("startTime")
    private Date startTime;

    @JsonAdapter(DateTimeAdapter.class)
    @SerializedName("endTime")
    private Date endTime;

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

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}