package com.example.gabriel.carona_fatec;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.adapter.AdapterConfirmarCarona;
import com.example.gabriel.carona_fatec.api.model.Reserva;
import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.service.ReservaServices;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmarCaronaActivity extends AppCompatActivity {

    Rota rota;
    ProgressDialog dialog;
    RecyclerView rv;
    Intent intentConfirmarCarona;
    TextView nenhumUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_carona);

        //Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intentConfirmarCarona = getIntent();

        //cria dialog progressbar
        dialog = new ProgressDialog(this);
        dialog.setMessage("Aguarde...");
        dialog.setCancelable(false);
        dialog.show();

        //Recebendo rota por intent
        rota = getIntent().getExtras().getParcelable("infoRotas");

        rv = (RecyclerView) findViewById(R.id.rv_confirmar_carona);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        nenhumUsuario = (TextView) findViewById(R.id.txt_nenhum_usuario);

        // Testa retorno http
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        //End

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://201.82.208.46:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        //Preparar retrofit para requisição
        ReservaServices reservaServices = retrofit.create(ReservaServices.class);
        Call<List<Reserva>> call = reservaServices.buscarReservaRota(rota.getId());

        call.enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                if (dialog.isShowing()) {

                    List<Reserva> reservasUsuarios = response.body();

                    mostrarReservasRota(reservasUsuarios);

                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(ConfirmarCaronaActivity.this, "Erro ao conectar no servidor, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void mostrarReservasRota(List<Reserva> listaReservas){

        if (listaReservas.isEmpty()) {
            //Toast.makeText(this, "Ops, nenhuma carona encontrada para seu destino :(", Toast.LENGTH_LONG).show();
            rv.setVisibility(View.INVISIBLE);
            nenhumUsuario.setVisibility(View.VISIBLE);

        } else {

            AdapterConfirmarCarona adapter = new AdapterConfirmarCarona(listaReservas);
            rv.setAdapter(adapter);

        }

    }

    public void requisicaoPut(Reserva reserva, final Context context){

        //cria dialog progressbar
        dialog = new ProgressDialog(context);
        dialog.setMessage("Alterando status da carona...");
        dialog.setCancelable(false);
        dialog.show();

        // Testa retorno http
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        //End

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://201.82.208.46:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        //Preparar retrofit para requisição
        ReservaServices reservaServices = retrofit.create(ReservaServices.class);
        Call<Boolean> call = reservaServices.alterarStatusReserva(reserva);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (dialog.isShowing()) {
                    //Se a resposta do servidor for verdadeira (Foi inserido com sucesso)
                    if (response.body()) {
                        dialog.dismiss();
                        Toast.makeText(context, "Usuário aprovado para carona.", Toast.LENGTH_LONG).show();
                        ((Activity)context).finish();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, "Erro ao inserir no banco de dados", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void requisicaoDelete(Reserva reserva, final Context context){

        //cria dialog progressbar
        dialog = new ProgressDialog(context);
        dialog.setMessage("Alterando status da carona...");
        dialog.setCancelable(false);
        dialog.show();

        // Testa retorno http
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        //End

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://201.82.208.46:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        //Preparar retrofit para requisição
        ReservaServices reservaServices = retrofit.create(ReservaServices.class);
        Call<Boolean> call = reservaServices.deletarReserva(reserva.getIdRota(), reserva.getIdUsuario());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (dialog.isShowing()) {
                    //Se a resposta do servidor for verdadeira (Foi recusado)
                    if (response.body()) {
                        dialog.dismiss();
                        Toast.makeText(context, "Usuário recusado para carona!", Toast.LENGTH_LONG).show();
                        ((Activity)context).finish();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, "Erro ao alterar no banco de dados", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Botão voltar sem recriar a activity de Listar Carona
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
