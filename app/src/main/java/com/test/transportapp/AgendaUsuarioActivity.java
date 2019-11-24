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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AgendaUsuarioActivity extends AppCompatActivity {
    private List<DatosObtenidosSolicitud> datosAgenda1 = new ArrayList<DatosObtenidosSolicitud>();
    ArrayAdapter<DatosObtenidosSolicitud> adaptador1;
    private List<String> llaves = new ArrayList<>();
    private DatabaseReference bdApp;
    private String key;
    private TextView origen1, destino1;
    private TextView hora1, fecha1;

    private ListView listado;

    DatosObtenidosSolicitud viajeSeleccionado1;

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
        viajeSeleccionado1 = (DatosObtenidosSolicitud) parent.getItemAtPosition(position);
        destino1.setText(viajeSeleccionado1.getDestino());
        hora1.setText(viajeSeleccionado1.getHora());
        fecha1.setText(viajeSeleccionado1.getFecha());
        key = llaves.get(position);
    }

    private void capturaDatos() {
        destino1 = findViewById(R.id.campoDestino);
        hora1 = findViewById(R.id.campoHora);
        fecha1 = findViewById(R.id.campoFecha);

        listado = findViewById(R.id.listaInfo);
    }

    public void listarDatos() {
        bdApp.child("Recorridos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosAgenda1.clear();
                for (DataSnapshot datos : dataSnapshot.getChildren()) {
                    DatosObtenidosSolicitud info = datos.getValue(DatosObtenidosSolicitud.class);
                    datosAgenda1.add(info);
                    adaptador1 = new ArrayAdapter<DatosObtenidosSolicitud>(AgendaUsuarioActivity.this, android.R.layout.simple_list_item_1, datosAgenda1);
                    listado.setAdapter(adaptador1);
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
                Intent lanzar = new Intent(AgendaUsuarioActivity.this, UsuarioActivity.class);
                startActivity(lanzar);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
