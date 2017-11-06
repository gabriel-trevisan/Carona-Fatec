package com.example.gabriel.carona_fatec;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OferecerCaronasActivity extends AppCompatActivity {

    EditText localizacaoAtual;
    EditText destino;
    EditText distancia;
    EditText data;
    EditText horario;

    Calendar calendario;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferecer_caronas);

        localizacaoAtual = (EditText) findViewById(R.id.edtLocalizacaoUsuario);
        destino = (EditText) findViewById(R.id.edtDestinoUsuario);
        distancia = (EditText) findViewById(R.id.edtDistancia);
        data = (EditText) findViewById(R.id.edtDataUsuario);
        horario = (EditText) findViewById(R.id.edtHorario);

        calendario = Calendar.getInstance();

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

                    new DatePickerDialog(OferecerCaronasActivity.this, R.style.DialogTheme, date, calendario
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

                relogioPicker = new TimePickerDialog(OferecerCaronasActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override

                    public void onTimeSet(TimePicker timePicker, int horas, int minutos) {
                        horario.setText( String.format("%02d:%02d", horas, minutos));
                    }
                }, horas, minutos, true);

                relogioPicker.show();
            }
        });


    }

    public void confirmarRota (View v){

        String stringLocalizacaoAtual = localizacaoAtual.getText().toString();
        String stringDestino = destino.getText().toString();

        //Key da api de Geo
        String contextGeoApiKey = getResources().getString(R.string.api_key_geo);

        GeoApiContext geoApiContext = new GeoApiContext().setApiKey(contextGeoApiKey);

        OferecerCaronasAsync rota = new OferecerCaronasAsync(geoApiContext, this);

        rota.execute(stringLocalizacaoAtual, stringDestino, distancia.getText().toString(), data.getText().toString(), horario.getText().toString());

    }

}
