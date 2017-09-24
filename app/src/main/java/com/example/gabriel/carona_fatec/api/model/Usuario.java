package com.example.gabriel.carona_fatec.api.model;

/**
 * Created by gabriel on 08/09/2017.
 */

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String turma;
    private int numeroCelular;
    private int perfil;
    private String rota;

    public Usuario(){};

    public Usuario(String email, String nome, int numeroCelular, int perfil, String senha, String turma){
        this.email = email;
        this.nome = nome;
        this.numeroCelular = numeroCelular;
        this.perfil = perfil;
        this.senha = senha;
        this.turma = turma;
    }

    public Usuario(String email, String senha){
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getRota() {
        return rota;
    }

    public void setRota(String rota) {
        this.rota = rota;
    }

    public String toString(){

        return this.id + " " + this.rota;
    }

}
