package com.example.christian.aplicacionsegura.Response;

import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseIncidencias {
    @SerializedName("cod")
    @Expose
    private Integer cod;
    @SerializedName("data")
    @Expose
    private List<Incidencia> data = null;

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public List<Incidencia> getData() {
        return data;
    }

    public void setData(List<Incidencia> data) {
        this.data = data;
    }
}
