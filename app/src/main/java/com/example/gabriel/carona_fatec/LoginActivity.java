package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Usuario;
import com.example.gabriel.carona_fatec.api.service.UsuarioServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtSenha;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmailLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenhaLogin);

    }

    public void logar(View v) {

        //Separar valor dos edts em duas variáveis
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        //instacia o objeto usuario
        final Usuario usuario = new Usuario(email, senha);

        //cria progressbar
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Logando...");
        dialog.setCancelable(false);
        dialog.show();

        //prepara retrofit para requisição
        UsuarioServices usuarioServices = UsuarioServices.retrofit.create(UsuarioServices.class);
        Call<Boolean> call = usuarioServices.validarUsuario(usuario);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (dialog.isShowing()) {
                    //Se a resposta do servidor for verdadeira (Usuário válido!)
                    if (response.body()) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Seja bem-vindo!", Toast.LENGTH_SHORT).show();
                        Intent intentCarona = new Intent(LoginActivity.this, MainActivity.class);

                        //SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("usuarioLoginEmail", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("emailUsuario", usuario.getEmail());
                        editor.apply();

                        startActivity(intentCarona);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Usuário inválido ou senha incorreta.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Erro ao conectar no servidor, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void novoCadastro(View v){

        Intent novoCadastro = new Intent(this, CadastroActivity.class);
        startActivity(novoCadastro);

    }

}
