package com.test.transportapp;

import android.app.TimePickerDialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import android.icu.util.Calendar;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.test.transportapp.notificaciones.APIService;
import com.test.transportapp.notificaciones.Client;
import com.test.transportapp.notificaciones.Data;
import com.test.transportapp.notificaciones.Response;
import com.test.transportapp.notificaciones.Sender;
import com.test.transportapp.notificaciones.Token;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;


public class manejoCalendario extends LinearLayout {

    APIService apiService;
    String mUID;
    String conduc;
    boolean notify = false;
    ImageButton atras, adelante;
    TextView fecha;
    GridView vista_columna;
    String origenStrin;
    String destinoStrin;
    String eventoTiempo;
    String pasajeroBD;
    String id;
    String token1,agendador;


    private static final int DIAS_MAXIMO_CALENDARIO = 42;
    Calendar calendario = Calendar.getInstance(Locale.ENGLISH);
    Context contexto;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat formatoYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat formatodia = new SimpleDateFormat("dd", Locale.ENGLISH);
    SimpleDateFormat eventoFormato = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    AlertDialog alerta;
    List<Date> fechas = new ArrayList<>();
    List<Events> eventos = new ArrayList<>();
    adaptadorCeldas adaptadorCeldas;
    datosObtenidosLogin informacion;
    FirebaseAuth autenticacion;
    DatabaseReference bdApp;
    ArrayAdapter<Events> adaptadorEvento;
    private List<String> llave = new ArrayList<>();
    private List<String> roles = new ArrayList<>();
    private List<String> llaves= new ArrayList<>();
    private List<String> area= new ArrayList<>();
    private List<String> ids= new ArrayList<>();

    Events info;
    String idKey = null;

    public manejoCalendario(Context context) {
        super(context);

    }

    public manejoCalendario(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contexto = context;

        info= new Events();
        autenticacion= FirebaseAuth.getInstance();
        bdApp= FirebaseDatabase.getInstance().getReference();
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
        mUID= autenticacion.getCurrentUser().getUid();
        iniciarLayout();
        leerFecha();
        atras.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario.add(Calendar.MONTH, -1);
                leerFecha();
            }
        });

        adelante.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario.add(Calendar.MONTH, 1);
                leerFecha();
            }
        });

        vista_columna.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder= new AlertDialog.Builder(contexto);
                builder.setCancelable(true);
                final View agregarVista = LayoutInflater.from(parent.getContext()).inflate(R.layout.agregar_evento, null);
                final EditText orig = agregarVista.findViewById(R.id.origen);
                final EditText dest = agregarVista.findViewById(R.id.destino);
                final EditText pasajeros = agregarVista.findViewById(R.id.numPasajero);
                final TextView muestraTiempo= agregarVista.findViewById(R.id.mostrarTiempo);
                final ImageButton clock = agregarVista.findViewById(R.id.reloj);
                Button agregar = agregarVista.findViewById(R.id.botonsolicitud);
                clock.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int horas = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutos = calendar.get(Calendar.MINUTE);
                        TimePickerDialog tiempo = new TimePickerDialog(agregarVista.getContext(), R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar ca = Calendar.getInstance();
                                ca.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                ca.set(Calendar.MINUTE,minute);
                                ca.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat horaFormato = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                eventoTiempo = horaFormato.format(ca.getTime());
                                muestraTiempo.setText(eventoTiempo);

                            }
                        },horas,minutos, false);
                        tiempo.show();
                    }
                });
                final String fechaStrin = eventoFormato.format(fechas.get(position));
                final String mesStrin = formatoMes.format(fechas.get(position));
                final String yearStrin = formatoYear.format(fechas.get(position));
                final String diaStrin= formatodia.format(fechas.get(position));

                agregar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        origenStrin = orig.getText().toString().trim();
                        destinoStrin = dest.getText().toString().trim();
                        pasajeroBD = pasajeros.getText().toString().trim();

                        if(!origenStrin.isEmpty() && !destinoStrin.isEmpty() && !pasajeroBD.isEmpty() && !eventoTiempo.isEmpty()) {
                            int pasagger=Integer.parseInt(pasajeroBD);
                            if(pasagger>0 && pasagger<6) {
                                notify = true;
                                guardarSolicitud(diaStrin, mesStrin, yearStrin, origenStrin, destinoStrin, pasajeroBD, eventoTiempo, fechaStrin);
                                leerFecha();
                                alerta.dismiss();
                            }
                            else{
                                Toast.makeText(getContext(), "COMO MÁXIMO PUEDE TENER 5 PASAJEROS", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "DEBE LLENAR TODOS LOS DATOS DEL RECORRIDO", Toast.LENGTH_SHORT).show();
                        }


                        }
                });
                builder.setView(agregarVista);
                alerta = builder.create();
                alerta.show();
            }
        });

    }

    public manejoCalendario(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void iniciarLayout(){
        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendario,this);
        atras= findViewById(R.id.atras);
        adelante=findViewById(R.id.adelante);
        fecha=findViewById(R.id.fecha);
        vista_columna= findViewById(R.id.vista_columna);
    }
    private void leerFecha(){
        String fechaMetodo= formatoFecha.format(calendario.getTime());
        fecha.setText(fechaMetodo);
        fechas.clear();
        Calendar meses = (Calendar) calendario.clone();
        meses.set(Calendar.DAY_OF_MONTH,1);
        int primerDiaMes = meses.get(Calendar.DAY_OF_WEEK)-1;
        meses.add(Calendar.DAY_OF_MONTH, -primerDiaMes);
        coleccionEventosMes();

        while(fechas.size()<DIAS_MAXIMO_CALENDARIO){

            fechas.add(meses.getTime());
            meses.add(Calendar.DAY_OF_MONTH,1);

        }

        adaptadorCeldas = new adaptadorCeldas(contexto,fechas,calendario,eventos);
        vista_columna.setAdapter(adaptadorCeldas);
    }

    private void coleccionEventosMes(){

        bdApp.child("Recorridos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventos.clear();
                for (DataSnapshot datos:dataSnapshot.getChildren()) {
                    info =datos.getValue(Events.class);
                    eventos.add(info);
                    adaptadorEvento= new ArrayAdapter<Events>(contexto,android.R.layout.simple_list_item_1, eventos);
                    //eventosLista.setAdapter(adaptadorEvento);
                    llave.add(datos.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void guardarSolicitud(String dia, String mes, String year, String origin, final String destination, String pasajeroBD, String hora, String fecha ){


        idKey= autenticacion.getCurrentUser().getUid();

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

        for(int i=0;i<llaves.size();i++){
            if(idKey.equalsIgnoreCase(llaves.get(i))){
//                posis=i;
            }
        }

//        agendador=ids.get(posis);
        final Map<String, Object> infoSolicitud = new HashMap<>();
        infoSolicitud.put("origen",origin);
        infoSolicitud.put("destino",destination);
        infoSolicitud.put("num_pasajeros", pasajeroBD);
        infoSolicitud.put("hora",hora);
        infoSolicitud.put("dia",dia);
        infoSolicitud.put("mes",mes);
        infoSolicitud.put("año",year);

        infoSolicitud.put("fecha",fecha);
  //      infoSolicitud.put("agendado",agendador);
        final String mensaje="Recorrido agendado:\n Destino:"+destination+"\n Origen:"+origin+"\n fecha:"+fecha;


        //bdApp.child("Recorridos").child(id).setValue(infoSolicitud).addOnCompleteListener(new OnCompleteListener<Void>() {
          //  @Override
            //public void onComplete(@NonNull Task<Void> task1) {

                //if(task1.isSuccessful()){
                  //  Toast.makeText(contexto,"Recorrido asignado", Toast.LENGTH_LONG).show();

                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Usuarios").child(idKey);
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final infoMensaje user = dataSnapshot.getValue(infoMensaje.class);

                            if(user.getRol().equalsIgnoreCase("Conductor") && user.getArea().equalsIgnoreCase(user.getArea())){

                                }

                            final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Usuarios");
                            database.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    for(DataSnapshot datos:dataSnapshot1.getChildren()) {
                                        actualizarDatos info = datos.getValue(actualizarDatos.class);
                                        if(info.getArea().equalsIgnoreCase(user.getArea())){
                                            String estado="Ocupado";
                                            infoSolicitud.put("estado",estado);
                                            String  id =bdApp.push().getKey();


                                            bdApp.child("Recorridos").child(id).setValue(infoSolicitud).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(notify){
                                                        sendNotification(mUID,user.getNombre(),mensaje);
                                                        sendNotification(conduc,user.getNombre(),mensaje);
                                                    }
                                                    notify=false;
                                                }
                                            });

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                //}
                /*else{
                    Toast.makeText(contexto,"Hubo un error, intente nuevamente", Toast.LENGTH_LONG).show();
                }*/
          //  }
        //});
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
