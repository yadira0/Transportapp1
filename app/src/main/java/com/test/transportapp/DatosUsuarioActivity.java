package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    //private String email, pass, cel, are, nomb, cc, rolin, ape1,ape2,uID;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference bdApp;
    FirebaseUser user;
    FirebaseAuth autenticacion;
    actualizarDatos datosUsuario;
    private List<String> actualizarDat = new ArrayList<String>();
    String key1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        bdApp= FirebaseDatabase.getInstance().getReference();
        autenticacion=FirebaseAuth.getInstance();
        user=autenticacion.getCurrentUser();
        database =FirebaseDatabase.getInstance();
        reference=database.getReference("Usuarios").child(user.getUid());

        //uID=autenticacion.getCurrentUser().getUid();
        infoCajas();
        actualiza=(Button) findViewById(R.id.btActualizarDat);
        actualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturaDatos();
            }
        });



    }

    private void infoCajas() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    actualizarDat.add((String) dataSnapshot.child("correo").getValue());

                    actualizarDat.add((String) dataSnapshot.child("password").getValue());

                    actualizarDat.add((String) dataSnapshot.child("telefono").getValue());

                    actualizarDat.add((String) dataSnapshot.child("area").getValue());

                    actualizarDat.add((String) dataSnapshot.child("nombre").getValue());
                    actualizarDat.add((String) dataSnapshot.child("apellido1").getValue());
                    actualizarDat.add((String) dataSnapshot.child("apellido2").getValue());
                    actualizarDat.add((String) dataSnapshot.child("rol").getValue());
                    actualizarDat.add((String) dataSnapshot.child("cedula").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        correo.setText(actualizarDat.get(0));
        passwor.setText(actualizarDat.get(0));
        telefo.setText(actualizarDat.get(0));
        area.setText(actualizarDat.get(0));

    }

    private void capturaDatos() {
        correo = (TextView) findViewById(R.id.campoMailUser);
        telefo = ((EditText) findViewById(R.id.campoTelefonoUser));
        area=(EditText) findViewById(R.id.campoAreaUser);
        passwor=(EditText) findViewById(R.id.campoPassUser);
    }

    /*private void actualizar(){

        if(correo.getText().toString().trim().equalsIgnoreCase(email) && telefo.getText().toString().trim().equalsIgnoreCase(cel)
                && area.getText().toString().trim().equalsIgnoreCase(are) && passwor.getText().toString().trim().equalsIgnoreCase(pass)) {

            Toast.makeText(this, "DEBE MODIFICAR AL MENOS UN CAMPO", Toast.LENGTH_SHORT).show();
        }
        else{

            actualizarDatos actualizacion = new actualizarDatos();
            actualizacion.setPassword(passwor.getText().toString().trim());
            actualizacion.setTelefono(telefo.getText().toString().trim());
            actualizacion.setArea(area.getText().toString().trim());
            actualizacion.setNombre(nomb);
            actualizacion.setApellido1(ape1);
            actualizacion.setApellido2(ape2);
            actualizacion.setCedula(cc);
            actualizacion.setRol(rolin);

            bdApp.child("Usuarios").child(key1).setValue(actualizacion);
            user.updatePassword(passwor.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });

            Toast.makeText(this, "DATOS ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();

        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.boton, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.atras:
                Intent lanzar = new Intent(DatosUsuarioActivity.this,UsuarioActivity.class);
                startActivity(lanzar);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
