package com.example.christian.aplicacionsegura.Retrofit;

import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.example.christian.aplicacionsegura.Models.Usuario;
import com.example.christian.aplicacionsegura.Response.LoginResponse;
import com.example.christian.aplicacionsegura.Response.ResponseIncidencias;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Christian on 18/05/2017.
 */

public interface Service {
    @POST("/login")
    Call<LoginResponse> getDataFotLogin(@Body Usuario user);

    @POST("/updateUser")
    Call<LoginResponse> updateUserPerfil(@Body Usuario user);

    @GET("/allIncidence")
    Call<ResponseIncidencias> getAll();

    @POST("/regiterIncidence")
    Call<LoginResponse> saveIncidencia(@Body Incidencia incidencia);

    @POST("/updateRangoUser")
    Call<LoginResponse> updateRangoUsuario(@Body Usuario user);
}
