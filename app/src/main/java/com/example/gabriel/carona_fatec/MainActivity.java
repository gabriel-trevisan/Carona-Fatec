package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.gabriel.carona_fatec.api.model.Usuario;
import com.example.gabriel.carona_fatec.api.service.UsuarioServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Usuario usuario;
    ProgressDialog dialog;
    TextView tvUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        tvUsuario = (TextView) header.findViewById(R.id.tv_email_usuario);

        tvUsuario.setText(result);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_caronas_procuradas) {

            Intent intent = new Intent(this, MinhasBuscasCarona.class);
            startActivity(intent);

        } else if (id == R.id.nav_caronas_oferecidas) {

            Intent intent = new Intent(this, MinhasOfertasCarona.class);
            startActivity(intent);

        } else if (id == R.id.nav_sobre) {

            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_aprovar) {

            Intent intent = new Intent(this, AprovarCaronaActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_sair) {

            finish();
            System.exit(0);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void oferecerCarona(View v){

        Intent intentOferecerCarona = new Intent(this, OferecerCaronasActivity.class);
        startActivity(intentOferecerCarona);

    }

    public void buscarCarona (View v){

        Intent intentBuscarCarona = new Intent(this, BuscarCaronasActivity.class);
        startActivity(intentBuscarCarona);

    }

}
