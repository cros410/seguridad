package com.example.christian.aplicacionsegura.Retrofit;

import com.example.christian.aplicacionsegura.Models.Usuario;
import com.example.christian.aplicacionsegura.Response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Christian on 18/05/2017.
 */

public interface Service {
    @POST("/login")
    Call<LoginResponse> getDataFotLogin(@Body Usuario user);
}
