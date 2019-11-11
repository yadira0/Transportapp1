package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText apellido2;
    private EditText cedula;
    private EditText telefono;
    private EditText area;
    private EditText correo;
    private EditText pass;
    private Spinner rol;
    private Button registro;

    private String nom;
    private String ape;
    private String apell;
    private String ced;
    private String tel;
    private String are;
    private String mail;
    private String password;
    private String tipoRol;

    FirebaseAuth autenticacion;
    DatabaseReference bdApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        autenticacion= FirebaseAuth.getInstance();
        bdApp= FirebaseDatabase.getInstance().getReference();
        capturaDatos();
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.roles,android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rol.setAdapter(adaptador);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardaDatos();
                if(!nom.isEmpty() && !ape.isEmpty() && !apell.isEmpty() && !ced.isEmpty() && !tel.isEmpty() && !are.isEmpty() && !mail.isEmpty() && !password.isEmpty()){
                    if(password.length() > 5){
                        registrarUsuario();
                    }
                    else{
                        Toast.makeText(RegistroActivity.this, "La contraseña debe tener mínimo 6 de caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegistroActivity.this, "Los campos se deben llenar por completo", Toast.LENGTH_SHORT).show();
                }
                limpiarCajas();
            }
        });
    }

    private void guardaDatos() {
        nom=nombre.getText().toString().trim();
        ape=apellido.getText().toString().trim();
        apell=apellido2.getText().toString().trim();
        ced=cedula.getText().toString().trim();
        tel=telefono.getText().toString().trim();
        are=area.getText().toString().trim();
        mail=correo.getText().toString().trim();
        password=pass.getText().toString().trim();
        tipoRol=rol.getSelectedItem().toString().trim();
    }

    private void capturaDatos() {
        nombre=(EditText)findViewById(R.id.campoNombre);
        apellido= (EditText)findViewById(R.id.campoApellido);
        apellido2=(EditText)findViewById(R.id.campoApellido2);
        cedula=(EditText)findViewById(R.id.campoCedula);
        telefono=(EditText)findViewById(R.id.campoTelefono);
        area=(EditText)findViewById(R.id.campoArea);
        correo=(EditText)findViewById(R.id.campoMail);
        pass=(EditText)findViewById(R.id.campoPass);
        rol=(Spinner)findViewById(R.id.tipo);

        registro=(Button)findViewById(R.id.btRegistrar);
    }

    public void registrarUsuario(){
        autenticacion.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("nombre",nom);
                    datos.put("apellido1", ape);
                    datos.put("apellido2", apell);
                    datos.put("cedula",ced);
                    datos.put("telefono",tel);
                    datos.put("area", are);
                    datos.put("rol", tipoRol);
                    datos.put("correo",mail);
                    datos.put("password", password);

                    String id = autenticacion.getCurrentUser().getUid();
                    bdApp.child("Usuarios").child(id).setValue(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if(task1.isSuccessful()){
                                Toast.makeText(RegistroActivity.this,"SE HA REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegistroActivity.this, "No se pudo registrar el usuario en la BD", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegistroActivity.this, "No se pudo registrar el usuario verifique que el correo este bien escrito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.boton, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.atras:
                Intent lanzar = new Intent(RegistroActivity.this,MainActivity.class);
                startActivity(lanzar);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void limpiarCajas(){
        nombre.setText("");
        apellido.setText("");
        apellido2.setText("");
        cedula.setText("");
        telefono.setText("");
        area.setText("");
        correo.setText("");
        pass.setText("");
    }
}
