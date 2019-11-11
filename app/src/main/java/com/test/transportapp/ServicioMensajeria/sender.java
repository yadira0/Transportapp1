package com.test.transportapp.ServicioMensajeria;

public class sender {

    private data data;
    private String to;

    public sender() {
    }

    public sender(com.test.transportapp.ServicioMensajeria.data data, String to) {
        this.data = data;
        this.to = to;
    }

    public com.test.transportapp.ServicioMensajeria.data getData() {
        return data;
    }

    public void setData(com.test.transportapp.ServicioMensajeria.data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
