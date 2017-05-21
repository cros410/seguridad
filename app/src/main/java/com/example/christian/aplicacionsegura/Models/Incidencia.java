package com.example.christian.aplicacionsegura.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Christian on 21/05/2017.
 */

public class Incidencia extends RealmObject{
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("id_usuario")
    @Expose
    private String idUsuario;
    @SerializedName("titulo")
    @Expose
    private String titulo;
    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("distrito")
    @Expose
    private String distrito;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("pos")
    @Expose
    private Pos pos;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("revisado")
    @Expose
    private Integer revisado;
    @SerializedName("foto")
    @Expose
    private String foto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getRevisado() {
        return revisado;
    }

    public void setRevisado(Integer revisado) {
        this.revisado = revisado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
