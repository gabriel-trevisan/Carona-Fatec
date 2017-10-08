package com.example.gabriel.carona_fatec.api.model;

/**
 * Created by gabriel on 07/10/2017.
 */

public class Reserva {

    private int id;
    private int idRota;
    private int idUsuario;
    private String status;
    private Usuario usuario;
    private Rota rota;

    public Reserva(int idRota, int idUsuario, String status){
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    public String toString(){

        return  "\n" +
                "Nome: " + this.usuario.getNome() + "\n" +
                "Email: " + this.usuario.getEmail() + "\n"+
                "Numero Celular: " + this.usuario.getNumeroCelular() + "\n" +
                "Turma: " + this.usuario.getTurma() + "\n" +
                "Horario: " + this.rota.getHorario() + "\n" +
                "Status: " + this.status
                + "\n";

    }

}
