package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AgendaActivity extends AppCompatActivity {

    private TextView txtDestino;
    private TextView txtEstado;
    private TextView txtHoraLlegada;


    private String destino;
    private String estado;
    private String horaLlegada;

    private FirebaseAuth autenticacion;
    private DatabaseReference bdApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        autenticacion = FirebaseAuth.getInstance();
        bdApp = FirebaseDatabase.getInstance().getReference();

        txtDestino = (TextView) findViewById(R.id.txtDestino);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        txtHoraLlegada = (TextView) findViewById(R.id.txtHoraLLegada);

        //btnSolicitar = (Button) findViewById(R.id.btnSolicitar);

        String id = autenticacion.getCurrentUser().getUid();
        bdApp.child("Solicitud");
        bdApp.addValueEventListener(new ValueEventListener() {
            //bdApp.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot info : dataSnapshot.getChildren()) {
                        DatosObtenidosSolicitud data = info.getValue(DatosObtenidosSolicitud.class);

                        destino = data.getDestino();
                        estado = data.getEstado();
                        horaLlegada = data.getHoraLlegada();
                        txtDestino.setText(destino);
                        txtEstado.setText(estado);
                        txtHoraLlegada.setText(horaLlegada);
                        /*if (data.getCorreo().equalsIgnoreCase(usuario) && data.getRol().equalsIgnoreCase("Admin")) {
                            Intent next = new Intent(getApplication(), MenuActivity.class);// DEBO BUSCAR PONER UNA BANDERA PARA ABRIR ACTIVIDAD
                            startActivity(next);
                        }*/

                    }

                    /*
                    destino = dataSnapshot.child("destino").getValue().toString();
                    estado = dataSnapshot.child("estado").getValue().toString();
                    horaLlegada = dataSnapshot.child("HoraLlegada").getValue().toString();
                    txtDestino.setText(destino);
                    txtEstado.setText(estado);
                    txtHoraLlegada.setText(horaLlegada);
                    */

                } else {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}