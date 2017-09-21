package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Usuario;
import com.example.gabriel.carona_fatec.api.service.UsuarioServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaronaActivity extends AppCompatActivity {

    Usuario usuario;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carona);

        //Recebendo email do usuário da tela de login
        SharedPreferences sharedPreferences = getSharedPreferences("emailUsuario", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("emailUsuario", "");

        usuario = new Usuario();
        usuario.setEmail(result);

    }

    //Button oferecerCarona
    public void oferecerCarona(View v){

        dialog = new ProgressDialog(CaronaActivity.this);
        dialog.setMessage("Esperando resposta do servidor...");
        dialog.setCancelable(false);
        dialog.show();

        UsuarioServices usuarioServices = UsuarioServices.retrofit.create(UsuarioServices.class);
        Call<Usuario> call = usuarioServices.getUsuario(usuario.getEmail());

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(dialog.isShowing()) {
                    //Se api retornar null, significa que nenhum email foi encontrado.
                    if (response.body() == null) {
                        //Falha no sharedPreference
                        dialog.dismiss();
                        Toast.makeText(CaronaActivity.this, "Falha no app, finalize seu aplicativo e acesse-o novamente.", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getPerfil() == 1) {
                        dialog.dismiss();
                        Intent intentOferecerCarona = new Intent(CaronaActivity.this, OferecerCaronasActivity.class);
                        startActivity(intentOferecerCarona);
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(CaronaActivity.this, "Você não possui perfil para oferecer carona, altere seu perfil para oferecer carona!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(CaronaActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
