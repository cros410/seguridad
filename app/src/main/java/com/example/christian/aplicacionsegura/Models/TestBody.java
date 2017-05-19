package com.example.christian.aplicacionsegura.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Christian on 18/05/2017.
 */

public class TestBody {
    @SerializedName("correo")
    @Expose
    private String correo;

    public TestBody() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
