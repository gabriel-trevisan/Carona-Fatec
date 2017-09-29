package com.example.gabriel.carona_fatec.api.model;

/**
 * Created by gabriel on 27/08/2017.
 */

public class Rota {

    private String rota;
    private String data;
    private String horario;
    private int distancia;

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

        return this.rota + " " + this.data + " " + this.horario;
    }

}
