package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        //Recebendo email do usuário por SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("usuarioLoginEmail", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("emailUsuario", "");

        usuario = new Usuario();
        usuario.setEmail(result);

        UsuarioServices usuarioServices = UsuarioServices.retrofit.create(UsuarioServices.class);
        Call<Usuario> call = usuarioServices.getUsuario(usuario.getEmail());

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                    usuario.setId(response.body().getId());
                    System.out.println(usuario.getId());

                    //Armazenar as informações do usuário em SharedPreferences, apenas id do usuário ainda.
                    SharedPreferences sharedPreferences = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("idUsuario", usuario.getId());
                    editor.apply();

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.minhasBuscasCarona){

            Intent intent = new Intent(this, MinhasBuscasCarona.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.minhasOfertasCarona){

            Intent intent = new Intent(this, MinhasOfertasCarona.class);
            startActivity(intent);
            return true;
        }

        return true;
    }

    
    public void oferecerCarona(View v){

        Intent intentOferecerCarona = new Intent(CaronaActivity.this, OferecerCaronasActivity.class);
        startActivity(intentOferecerCarona);

    }

    public void buscarCarona (View v){

        Intent intentBuscarCarona = new Intent(CaronaActivity.this, BuscarCaronasActivity.class);
        startActivity(intentBuscarCarona);

    }

}
