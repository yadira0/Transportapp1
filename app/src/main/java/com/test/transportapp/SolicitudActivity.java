package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SolicitudActivity extends AppCompatActivity {

    private List<DatosObtenidosSolicitud> datosAgenda = new ArrayList<DatosObtenidosSolicitud>();
    ArrayAdapter<DatosObtenidosSolicitud> adaptador;
    private List<String> llaves = new ArrayList<>();
    private DatabaseReference bdApp;
    //private String key;
    private TextView origen, destino;
    private TextView hora, fecha;

    private ListView listado;

    DatosObtenidosSolicitud viajeSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);
        bdApp = FirebaseDatabase.getInstance().getReference();
        capturaDatos();
        listarDatos();
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                infoCajas(parent, position);
            }
        });
    }

    private void infoCajas(AdapterView<?> parent, int position) {
        viajeSeleccionado = (DatosObtenidosSolicitud) parent.getItemAtPosition(position);
        destino.setText(viajeSeleccionado.getDestino());
        hora.setText(viajeSeleccionado.getHora());
        fecha.setText(viajeSeleccionado.getFecha());
      //  key = llaves.get(position);
    }

    private void capturaDatos() {
        destino = findViewById(R.id.campoDestino);
        hora = findViewById(R.id.campoHora);
        fecha = findViewById(R.id.campoFecha);

        listado = findViewById(R.id.listaInfo);
    }

    public void listarDatos() {
        bdApp.child("Recorridos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosAgenda.clear();
                for (DataSnapshot datos : dataSnapshot.getChildren()) {
                    DatosObtenidosSolicitud info = datos.getValue(DatosObtenidosSolicitud.class);
                    datosAgenda.add(info);
                    adaptador = new ArrayAdapter<DatosObtenidosSolicitud>(SolicitudActivity.this, android.R.layout.simple_list_item_1, datosAgenda);
                    listado.setAdapter(adaptador);
                    llaves.add(datos.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        switch (item.getItemId()) {
            case R.id.atras:
                Intent lanzar = new Intent(SolicitudActivity.this, MainActivity.class);
                startActivity(lanzar);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}

