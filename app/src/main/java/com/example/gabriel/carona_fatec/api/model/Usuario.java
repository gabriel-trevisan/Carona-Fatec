package com.example.gabriel.carona_fatec.api.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabriel on 08/09/2017.
 */

public class Usuario implements Parcelable {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String turma;
    private int numeroCelular;
    private int perfil;
    private Rota rota;

    public Usuario(){}

    public Usuario(int id, String nome, String email, int numeroCelular, String turma, Rota rota){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.numeroCelular = numeroCelular;
        this.turma = turma;
        this.rota = rota;
    }

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

    public Usuario(int id, Rota rota){
        this.id = id;
        this.rota = rota;
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

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    public String toString(){

        return  "\n" +
                "Nome: " + this.nome + "\n" +
                "Email: " + this.email + "\n"+
                "Numero Celular: " + this.numeroCelular + "\n" +
                "Turma: " + this.turma + "\n" +
                "Horario: " + this.rota.getHorario()
                + "\n";
    }

    // Parcelable para passar objetos entre activities

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeInt(numeroCelular);
        dest.writeString(turma);
        dest.writeValue(rota);
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Usuario(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        email = in.readString();
        numeroCelular = in.readInt();
        turma = in.readString();
        rota = (Rota) in.readValue(Rota.class.getClassLoader());

    }

}
