package com.test.transportapp;

import androidx.annotation.NonNull;

public class actualizarDatos {
    String nombre;
    String apellido1;
    String apellido2;
    String cedula;
    String correo;
    String password;
    String telefono;
    String area;
    String rol;

    public actualizarDatos() {

    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getCorreo() {

        return correo;
    }

    public void setCorreo(String correo) {

        this.correo = correo;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getTelefono() {

        return telefono;
    }

    public void setTelefono(String telefono) {

        this.telefono = telefono;
    }

    public String getArea() {

        return area;
    }

    public void setArea(String area) {

        this.area = area;
    }


    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @NonNull
    @Override
    public String toString() {

        return nombre;
    }
}
