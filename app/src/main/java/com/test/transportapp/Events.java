package com.test.transportapp;

public class Events {
    String destino, dia, hora, mes, year, fecha, origen;

    public Events(String destino, String dia, String hora, String mes, String year, String fecha, String origen) {
        this.destino = destino;
        this.dia = dia;
        this.hora = hora;
        this.mes = mes;
        this.year = year;
        this.fecha=fecha;
        this.origen=origen;
    }

    public String getDestino() {

        return destino;
    }

    public void setDestino(String destino) {

        this.destino = destino;
    }

    public String getDia() {

        return dia;
    }

    public void setDia(String dia) {

        this.dia = dia;
    }

    public String getHora() {

        return hora;
    }

    public void setHora(String hora) {

        this.hora = hora;
    }

    public String getMes() {

        return mes;
    }

    public void setMes(String mes) {

        this.mes = mes;
    }

    public String getYear() {

        return year;
    }

    public void setYear(String year) {

        this.year = year;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
