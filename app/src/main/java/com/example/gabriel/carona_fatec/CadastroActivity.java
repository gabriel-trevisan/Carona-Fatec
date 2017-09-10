package com.example.gabriel.carona_fatec;

import android.app.ProgressDialog;
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

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;
    private EditText edtNumeroCelular;
    private Spinner listaPerfil;
    private Spinner listaTurmas;
    private String turmas = null;
    private String perfil;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Lista de perfil
        listaPerfil = (Spinner) findViewById(R.id.spinner_perfil);
        ArrayAdapter<CharSequence> adapterPerfil = ArrayAdapter.createFromResource(this, R.array.perfil_array, android.R.layout.simple_spinner_item);
        adapterPerfil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Lista de turmas
        listaTurmas = (Spinner) findViewById(R.id.spinner_turmas);
        ArrayAdapter<CharSequence> adapterTurmas = ArrayAdapter.createFromResource(this, R.array.turmas_array, android.R.layout.simple_spinner_item);
        adapterTurmas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listaPerfil.setAdapter(adapterPerfil);
        listaTurmas.setAdapter(adapterTurmas);

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

        listaTurmas.setOnItemSelectedListener(this);
        listaPerfil.setOnItemSelectedListener(this);

        if(TextUtils.isEmpty((nome))) {
            Toast.makeText(this, "Por favor, insira o seu nome.", Toast.LENGTH_SHORT).show();
        }
        else if(!validarEmail(email)) {
            Toast.makeText(this, "Por favor, insira um email válido.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor, insira uma senha.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmarSenha)) {
            Toast.makeText(this, "Por favor, confirme sua senha.", Toast.LENGTH_SHORT).show();
        }
        else if(!(senha.equals(confirmarSenha))) {
            Toast.makeText(this, "As senhas não são iguais, digite duas senhas identicas.", Toast.LENGTH_SHORT).show();
        }
        else if(turmas == null) {
            Toast.makeText(this, "Por favor, escolha uma turma.", Toast.LENGTH_SHORT).show();
        }
        else if(numeroCelular.isEmpty()){
            Toast.makeText(this, "Por favor, insira um número de celular.", Toast.LENGTH_SHORT).show();
        }
        else if(perfil == null){
            Toast.makeText(this, "Por favor, escolha um perfil.", Toast.LENGTH_SHORT).show();
        }
        else{
            Usuario usuario = new Usuario(email, nome, Integer.parseInt(numeroCelular),tranformarPerfilInt(perfil), senha, turmas);
            enviarRequesicaoGetApi(usuario);
        }
    }

    private void enviarRequesicaoGetApi(final Usuario usuario){

        dialog = new ProgressDialog(CadastroActivity.this);
        dialog.setMessage("Esperando resposta do servidor...");
        dialog.setCancelable(false);
        dialog.show();

        UsuarioServices usuarioServices = UsuarioServices.retrofit.create(UsuarioServices.class);
        Call<Boolean> call = usuarioServices.selecionarUsuario(usuario.getEmail());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(dialog.isShowing()) {
                    //Se a resposta for verdadeira, existe email cadastrado
                    if (response.body()) {
                        dialog.dismiss();
                        Toast.makeText(CadastroActivity.this, "Email já cadastrado no app, digite outro email.", Toast.LENGTH_SHORT).show();
                    } else {
                        //Requesição de inserção
                        enviarRequisicaoPostApi(usuario);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(CadastroActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void enviarRequisicaoPostApi(Usuario usuario) {

        /* Testa retorno http
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);*/
        //End

        UsuarioServices usuarioServices = UsuarioServices.retrofit.create(UsuarioServices.class);
        Call<Boolean> call = usuarioServices.inserirUsuario(usuario);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (dialog.isShowing()) {
                        //Se a resposta do servidor for verdadeira (Foi inserido com sucesso)
                    if (response.body()) {
                        dialog.dismiss();
                        Toast.makeText(CadastroActivity.this, "Usuário inserido com sucesso, realize o seu login!", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(CadastroActivity.this, "Erro ao inserir no banco de dados", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(CadastroActivity.this, "Erro ao conectar a API, verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
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

    //Método de apoio nesta classe
    //Tranforma perfil em int
    public int tranformarPerfilInt(String perfil){

        int perfilTransformado = 0;

        if(perfil.equals("Oferecer carona")){
            perfilTransformado = 1;
        }
        else if(perfil.equals("Buscar carona")){
            perfilTransformado = 0;
        }
        return perfilTransformado;
    }

    //A classe CadastroActivity realiza um contrato com a OnItemSelectedListener para suportar os métodos abaixo.

    //Pega o item selecionado nas spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spinner_turmas:
                if (position == 0) {
                    // Sem turma
                    turmas = null;
                }
                else {
                    turmas = parent.getSelectedItem().toString();
                }
                break;
            case R.id.spinner_perfil:
                if (position == 0) {
                    //Sem perfil
                    perfil = null;
                }
                else {
                    perfil = parent.getSelectedItem().toString();
                }
                break;
        }
    }

    //Nada selecionado na spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch(parent.getId()){
            case R.id.spinner_turmas:
                Toast.makeText(this, "Por favor, selecione uma turma", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_perfil:
                Toast.makeText(this, "Por favor, selecione um perfil", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
