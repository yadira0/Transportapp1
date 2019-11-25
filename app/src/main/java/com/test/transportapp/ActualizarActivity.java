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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.OnDelegateCreatedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActualizarActivity extends AppCompatActivity {

    private List<actualizarDatos> actualizarD = new ArrayList<actualizarDatos>();
    ArrayAdapter<actualizarDatos> adaptador;
    private List<String> llaves= new ArrayList<>();
    private DatabaseReference bdApp;
    private String key;
    private String tipoRolAct;
    private TextView mail;
    private EditText nombre;
    private EditText apel1;
    private EditText tel,apelli2;
    private EditText are,rol,cc;
    private ListView lista;
    private Spinner rolActual;
    FirebaseUser user;
    FirebaseAuth autenticacion;
    actualizarDatos usuarioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        bdApp=FirebaseDatabase.getInstance().getReference();
        autenticacion=FirebaseAuth.getInstance();
        capturaDatos();
        listarDatos();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                infoCajas(parent, position);
            }
        });
    }

    private void infoCajas(AdapterView<?> parent, int position) {

        usuarioSeleccionado =(actualizarDatos) parent.getItemAtPosition(position);
        mail.setText(usuarioSeleccionado.getCorreo());
        tel.setText(usuarioSeleccionado.getTelefono());
        are.setText(usuarioSeleccionado.getArea());

        key=llaves.get(position);
    }

    private void capturaDatos() {
        mail=findViewById(R.id.campoMailAc);
        tel=findViewById(R.id.campoTelefonoAc);
        are=findViewById(R.id.campoAreaAc);
        lista=findViewById(R.id.listaDatos);

    }

    public void listarDatos(){
        final String usuar=autenticacion.getCurrentUser().getUid();

        bdApp.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                actualizarD.clear();
                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                    actualizarDatos info =datos.getValue(actualizarDatos.class);

                        actualizarD.add(info);
                        adaptador= new ArrayAdapter<actualizarDatos>(ActualizarActivity.this,android.R.layout.simple_list_item_1, actualizarD);
                        lista.setAdapter(adaptador);
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
        getMenuInflater().inflate(R.menu.administar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atras:
                Intent lanzar = new Intent(ActualizarActivity.this,MainActivity.class);
                startActivity(lanzar);
                break;

            case R.id.actualzar:
                actualizaDatos();
                limpiarCajas();
                break;

            case R.id.eliminar:

                if(key!=null) {

                    bdApp.child("Usuarios").child(key).removeValue();
                    Toast.makeText(this, "DATOS ELIMINADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    limpiarCajas();
                }
                else{
                    Toast.makeText(this, "DEBE SELECCIONAR UN USUARIO A ELIMINAR", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void actualizaDatos() {

        if(usuarioSeleccionado==null){
            Toast.makeText(this, "DEBE SELECCIONAR UN USUARIO", Toast.LENGTH_SHORT).show();
        }

        else if(mail.getText().toString().trim().equalsIgnoreCase(usuarioSeleccionado.getCorreo()) && tel.getText().toString().trim().equalsIgnoreCase(usuarioSeleccionado.getTelefono()) && are.getText().toString().trim().equalsIgnoreCase(usuarioSeleccionado.getArea())) {
            Toast.makeText(this, "DEBE MODIFICAR AL MENOS UN CAMPO", Toast.LENGTH_SHORT).show();
        }
        else{

            actualizarDatos actual = new actualizarDatos();
            actual.setCorreo(usuarioSeleccionado.getCorreo());
            actual.setPassword(usuarioSeleccionado.getPassword());
            actual.setTelefono(tel.getText().toString().trim());
            actual.setArea(are.getText().toString().trim());
            actual.setNombre(usuarioSeleccionado.getNombre());
            actual.setApellido1(usuarioSeleccionado.getApellido1());
            actual.setApellido2(usuarioSeleccionado.getApellido2());
            actual.setCedula(usuarioSeleccionado.getCedula());
            actual.setRol(usuarioSeleccionado.getRol());
            bdApp.child("Usuarios").child(key).setValue(actual);
            /*user.updatePassword(pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });*/
            Toast.makeText(this, "DATOS ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();

        }

    }

    public void limpiarCajas(){
        mail.setText("");
        tel.setText("");
        are.setText("");

    }

}
