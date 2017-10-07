package com.example.gabriel.carona_fatec.api.model;

/**
 * Created by gabriel on 07/10/2017.
 */

public class Reserva {

    private int id;
    private int idRota;
    private int idUsuario;
    private int status;

    public Reserva(int idRota, int idUsuario, int status){
        this.idRota = idRota;
        this.idUsuario = idUsuario;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRota() {
        return idRota;
    }

    public void setIdRota(int idRota) {
        this.idRota = idRota;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
