package com.test.transportapp;

public class infoMensaje {

    String rol,uid;
    String nombre,area;

    public infoMensaje(String rol, String uid, String nombre, String area) {
        this.rol = rol;
        this.uid = uid;
        this.nombre = nombre;
        this.area = area;
    }

    public infoMensaje() {
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
