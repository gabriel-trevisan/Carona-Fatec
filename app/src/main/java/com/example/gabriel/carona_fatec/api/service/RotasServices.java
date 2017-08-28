package com.example.gabriel.carona_fatec.api.service;

import com.example.gabriel.carona_fatec.api.model.Rotas;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gabriel on 27/08/2017.
 */

public interface RotasServices {

    @POST("rotas")
    Call<Rotas> inserirRota (@Body Rotas rota);

}
