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
    Call<Usuario> getUsuario(@Path("email") String email);

    @POST("usuario/login")
    Call<Boolean> validarUsuario (@Body Usuario usuario);

    //Objeto para requisições http
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://201.82.208.46:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
