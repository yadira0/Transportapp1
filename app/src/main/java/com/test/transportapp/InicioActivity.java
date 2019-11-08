package com.test.transportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InicioActivity extends AppCompatActivity {

    manejoCalendario manejador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        manejador = (manejoCalendario) findViewById(R.id.manejoCalendario);
    }
}
