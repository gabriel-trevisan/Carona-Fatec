package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;

public class OferecerCaronasActivity extends AppCompatActivity {

    EditText localizacaoAtual;
    EditText destino;
    EditText distancia;
    GeocodingResult[] resultLocalizacaoAtual;
    GeocodingResult[] resultDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferecer_caronas);

        //Instanciando objetos
        localizacaoAtual = (EditText) findViewById(R.id.edtLocalizacaoUsuario);
        destino = (EditText) findViewById(R.id.edtDestinoUsuario);
        distancia = (EditText) findViewById(R.id.edtDistancia);

    }

    public void confirmarRota (View v){

        String stringLocalizacaoAtual = localizacaoAtual.getText().toString();
        String stringDestino = destino.getText().toString();

        //Key da api de Geo
        String contextGeoApiKey = getResources().getString(R.string.api_key_geo);

        GeoApiContext context = new GeoApiContext().setApiKey(contextGeoApiKey);

        resultLocalizacaoAtual = new GeocodingResult[0];
        resultDestino = new GeocodingResult[0];

            try {

                resultLocalizacaoAtual = GeocodingApi.geocode(context, stringLocalizacaoAtual).await();
                resultDestino = GeocodingApi.geocode(context, stringDestino).await();

                Intent confirmarRotaActivity = new Intent(this, ConfirmarRotaActivity.class);
                Bundle extras = new Bundle();
                extras.putString("resultLocalizacaoAtual", String.valueOf(resultLocalizacaoAtual[0].geometry.location));
                extras.putString("resultDestino", String.valueOf(resultDestino[0].geometry.location));
                confirmarRotaActivity.putExtras(extras);
                startActivity(confirmarRotaActivity);

            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

}
