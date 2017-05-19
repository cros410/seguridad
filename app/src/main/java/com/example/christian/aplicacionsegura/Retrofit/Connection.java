package com.example.christian.aplicacionsegura.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Christian on 18/05/2017.
 */

public class Connection {
    public static final String BASE_URL = "https://seguridadapp.herokuapp.com";

    public Retrofit getConecction(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return  retrofit;
    }

    public static Service getService(){
        Connection connection = new Connection();
        Retrofit retrofit = connection.getConecction();
        return  retrofit.create(Service.class);
    }
}
