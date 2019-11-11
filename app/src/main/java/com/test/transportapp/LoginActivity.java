package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth autenticacion;
    private String user, pass;
    private DatabaseReference bdApp;
    private String rol;
    String informa;
    private EditText email;
    private EditText password;
    private Button login;
    private Integer flag=0;
    datosObtenidosLogin data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        autenticacion = FirebaseAuth.getInstance();
        bdApp= FirebaseDatabase.getInstance().getReference();


        email = (EditText) findViewById(R.id.campoCorreoL);
        password = (EditText) findViewById(R.id.campoPasswordL);
        login = (Button) findViewById(R.id.btnRegistrarL);

        //user = email.getText().toString().trim();
        //pass = password.getText().toString().trim();

        //      progreso = new ProgressDialog(this);
        login.setOnClickListener(this);

    }

    public void loguearUsuario() {


        user = email.getText().toString().trim();
        pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, "se debe ingresar info", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "se debe ingresar info", Toast.LENGTH_LONG).show();
            return;
        }

        autenticacion.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    traerDatos(user);
                }

                else {

                    Toast.makeText(LoginActivity.this, "no se pudo loguear usuario y/o contraseña erróneas", Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private void traerDatos(final String usuario) {
        bdApp.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot info : dataSnapshot.getChildren()) {

                    data = info.getValue(datosObtenidosLogin.class);
                    String correoMail = data.getCorreo();
                    String roles = data.getRol();
                    if (data.getCorreo().equalsIgnoreCase(usuario)) {
                        if(roles.equalsIgnoreCase("Admin")){
                            flag=1;
                            informa = data.getNombre();
                        }
                        else if(roles.equalsIgnoreCase("Usuario")){
                            flag=2;
                            informa = data.getNombre();
                        }
                        else{
                            flag=3;
                            informa = data.getNombre();
                        }
                    }
                }

                if(flag==1){
                   // data.setNombre(informa);

                    Intent next = new Intent(getApplication(), MainActivity.class);// DEBO BUSCAR PONER UNA BANDERA PARA ABRIR ACTIVIDAD
                    startActivity(next);
                }
                else if(flag==2){
                    //data.setNombre(informa);

                }
                else if (flag==3){
                    //data.setNombre(informa);
                    Intent next = new Intent(getApplication(), AgendaActivity.class);
                    startActivity(next);
                }
                else{
                   Toast.makeText(LoginActivity.this,"No estás registrado comunicate con el administrador",Toast.LENGTH_SHORT).show();
                    Intent next = new Intent(getApplication(), LoginActivity.class);
                    startActivity(next);
                   /* Intent next = new Intent(getApplication(), MainActivity.class);// DEBO BUSCAR PONER UNA BANDERA PARA ABRIR ACTIVIDAD
                    startActivity(next);*/
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        loguearUsuario();
    }

}
