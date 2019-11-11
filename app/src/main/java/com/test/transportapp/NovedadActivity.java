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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NovedadActivity extends AppCompatActivity {

    private EditText carrActual;
    private EditText carReemplazo;
    private EditText novedad;
    private Button registro;
    private String actual;
    private String reemplazo;
    private String noveda;

    FirebaseAuth autenticacion;
    DatabaseReference bdApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novedad);
        autenticacion= FirebaseAuth.getInstance();
        bdApp= FirebaseDatabase.getInstance().getReference();

        carrActual=(EditText)findViewById(R.id.carro_novedad);
        carReemplazo= (EditText)findViewById(R.id.carro_reemplazo);
        novedad=(EditText)findViewById(R.id.motivo);

        registro=(Button)findViewById(R.id.btRegistro);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actual=carrActual.getText().toString().trim();
                reemplazo=carReemplazo.getText().toString().trim();
                noveda=novedad.getText().toString().trim();

                if(!actual.isEmpty() && !reemplazo.isEmpty() && !noveda.isEmpty()){

                    registrarNovedad();
                    limpiarCajas();
                }
                else{
                    Toast.makeText(NovedadActivity.this, "Los campos se deben llenar por completo", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void registrarNovedad(){

        Map<String, Object> datos = new HashMap<>();
        datos.put("vehiculo_averiado",actual);
        datos.put("vehiculo_reemplazo", reemplazo);
        datos.put("motivo",noveda);

        String  id =bdApp.push().getKey();
        bdApp.child("Novedades").child(id).setValue(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                if(task1.isSuccessful()){
                    Toast.makeText(NovedadActivity.this,"SE HA REGISTRADO CORRECTAMENTE EL VEHICULO", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(NovedadActivity.this, "No se pudo registrar el vehículo en la BD", Toast.LENGTH_SHORT).show();

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
                Intent lanzar = new Intent(NovedadActivity.this,AgendaActivity.class);
                startActivity(lanzar);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void limpiarCajas(){
        carrActual.setText("");
        carReemplazo.setText("");
        novedad.setText("");
    }
}
