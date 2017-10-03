package com.example.gabriel.carona_fatec.api.service;

import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gabriel on 27/08/2017.
 */

public interface RotaServices {

    @POST("rota")
    Call<Boolean> inserirRota (@Body Usuario rota);

    @GET("rota")
    Call<List<Usuario>> buscarRota (@Query("destino") String destino,
                                 @Query("data") String data,
                                 @Query("horario") String horario);

    //Objeto para requisições http
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.37:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
