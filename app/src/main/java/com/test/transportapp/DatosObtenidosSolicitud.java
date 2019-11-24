package com.test.transportapp;

import androidx.annotation.NonNull;

public class DatosObtenidosSolicitud {

    String origen, destino, hora, fecha;


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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @NonNull
    @Override
    public String toString() {

        return origen;
    }
}
