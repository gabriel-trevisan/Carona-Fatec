package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Reserva;
import com.example.gabriel.carona_fatec.api.service.ReservaServices;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MinhasBuscasCarona extends AppCompatActivity {

    ListView listViewReservas;
    ProgressDialog dialog;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_buscas_carona);

        //cria dialog progressbar
        dialog = new ProgressDialog(this);
        dialog.setMessage("Aguarde, buscando suas caronas...");
        dialog.setCancelable(false);
        dialog.show();

        //Receber idUsuario por sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", 0);

        listViewReservas = (ListView) findViewById(R.id.listaReservas);

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
        Call<List<Reserva>> call = reservaServices.buscarReserva(idUsuario);

        call.enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                if (dialog.isShowing()) {

                    List<Reserva> reservasUsuarios = response.body();

                    mostrarReservas(reservasUsuarios);

                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(MinhasBuscasCarona.this, "Erro ao conectar no servidor, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void mostrarReservas(List<Reserva> listaReservas){

        ArrayAdapter<Reserva> adapter = new ArrayAdapter<Reserva>(this, android.R.layout.simple_list_item_1, listaReservas);
        listViewReservas.setAdapter(adapter);

    }

}
