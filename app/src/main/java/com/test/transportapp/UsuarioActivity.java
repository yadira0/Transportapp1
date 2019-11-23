package com.test.transportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.test.transportapp.notificaciones.APIService;
import com.test.transportapp.notificaciones.Client;
import com.test.transportapp.notificaciones.Token;

public class UsuarioActivity extends AppCompatActivity {

    private Button actualizar;
    private Button verAgen;
    private Button agendar;
    private FirebaseAuth autenticacion;
    String agedarRecorrido, verAgenda, actualizarDatos, mUID;
    APIService apiService;
    private Boolean notify=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
        autenticacion=FirebaseAuth.getInstance();
        mUID=autenticacion.getCurrentUser().getUid();
        setContentView(R.layout.activity_usuario);

        agendar=(Button) findViewById(R.id.agenda_recor);
        verAgen=(Button) findViewById(R.id.ver_book);
        actualizar=(Button) findViewById(R.id.actuali_datos);
        updateToken(FirebaseInstanceId.getInstance().getToken());

        agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                Intent lanzar= new Intent(UsuarioActivity.this,InicioActivity.class);
                startActivity(lanzar);
            }
        });

        verAgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lanzar= new Intent(UsuarioActivity.this,AgendaConductor.class);
                startActivity(lanzar);

            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lanzar = new Intent(UsuarioActivity.this,InicioActivity.class);
            }
        });
    }

    public void updateToken (String tken){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mtoken = new Token(tken);
        ref.child(mUID).setValue(mtoken);
    }
}
