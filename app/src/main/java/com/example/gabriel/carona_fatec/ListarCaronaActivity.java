package com.example.gabriel.carona_fatec;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListarCaronaActivity extends AppCompatActivity {

    ListView listViewUsuarios;
    ArrayList<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_carona);

        if(savedInstanceState != null){
           usuarios  = savedInstanceState.getParcelableArrayList("lista");
        }
        else{

            Intent intent = getIntent();
            usuarios = intent
                    .getParcelableArrayListExtra("arrayUsuarios");

        }
            listViewUsuarios = (ListView) findViewById(R.id.listaUsuarios);

            List<Usuario> listaUsuarios = usuarios;

            if (listaUsuarios.isEmpty()) {
                Toast.makeText(this, "Ops, nenhuma carona encontrada para seu destino :(", Toast.LENGTH_LONG).show();
            } else {

                ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, listaUsuarios);
                listViewUsuarios.setAdapter(adapter);

            }

            listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Usuario usuario = (Usuario) parent.getItemAtPosition(position);
                    Intent intent = new Intent(ListarCaronaActivity.this, ReservarCaronaActivity.class);
                    intent.putExtra("infoReservaUsuario", usuario);
                    startActivity(intent);

                }
            });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("lista", usuarios);
        super.onSaveInstanceState(savedInstanceState);
    }

}
