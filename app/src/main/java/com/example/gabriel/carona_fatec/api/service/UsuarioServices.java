package com.example.gabriel.carona_fatec.api.service;

import com.example.gabriel.carona_fatec.api.model.Usuario;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by gabriel on 09/09/2017.
 */

public interface UsuarioServices {

    @POST("usuario")
    Call<Boolean> inserirUsuario (@Body Usuario usuario);

    //verificar existencia de email no banco de dados
    @GET("usuario/{email}")
    Call<Boolean> selecionarUsuario (@Path("email") String email);

    //Objeto para requisições http
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.37:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
