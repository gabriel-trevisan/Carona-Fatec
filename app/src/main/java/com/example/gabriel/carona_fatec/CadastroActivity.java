package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.api.model.Usuario;
import com.example.gabriel.carona_fatec.api.service.UsuarioServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;
    private EditText edtNumeroCelular;
    private Spinner listaTurmas;
    private String turmas = "";
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Lista de turmas
        listaTurmas = (Spinner) findViewById(R.id.spinner_turmas);
        ArrayAdapter<CharSequence> adapterTurmas = ArrayAdapter.createFromResource(this, R.array.turmas_array, android.R.layout.simple_spinner_item);
        adapterTurmas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listaTurmas.setAdapter(adapterTurmas);

        listaTurmas.setOnItemSelectedListener(this);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        edtConfirmaSenha = (EditText) findViewById(R.id.edtConfirmarSenha);
        edtNumeroCelular = (EditText) findViewById(R.id.edtNumeroCelular);

    }

    public void cadastrarUsuario(View v) {

        String nome = edtNome.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String confirmarSenha = edtConfirmaSenha.getText().toString();
        String numeroCelular = edtNumeroCelular.getText().toString();

        if(TextUtils.isEmpty((nome))) {
            Toast.makeText(this, "Por favor, insira o seu nome.", Toast.LENGTH_LONG).show();
        }
        else if(!validarEmail(email)) {
            Toast.makeText(this, "Por favor, insira um email válido.", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor, insira uma senha.", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(confirmarSenha)) {
            Toast.makeText(this, "Por favor, confirme sua senha.", Toast.LENGTH_LONG).show();
        }
        else if(!(senha.equals(confirmarSenha))) {
            Toast.makeText(this, "As senhas não são iguais, digite duas senhas identicas.", Toast.LENGTH_LONG).show();
        }
        else if(numeroCelular.isEmpty()){
            Toast.makeText(this, "Por favor, insira um número de celular.", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(turmas)) {
            Toast.makeText(this, "Por favor, escolha uma turma.", Toast.LENGTH_LONG).show();
        }
        else{
            Usuario usuario = new Usuario(email, nome, numeroCelular, senha, turmas);
            enviarRequesicaoGetApi(usuario);
        }
    }

    private void enviarRequesicaoGetApi(final Usuario usuario){

        dialog = new ProgressDialog(CadastroActivity.this);
        dialog.setMessage("Esperando resposta do servidor...");
        dialog.setCancelable(false);
        dialog.show();

        UsuarioServices usuarioServices = UsuarioServices.retrofit.create(UsuarioServices.class);
        Call<Usuario> call = usuarioServices.getUsuario(usuario.getEmail());

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(dialog.isShowing()) {
                    //Se api retornar null, significa que nenhum email foi encontrado.
                    if (response.body() == null) {
                        //Requesição de inserção
                        enviarRequisicaoPostApi(usuario);
                    } else if (usuario.getEmail().equals(response.body().getEmail())) {
                        dialog.dismiss();
                        Toast.makeText(CadastroActivity.this, "Email já cadastrado no app, digite outro email.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(CadastroActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void enviarRequisicaoPostApi(Usuario usuario) {

        UsuarioServices usuarioServices = UsuarioServices.retrofit.create(UsuarioServices.class);
        Call<Boolean> call = usuarioServices.inserirUsuario(usuario);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (dialog.isShowing()) {
                        //Se a resposta do servidor for verdadeira (Foi inserido com sucesso)
                    if (response.body()) {
                        dialog.dismiss();
                        Toast.makeText(CadastroActivity.this, "Usuário inserido com sucesso, realize o seu login!", Toast.LENGTH_LONG).show();
                        Intent intentLogin = new Intent(CadastroActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(CadastroActivity.this, "Erro ao inserir no banco de dados", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(CadastroActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Método de apoio neste classe
    public final static boolean validarEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //A classe CadastroActivity realiza um contrato com a OnItemSelectedListener para suportar os métodos abaixo.

    //Pega o item selecionado na spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        turmas = parent.getSelectedItem().toString();

    }

    //Nada selecionado na spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        parent.setSelection(0);

    }

}
