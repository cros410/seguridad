package com.example.christian.aplicacionsegura.Retrofit;

import com.example.christian.aplicacionsegura.Models.TestBody;
import com.example.christian.aplicacionsegura.Models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by Christian on 18/05/2017.
 */

public interface Service {
    @GET("/allIncidence")
    Call<Usuario> getUserByCorreo(@Body TestBody user);
}
