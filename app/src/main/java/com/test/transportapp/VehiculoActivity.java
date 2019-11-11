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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class VehiculoActivity extends AppCompatActivity {
    private EditText marca;
    private EditText modelo;
    private EditText placa;
    private EditText nPasajeros;
    private EditText area;
    private Button registrar;

    private String mar;
    private String mode;
    private String plac;
    private String pasajeros;
    private String areaVehiculo;

    FirebaseAuth autenticacion;
    DatabaseReference bdApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);
        autenticacion= FirebaseAuth.getInstance();
        bdApp= FirebaseDatabase.getInstance().getReference();

        marca=(EditText)findViewById(R.id.campoMarca);
        modelo= (EditText)findViewById(R.id.campoModelo);
        placa=(EditText)findViewById(R.id.campoPlaca);
        nPasajeros=(EditText)findViewById(R.id.campoPasajeros);
        area=(EditText)findViewById(R.id.areaCarro);

        registrar=(Button)findViewById(R.id.btRegistro);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mar=marca.getText().toString().trim();
                mode=modelo.getText().toString().trim();
                plac=placa.getText().toString().trim();
                pasajeros=nPasajeros.getText().toString().trim();
                areaVehiculo=area.getText().toString().trim();

                if(!mar.isEmpty() && !mode.isEmpty() && !plac.isEmpty() && !pasajeros.isEmpty()){

                   registrarVehiculo();
                   limpiarCajas();
                }
                else{
                    Toast.makeText(VehiculoActivity.this, "Los campos se deben llenar por completo", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void registrarVehiculo(){

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("marca",mar);
                    datos.put("modelo", mode);
                    datos.put("placa", plac);
                    datos.put("no Pasajeros",pasajeros);
                    datos.put("area_vehiculo",areaVehiculo);

                    String  id =bdApp.push().getKey();
                    bdApp.child("Vehiculos").child(id).setValue(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if(task1.isSuccessful()){
                                Toast.makeText(VehiculoActivity.this,"SE HA REGISTRADO CORRECTAMENTE EL VEHICULO", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(VehiculoActivity.this, "No se pudo registrar el vehículo en la BD", Toast.LENGTH_SHORT).show();

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
                Intent lanzar = new Intent(VehiculoActivity.this,MainActivity.class);
                startActivity(lanzar);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void limpiarCajas(){
        marca.setText("");
        modelo.setText("");
        placa.setText("");
        nPasajeros.setText("");
        area.setText("");
    }

}


