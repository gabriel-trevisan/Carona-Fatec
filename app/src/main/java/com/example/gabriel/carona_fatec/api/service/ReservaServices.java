package com.example.gabriel.carona_fatec.api.service;

import com.example.gabriel.carona_fatec.api.model.Reserva;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by gabriel on 07/10/2017.
 */

public interface ReservaServices {

    @POST("reserva")
    Call<Boolean> inserirReserva (@Body Reserva reserva);

    @GET("reserva/{idUsuario}")
    Call<List<Reserva>> buscarReserva (@Path("idUsuario") int idUsuario);

    @GET("reserva/rota/{idRota}")
    Call<List<Reserva>> buscarReservaRota (@Path("idRota") int idRota);

    @PUT("reserva")
    Call<Boolean> alterarStatusReserva (@Body Reserva reserva);

    @GET("reserva/{idUsuario}/{idRota}")
    Call<Boolean> validarReserva (@Path("idUsuario") int idUsuario,
                                  @Path("idRota") int idRota);

    //Objeto para requisições http
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://201.82.208.46:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
