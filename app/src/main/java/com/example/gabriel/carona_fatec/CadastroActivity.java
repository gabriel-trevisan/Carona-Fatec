package com.example.gabriel.carona_fatec;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Lista de perfil
        Spinner listaPerfil = (Spinner) findViewById(R.id.spinner_perfil);
        ArrayAdapter<CharSequence> adapterPerfil = ArrayAdapter.createFromResource(this, R.array.perfil_array, android.R.layout.simple_spinner_item);
        adapterPerfil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Lista de turmas
        Spinner listaTurmas = (Spinner) findViewById(R.id.spinner_turmas);
        ArrayAdapter<CharSequence> adapterTurmas = ArrayAdapter.createFromResource(this, R.array.turmas_array, android.R.layout.simple_spinner_item);
        adapterTurmas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listaPerfil.setAdapter(adapterPerfil);
        listaTurmas.setAdapter(adapterTurmas);

    }

}
