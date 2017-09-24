package com.example.gabriel.carona_fatec.api.service;

import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.model.Usuario;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gabriel on 27/08/2017.
 */

public interface RotaServices {

    @POST("rota")
    Call<Boolean> inserirRota (@Body Usuario rota);

    //Objeto para requisições http
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.37:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
