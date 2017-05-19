package com.example.christian.aplicacionsegura.Bean;

import com.example.christian.aplicacionsegura.Models.Usuario;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Christian on 19/05/2017.
 */

public class Data {
    @SerializedName("usuario")
    @Expose
    private Usuario usuario;

    public Data(Usuario usuario) {
        this.usuario = usuario;
    }

    public Data() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
