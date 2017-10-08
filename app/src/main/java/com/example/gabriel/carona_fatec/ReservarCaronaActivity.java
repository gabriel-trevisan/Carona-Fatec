package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Reserva;
import com.example.gabriel.carona_fatec.api.model.Usuario;
import com.example.gabriel.carona_fatec.api.service.ReservaServices;
import com.example.gabriel.carona_fatec.api.service.UsuarioServices;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservarCaronaActivity extends AppCompatActivity {

    ProgressDialog dialog;
    TextView saida, destino, horario, nome, email, celular;
    Usuario usuario;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_carona);

        //Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", 0);

        //Recebendo dados usuário por intent
        usuario = getIntent().getExtras().getParcelable("infoReservaUsuario");

        saida = (TextView) findViewById(R.id.tv_saida);
        destino = (TextView) findViewById(R.id.tv_destino);
        horario = (TextView) findViewById(R.id.tv_horario);
        nome = (TextView) findViewById(R.id.tv_nome);
        email = (TextView) findViewById(R.id.tv_email);
        celular = (TextView) findViewById(R.id.tv_celular);

        //saida
        //destino
        horario.setText(String.valueOf(usuario.getRota().getHorario()));
        nome.setText(String.valueOf(usuario.getNome()));
        email.setText(String.valueOf(usuario.getEmail()));
        celular.setText(String.valueOf(usuario.getNumeroCelular()));

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

    public void inserirReserva(View v){

        dialog = new ProgressDialog(this);
        dialog.setMessage("Realizando reserva...");
        dialog.setCancelable(false);
        dialog.show();

        Reserva reserva = new Reserva(usuario.getRota().getId(), idUsuario, "PENDENTE");

        enviarRequisicaoPostApi(reserva);

    }

    private void enviarRequisicaoPostApi(Reserva reserva) {

        //Testa retorno http
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

        ReservaServices reservaServices = retrofit.create(ReservaServices.class);
        Call<Boolean> call = reservaServices.inserirReserva(reserva);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (dialog.isShowing()) {
                    //Se a resposta do servidor for verdadeira (Foi inserido com sucesso)
                    if (response.body()) {
                        dialog.dismiss();
                        Toast.makeText(ReservarCaronaActivity.this, "Reserva realizada com sucesso, aguarde aprovação de sua carona.", Toast.LENGTH_SHORT).show();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(ReservarCaronaActivity.this, "Erro ao inserir no banco de dados", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(ReservarCaronaActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
