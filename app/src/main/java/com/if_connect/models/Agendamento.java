package com.if_connect.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.if_connect.utils.typeadapters.DateTimeAdapter;

import java.util.Date;

public class Agendamento {

    @SerializedName("local")
    private Local local;

    @JsonAdapter(DateTimeAdapter.class)
    @SerializedName("startTime")
    private Date startTime;

    @JsonAdapter(DateTimeAdapter.class)
    @SerializedName("endTime")
    private Date endTime;

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