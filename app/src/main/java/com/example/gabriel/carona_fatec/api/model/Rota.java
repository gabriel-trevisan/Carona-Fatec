package com.example.gabriel.carona_fatec.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by gabriel on 27/08/2017.
 */

public class Rota implements Parcelable {

    private int id;
    private String rota, data, horario, atual, destino;
    private int distancia;

    public Rota(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRota() {
        return rota;
    }

    public void setRota(String rota) {
        this.rota = rota;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public String getAtual() {
        return atual;
    }

    public void setAtual(String atual) {
        this.atual = atual;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String toString(){

        return  "\n" +
                "Saída: " + this.atual + "\n" +
                "Destino: " + this.destino + "\n" +
                "Data: " + this.data + "\n" +
                "Horário: " + this.horario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(rota);
        dest.writeString(data);
        dest.writeString(horario);
        dest.writeInt(distancia);
        dest.writeString(atual);
        dest.writeString(destino);
    }

    public static final Parcelable.Creator<Rota> CREATOR = new Parcelable.Creator<Rota>() {
        public Rota createFromParcel(Parcel in) {
            return new Rota(in);
        }

        public Rota[] newArray(int size) {
            return new Rota[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Rota(Parcel in) {
        id = in.readInt();
        rota = in.readString();
        data = in.readString();
        horario = in.readString();
        distancia = in.readInt();
        atual = in.readString();
        destino = in.readString();
    }

}
