package com.example.gabriel.carona_fatec;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfirmarRotaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public String stringAtual;
    public String stringDestino;

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

        stringAtual = extras.getString("resultLocalizacaoAtual");
        stringDestino =  extras.getString("resultDestino");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Key da api de Direction
        String contextDirectionApiKey = getResources().getString(R.string.api_key_direction);
        GeoApiContext context = new GeoApiContext().setApiKey(contextDirectionApiKey);

        try {
            DirectionsResult result =
                    DirectionsApi.newRequest(context)
                            .origin(stringAtual)
                            .destination(stringDestino)
                            .mode(TravelMode.DRIVING)
                            .await();

            System.out.println(result.geocodedWaypoints);
            System.out.println(result.geocodedWaypoints.length);
            System.out.println(result.geocodedWaypoints[0].geocoderStatus);
            System.out.println(result.geocodedWaypoints[1].geocoderStatus);
            System.out.println(result.geocodedWaypoints[1].types[0]);


            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(listaLatLngJavaApi(result.routes[0].overviewPolyline.decodePath()))
                    .width(5)
                    .color(Color.RED));

        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(converterStringLatLng(stringAtual)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(converterStringLatLng(stringAtual)));

        mMap.addMarker(new MarkerOptions().position(converterStringLatLng(stringDestino)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(converterStringLatLng(stringDestino)));

    }

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


    public LatLng converterStringLatLng(String stringLatLng){

            String atualLocalizacao[] = stringLatLng.split(",");
            double latitude = Double.parseDouble(atualLocalizacao[0]);
            lat = latitude;
            double longitude = Double.parseDouble(atualLocalizacao[1]);
            lng = longitude;

            LatLng atualLocalizacaoLatLng = new LatLng(latitude,longitude);

        return atualLocalizacaoLatLng;
    }



}
