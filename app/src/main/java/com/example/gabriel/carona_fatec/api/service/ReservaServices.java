package com.example.gabriel.carona_fatec.api.service;

import com.example.gabriel.carona_fatec.api.model.Reserva;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by gabriel on 07/10/2017.
 */

public interface ReservaServices {

    @POST("reserva")
    Call<Boolean> inserirReserva (@Body Reserva reserva);

    //Objeto para requisições http
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.37:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
