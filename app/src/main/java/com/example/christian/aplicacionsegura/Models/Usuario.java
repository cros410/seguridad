package com.example.christian.aplicacionsegura.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Christian on 18/05/2017.
 */

public class Usuario extends RealmObject {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("correo")
    @Expose
    private String correo;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("rango")
    @Expose
    private int rango;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("distrito")
    @Expose
    private String distrito;
    @SerializedName("numero")
    @Expose
    private String numero;
    @SerializedName("notificaciones")
    @Expose
    private RealmList<Notificacion> notificaciones;

    public Usuario() {
    }

    public Usuario(String id, String correo, String password, String foto, int rango, String nombre, String direccion, String distrito, String numero, RealmList<Notificacion> notificaciones) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.foto = foto;
        this.rango = rango;
        this.nombre = nombre;
        this.direccion = direccion;
        this.distrito = distrito;
        this.numero = numero;
        this.notificaciones = notificaciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public RealmList<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(RealmList<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
