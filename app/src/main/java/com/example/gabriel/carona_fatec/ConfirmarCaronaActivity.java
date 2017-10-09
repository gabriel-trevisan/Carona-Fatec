package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Reserva;
import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.service.ReservaServices;

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
    ListView listViewConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_carona);

        //cria dialog progressbar
        dialog = new ProgressDialog(this);
        dialog.setMessage("Aguarde...");
        dialog.setCancelable(false);
        dialog.show();

        //Recebendo rota por intent
        rota = getIntent().getExtras().getParcelable("infoRotas");

        listViewConfirmar = (ListView) findViewById(R.id.listaConfirmar);


        // Testa retorno http
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        //End

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.37:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
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
                    Toast.makeText(ConfirmarCaronaActivity.this, "Erro ao conectar no servidor, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void mostrarReservasRota(List<Reserva> listaReservas){

        ArrayAdapter<Reserva> adapter = new ArrayAdapter<Reserva>(this, android.R.layout.simple_list_item_1, listaReservas);
        listViewConfirmar.setAdapter(adapter);

        listViewConfirmar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                final Reserva reserva = (Reserva) parent.getItemAtPosition(position);

                new AlertDialog.Builder(ConfirmarCaronaActivity.this)
                        .setTitle("Aprovar usuário")
                        .setMessage("Tem certeza que deseja aprovar este usuário para esta carona?")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        requisicaoPut(reserva);
                                    }
                                })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

    }

    public void requisicaoPut(Reserva reserva){

        //cria dialog progressbar
        dialog = new ProgressDialog(this);
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
                .baseUrl("http://10.0.2.37:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
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
                        Toast.makeText(ConfirmarCaronaActivity.this, "Usuário aprovado para carona.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());

                    } else {
                        dialog.dismiss();
                        Toast.makeText(ConfirmarCaronaActivity.this, "Erro ao inserir no banco de dados", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(ConfirmarCaronaActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
