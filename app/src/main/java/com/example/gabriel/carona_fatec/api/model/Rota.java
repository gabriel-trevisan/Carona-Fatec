package com.example.gabriel.carona_fatec.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by gabriel on 27/08/2017.
 */

public class Rota implements Parcelable {

    private int id;
    private String rota;
    private String data;
    private String horario;
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

    public String toString(){

        return  "\n" +
                "Data: " + this.data + "\n" +
                "Horário: " + this.horario +
                "\n";
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
    }

}
