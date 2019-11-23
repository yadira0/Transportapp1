package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatosUsuarioActivity extends AppCompatActivity {

    private TextView correo;
    private EditText passwor,telefo,area;
    private Button actualiza;
    private String email, password, telefono, depto,uID;
    private DatabaseReference bdApp;
    FirebaseUser user;
    FirebaseAuth autenticacion;
    actualizarDatos datosUsuario;
    private List<actualizarDatos> actualizarDat = new ArrayList<actualizarDatos>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        bdApp= FirebaseDatabase.getInstance().getReference();
        autenticacion=FirebaseAuth.getInstance();
        user=autenticacion.getCurrentUser();
        uID=autenticacion.getCurrentUser().getUid();
        infoCajas();
        capturaDatos();

    }

    private void infoCajas() {
        bdApp.child("Usuarios").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                    actualizarDatos info =datos.getValue(actualizarDatos.class);
                    if(datos.getKey() == uID){
                        actualizarDat.add(info);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        correo.setText(actualizarDat.get(0).getCorreo());
        passwor.setText(actualizarDat.get(0).getPassword());
        telefo.setText(actualizarDat.get(0).getTelefono());
        area.setText(actualizarDat.get(0).getArea());
    }

    private void capturaDatos() {
        correo = (TextView) findViewById(R.id.campoMailUser);
        telefo = ((EditText) findViewById(R.id.campoTelefonoUser));
        area=(EditText) findViewById(R.id.campoAreaUser);
        passwor=(EditText) findViewById(R.id.campoPassUser);
    }
}
