package com.example.gabriel.carona_fatec;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.model.Usuario;
import com.example.gabriel.carona_fatec.api.service.RotaServices;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarCaronasActivity extends AppCompatActivity {

    EditText destino;
    EditText data;
    EditText horario;
    GeocodingResult[] resultDestino;
    ProgressDialog dialog;

    Calendar calendario;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_caronas);

        destino = (EditText) findViewById(R.id.edtDestinoUsuario);
        data = (EditText) findViewById(R.id.edtDataUsuario);
        horario = (EditText) findViewById(R.id.edtHorario);

        calendario = Calendar.getInstance();

        resultDestino = new GeocodingResult[0];

        //Dialog data
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, monthOfYear);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                data.setText(sdf.format(calendario.getTime()));
            }
        };

        //Listener em edtData
        data.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(BuscarCaronasActivity.this, R.style.DialogTheme, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Listener em edtHora
        horario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int horas = calendario.get(Calendar.HOUR_OF_DAY);
                int minutos = calendario.get(Calendar.MINUTE);

                TimePickerDialog relogioPicker;

                relogioPicker = new TimePickerDialog(BuscarCaronasActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override

                    public void onTimeSet(TimePicker timePicker, int horas, int minutos) {
                        horario.setText( String.format("%02d:%02d", horas, minutos));
                    }
                }, horas, minutos, true);

                relogioPicker.show();
            }
        });

    }

    public void buscarCarona(View v) throws InterruptedException, ApiException, IOException {

        //Key da api de Geo
        String contextGeoApiKey = getResources().getString(R.string.api_key_geo);

        GeoApiContext context = new GeoApiContext().setApiKey(contextGeoApiKey);

        resultDestino = GeocodingApi.geocode(context, destino.getText().toString()).await();

        String destinoLatLng = String.valueOf(resultDestino[0].geometry.location);

        enviarRequisicaoGetApi(destinoLatLng,
                            data.getText().toString(),
                            horario.getText().toString());

    }

    private void enviarRequisicaoGetApi(String destino, String data, String horario) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Procurando carona...");
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

        RotaServices rota = retrofit.create(RotaServices.class);
        Call<List<Usuario>> call = rota.buscarRota(destino, data, horario);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                List<Usuario> listaUsuarios = response.body();

                dialog.dismiss();

                enviarUsuarios(listaUsuarios);

            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

                Toast.makeText(BuscarCaronasActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void enviarUsuarios(List<Usuario> listaUsuarios){

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        for(Usuario usuario: listaUsuarios) {

            usuarios.add(new Usuario(usuario.getNome(), usuario.getEmail(), usuario.getNumeroCelular(), usuario.getTurma(), usuario.getRota()));

        }

        Intent intent = new Intent(this, ListarCaronaActivity.class);
        intent.putExtra("arrayUsuarios", usuarios);
        startActivity(intent);

    }




}
