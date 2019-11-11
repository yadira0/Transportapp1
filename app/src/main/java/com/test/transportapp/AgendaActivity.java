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

public class AgendaActivity extends AppCompatActivity {

 private Button noveda;
 private Button verAgen;
 String novedad, agendaConduc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        noveda=(Button) findViewById(R.id.novedad);
        verAgen=(Button) findViewById(R.id.ver_Agenda);

        noveda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}