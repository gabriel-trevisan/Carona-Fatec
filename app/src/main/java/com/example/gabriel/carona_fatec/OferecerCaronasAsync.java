package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.ConfirmarRotaActivity;
import com.example.gabriel.carona_fatec.LoginActivity;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;

/**
 * Created by gabriel on 14/10/2017.
 */

public class OferecerCaronasAsync extends AsyncTask<String, Void, Void>{

    GeocodingResult[] resultLocalizacaoAtual;
    GeocodingResult[] resultDestino;
    String localizacaoAtual, destino, distancia, data, horario;

    GeoApiContext geoApiContext;
    ProgressDialog dialog;
    Context context;

    public OferecerCaronasAsync(GeoApiContext geoApiContext, Context context){
        this.geoApiContext = geoApiContext;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Mapeando rota...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {

        resultLocalizacaoAtual = new GeocodingResult[0];
        resultDestino = new GeocodingResult[0];

        try {

            resultLocalizacaoAtual = GeocodingApi.geocode(geoApiContext, params[0]).await();
            resultDestino = GeocodingApi.geocode(geoApiContext, params[1]).await();
            localizacaoAtual = params[0];
            destino = params[1];
            distancia = params[2];
            data = params[3];
            horario = params[4];

        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       return null;
    }

    @Override
    protected void onProgressUpdate(Void... text) {

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        try{

        Intent confirmarRotaActivity = new Intent(context, ConfirmarRotaActivity.class);
        Bundle extras = new Bundle();
        extras.putString("localizacaoAtual", localizacaoAtual);
        extras.putString("destino", destino);
        extras.putString("resultLocalizacaoAtual", String.valueOf(resultLocalizacaoAtual[0].geometry.location));
        extras.putString("resultDestino", String.valueOf(resultDestino[0].geometry.location));
        extras.putInt("distancia", Integer.parseInt(distancia));
        extras.putString("data", data);
        extras.putString("horario", horario);
        confirmarRotaActivity.putExtras(extras);
        context.startActivity(confirmarRotaActivity);
            dialog.dismiss();

        } catch(Exception e){
            dialog.dismiss();
            Toast.makeText(context, "Impossível traçar uma rota, local atual ou destino estão incorretos.", Toast.LENGTH_LONG).show();
        }

    }

}
