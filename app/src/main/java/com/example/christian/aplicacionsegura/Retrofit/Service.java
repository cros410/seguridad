package com.example.christian.aplicacionsegura.Retrofit;

import com.example.christian.aplicacionsegura.Models.TestBody;
import com.example.christian.aplicacionsegura.Models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Christian on 18/05/2017.
 */

public interface Service {
    @POST("/test")
    Call<Usuario> getUserByCorreo(@Body TestBody user);
}
