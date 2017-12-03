package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.model.Usuario;
import com.example.gabriel.carona_fatec.api.service.RotaServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmarRotaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public String stringAtual, stringDestino, stringData, stringHorario, atual, destino;
    public int intDistancia;

    DirectionsResult result;

    ProgressDialog dialog;

    String rotaEncodePath;

    public double lat;
    public double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_rota);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intentConfirmarRota = getIntent();
        Bundle extras = intentConfirmarRota.getExtras();

        atual = extras.getString("localizacaoAtual");
        destino = extras.getString("destino");
        stringAtual = extras.getString("resultLocalizacaoAtual");
        stringDestino =  extras.getString("resultDestino");
        intDistancia = extras.getInt("distancia");
        stringData = extras.getString("data");
        stringHorario = extras.getString("horario");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Key da api de Direction
        String contextDirectionApiKey = getResources().getString(R.string.api_key_direction);
        GeoApiContext context = new GeoApiContext().setApiKey(contextDirectionApiKey);

        try {
            result =
                    DirectionsApi.newRequest(context)
                            .origin(stringAtual)
                            .destination(stringDestino)
                            .mode(TravelMode.DRIVING)
                            .await();

        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{

            //System.out.println(result.geocodedWaypoints);
            //System.out.println(result.geocodedWaypoints.length);
            //System.out.println(result.geocodedWaypoints[0].geocoderStatus);
            //System.out.println(result.geocodedWaypoints[1].geocoderStatus);
            //System.out.println(result.geocodedWaypoints[1].types[0]);
            //System.out.println(result.routes[0].overviewPolyline.decodePath());
            rotaEncodePath = result.routes[0].overviewPolyline.getEncodedPath();


            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(listaLatLngJavaApi(result.routes[0].overviewPolyline.decodePath()))
                    .width(5)
                    .color(Color.RED));

        } catch (Exception e){
            Toast.makeText(this, "Impossível traçar uma rota, local atual ou destino estão incorretos.", Toast.LENGTH_LONG).show();
            finish();
        }

        try{
        //Criando limite de tela
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(converterStringLatLng(stringAtual));
        builder.include(converterStringLatLng(stringDestino));

        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));

        mMap.addMarker(new MarkerOptions().position(converterStringLatLng(stringAtual)).title("Marker in Sydney"));
       //mMap.moveCamera(CameraUpdateFactory.newLatLng(converterStringLatLng(stringAtual)));

        mMap.addMarker(new MarkerOptions().position(converterStringLatLng(stringDestino)).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(converterStringLatLng(stringDestino)));

        } catch (Exception e){

        }

    }

    //Converter maps model LatLng em LatLng android.gms.maps.model.LatLng
    public List<LatLng> listaLatLngJavaApi(List<com.google.maps.model.LatLng> points){

        List<String> novaLista = new ArrayList<String>();
        List<LatLng> listLatLng = new ArrayList<LatLng>();

        for(com.google.maps.model.LatLng point: points){
                novaLista.add(String.valueOf(point));
        }
        for(String listString: novaLista){
            listLatLng.add(converterStringLatLng(listString));
        }
        return listLatLng;
    }

    //Converter string em LatLng
    public LatLng converterStringLatLng(String stringLatLng){

            String atualLocalizacao[] = stringLatLng.split(",");
            double latitude = Double.parseDouble(atualLocalizacao[0]);
            lat = latitude;
            double longitude = Double.parseDouble(atualLocalizacao[1]);
            lng = longitude;

            LatLng atualLocalizacaoLatLng = new LatLng(latitude,longitude);

        return atualLocalizacaoLatLng;
    }

    public void confirmar(View v){

        //Recebendo id do usuário por SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE);
        int idUsuario = sharedPreferences.getInt("idUsuario", 0);

        Rota rotaUsuario = new Rota();

        rotaUsuario.setAtual(atual);
        rotaUsuario.setDestino(destino);
        rotaUsuario.setRota(rotaEncodePath);
        rotaUsuario.setData(stringData);
        rotaUsuario.setHorario(stringHorario);
        rotaUsuario.setDistancia(intDistancia);

        Usuario usuario = new Usuario(idUsuario, rotaUsuario);

        enviarRequisicaoApi(usuario);

    }

    private void enviarRequisicaoApi(Usuario usuario) {

        // Testa retorno http
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        //End

        dialog = new ProgressDialog(ConfirmarRotaActivity.this);
        dialog.setMessage("Gravando rota...");
        dialog.setCancelable(false);
        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://201.82.208.46:8080/Web-Service-Tamo-Junto-Carona/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        RotaServices rota = retrofit.create(RotaServices.class);
        Call<Boolean> call = rota.inserirRota(usuario);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (dialog.isShowing()) {
                    //Se a resposta do servidor for verdadeira (Foi inserido com sucesso)
                    if (response.body()) {
                        dialog.dismiss();
                        Toast toast = Toast.makeText(ConfirmarRotaActivity.this, "Rota inserida com sucesso! Aprove os usuário na funcionalidade: 'Aprovar Caronas'.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        Intent intent = new Intent(ConfirmarRotaActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(ConfirmarRotaActivity.this, "Erro ao inserir no banco de dados", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(ConfirmarRotaActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
