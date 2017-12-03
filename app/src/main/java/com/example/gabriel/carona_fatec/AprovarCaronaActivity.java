package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.adapter.AdapterAprovarCarona;
import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.service.RotaServices;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AprovarCaronaActivity extends AppCompatActivity {

    ProgressDialog dialog;
    int idUsuario;
    ListView listViewCaronasOferecidas;
    TextView nenhumUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprovar_carona);

        //cria dialog progressbar
        dialog = new ProgressDialog(this);
        dialog.setMessage("Aguarde, buscando suas caronas...");
        dialog.setCancelable(false);
        dialog.show();

        //Receber idUsuario por sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", 0);

        listViewCaronasOferecidas = (ListView) findViewById(R.id.listaOfertas);
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
        RotaServices reservaServices = retrofit.create(RotaServices.class);
        Call<List<Rota>> call = reservaServices.listarCaronasOferecidas(idUsuario);

        call.enqueue(new Callback<List<Rota>>() {
            @Override
            public void onResponse(Call<List<Rota>> call, Response<List<Rota>> response) {
                if (dialog.isShowing()) {

                    List<Rota> ofertasCarona = response.body();

                    mostrarCaronasOferecidas(ofertasCarona);

                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Rota>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(AprovarCaronaActivity.this, "Erro ao conectar no servidor, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void mostrarCaronasOferecidas(List<Rota> ofertasCarona){

        if (ofertasCarona.isEmpty()) {

            listViewCaronasOferecidas.setVisibility(View.INVISIBLE);
            nenhumUsuario.setVisibility(View.VISIBLE);

        } else {

            AdapterAprovarCarona adapter = new AdapterAprovarCarona(ofertasCarona, this);
            listViewCaronasOferecidas.setAdapter(adapter);

            listViewCaronasOferecidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Rota rota = (Rota) parent.getItemAtPosition(position);
                    Intent intent = new Intent(AprovarCaronaActivity.this, ConfirmarCaronaActivity.class);
                    Toast toast = Toast.makeText(AprovarCaronaActivity.this, "Aprove os usários pendentes.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    intent.putExtra("infoRotas", rota);
                    startActivity(intent);

                }
            });

        }

    }
}
