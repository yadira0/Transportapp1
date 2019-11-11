package com.test.transportapp;

import androidx.annotation.NonNull;

public class DatosObtenidosSolicitud {

    String origen, destino, hora, fechaComplete;


    public DatosObtenidosSolicitud() {

    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFechaComplete() {
        return fechaComplete;
    }

    public void setFechaComplete(String fechaComplete) {
        this.fechaComplete = fechaComplete;
    }

    @NonNull
    @Override
    public String toString() {

        return origen;
    }
}
