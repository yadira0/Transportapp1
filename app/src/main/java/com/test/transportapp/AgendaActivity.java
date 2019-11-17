package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.test.transportapp.notificaciones.APIService;
import com.test.transportapp.notificaciones.Client;
import com.test.transportapp.notificaciones.Token;

public class AgendaActivity extends AppCompatActivity {

 private Button noveda;
 private Button verAgen;
 private FirebaseAuth autenticacion;
 String novedad, agendaConduc, mUID;
 APIService apiService;
 private Boolean notify=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
        autenticacion=FirebaseAuth.getInstance();
        mUID=autenticacion.getCurrentUser().getUid();
        setContentView(R.layout.activity_agenda);
        noveda=(Button) findViewById(R.id.novedad);
        verAgen=(Button) findViewById(R.id.ver_Agenda);
        updateToken(FirebaseInstanceId.getInstance().getToken());

        noveda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                Intent lanzar= new Intent(AgendaActivity.this,NovedadActivity.class);
                startActivity(lanzar);
            }
        });

        verAgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lanzar= new Intent(AgendaActivity.this,AgendaConductor.class);
                startActivity(lanzar);

            }
        });
    }

    public void updateToken (String tken){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mtoken = new Token(tken);
        ref.child(mUID).setValue(mtoken);
    }

}