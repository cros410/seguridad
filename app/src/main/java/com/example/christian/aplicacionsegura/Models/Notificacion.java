package com.example.christian.aplicacionsegura.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Christian on 18/05/2017.
 */

public class Notificacion  extends RealmObject{
    @SerializedName("id_incidencia")
    @Expose
    String id_incidencia;
    @SerializedName("estado")
    @Expose
    int estado;

    public Notificacion() {
    }

    public Notificacion(String id_usuario, int estado) {
        this.id_incidencia = id_usuario;
        this.estado = estado;
    }

    public String getId_usuario() {
        return id_incidencia;
    }

    public void setId_usuario(String id_usuario) {
        this.id_incidencia = id_usuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
