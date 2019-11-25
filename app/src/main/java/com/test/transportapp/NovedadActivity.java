package com.test.transportapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.test.transportapp.notificaciones.APIService;
import com.test.transportapp.notificaciones.Data;
import com.test.transportapp.notificaciones.Response;
import com.test.transportapp.notificaciones.Sender;
import com.test.transportapp.notificaciones.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class NovedadActivity extends AppCompatActivity {

    private EditText carrActual;
    APIService apiService;
    private EditText carReemplazo;
    private EditText novedad;
    private Button registro;
    private String actual;
    private String reemplazo, administrador;
    private String noveda, idKey=null;
    private Boolean notify=false;
    private List<String> roles = new ArrayList<>();
    private List<String> llaves= new ArrayList<>();
    private List<String> area= new ArrayList<>();
    private List<String> ids= new ArrayList<>();
    private List<vehiculoDatos> datoVehiculo = new ArrayList<vehiculoDatos>();

    FirebaseAuth autenticacion;
    DatabaseReference bdApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novedad);
        autenticacion= FirebaseAuth.getInstance();
        bdApp= FirebaseDatabase.getInstance().getReference();
        idKey= autenticacion.getCurrentUser().getUid();

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

    public void registrarNovedad() {
        int contador=datoVehiculo.size();

        boolean flag = false;
        String datoObtenido = "";
        listarDatos();
        for (int incremento=0;incremento<contador;incremento++){
             datoObtenido=datoVehiculo.get(incremento).getPlaca();

            if (datoObtenido.equalsIgnoreCase(actual)) {
                flag = true;
            }
        }

        if (flag == true) {
            final String mensaje="Novedad: "+"Vehiculo averidado: "+actual+"\n vehiculo de reemplazo: "+reemplazo+
                    "\n motivo novedad: "+noveda;
            notify = true;
            Map<String, Object> datos = new HashMap<>();
            datos.put("vehiculo_averiado", actual);
            datos.put("vehiculo_reemplazo", reemplazo);
            datos.put("motivo", noveda);


            String id = bdApp.push().getKey();
            bdApp.child("Usuarios").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot datos : dataSnapshot.getChildren()) {
                        datosObtenidosLogin info = datos.getValue(datosObtenidosLogin.class);
                        roles.add(info.getRol());
                        llaves.add(datos.getKey());
                        area.add(info.getArea());
                        ids.add(info.getNombre());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            bdApp.child("Novedades").child(id).setValue(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task1) {
                    if (task1.isSuccessful()) {
                        Toast.makeText(NovedadActivity.this, "Novedad registrada", Toast.LENGTH_LONG).show();

                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Usuarios").child(idKey);
                        database.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                infoMensaje user = dataSnapshot.getValue(infoMensaje.class);


                                for (int i = 0; i < roles.size(); i++) {
                                    if (roles.get(i).equalsIgnoreCase("Admin") && area.get(i).equalsIgnoreCase(user.getArea())) {
                                        administrador = llaves.get(i);
                                    } else if (notify) {
                                        sendNotification(administrador, user.getNombre(), mensaje);
                                        sendNotification(user.getUid(), user.getNombre(), mensaje);

                                    }
                                    notify = false;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        Toast.makeText(NovedadActivity.this, "LA PLACA INGRESADA NO CORRESPONDE A UN VEHICULO REGISTRADO", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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

    public void listarDatos(){
        final String usuar=autenticacion.getCurrentUser().getUid();

        bdApp.child("Vehiculos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datoVehiculo.clear();
                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                    vehiculoDatos info =datos.getValue(vehiculoDatos.class);
                    datoVehiculo.add(info);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String mUID, final String nombre, final String mensaje) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(autenticacion.getUid());//VA EL ID DEL DESTINATARIO
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(mUID, nombre+":"+mensaje, "Nuevo Mensaje", mUID,R.drawable.ic_sms_black_24dp);
                    Sender sender = new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
