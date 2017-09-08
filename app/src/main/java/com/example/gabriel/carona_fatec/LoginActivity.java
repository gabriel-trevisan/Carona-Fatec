package com.example.gabriel.carona_fatec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void novoCadastro(View v){

        Intent novoCadastro = new Intent(this, CadastroActivity.class);
        startActivity(novoCadastro);

    }

}
