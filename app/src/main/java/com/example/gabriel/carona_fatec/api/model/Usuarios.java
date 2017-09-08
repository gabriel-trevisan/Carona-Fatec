package com.example.gabriel.carona_fatec.api.model;

/**
 * Created by gabriel on 08/09/2017.
 */

public class Usuarios {

    private String nome;
    private String email;
    private String senha;
    private String turma;
    private int numeroCelular;
    private String perfil;
    private Rotas Rotas;

    public Usuarios(String nome, String email, String senha, String turma, int numeroCelular, String perfil){

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.turma = turma;
        this.numeroCelular = numeroCelular;
        this.perfil = perfil;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public int getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(int numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Rotas getRotas() {
        return Rotas;
    }

    public void setRotas(Rotas rotas) {
        Rotas = rotas;
    }

}
