package com.test.transportapp;

import android.app.TimePickerDialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import android.icu.util.Calendar;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AlertDialog;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class manejoCalendario extends LinearLayout {

    ImageButton atras, adelante;
    TextView fecha;
    GridView vista_columna;
    private static final int DIAS_MAXIMO_CALENDARIO = 42;
    Calendar calendario= Calendar.getInstance(Locale.ENGLISH);
    Context contexto;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat formatoYear = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    AlertDialog alerta;
    List<Date> fechas=new ArrayList<>();
    List<Events> eventos = new ArrayList<>();
    adaptadorCeldas adaptadorCeldas;

    public manejoCalendario(Context context) {
        super(context);
    }

    public manejoCalendario(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.contexto = context;
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
                ImageButton clock = agregarVista.findViewById(R.id.reloj);
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
                                String eventoTiempo = horaFormato.format(ca.getTime());
                                muestraTiempo.setText(eventoTiempo);

                            }
                        },horas,minutos, false);
                        tiempo.show();
                    }
                });
                final String fechaStrin = formatoFecha.format(fechas.get(position));
                final String mesStrin = formatoMes.format(fechas.get(position));
                final String yearStrin = formatoYear.format(fechas.get(position));

                agregar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guardarSolicitud();
                        leerFecha();
                        alerta.dismiss();
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

        while(fechas.size()<DIAS_MAXIMO_CALENDARIO){

            fechas.add(meses.getTime());
            meses.add(Calendar.DAY_OF_MONTH,1);

        }

        adaptadorCeldas = new adaptadorCeldas(contexto,fechas,calendario,eventos);
        vista_columna.setAdapter(adaptadorCeldas);
    }

    private void coleccionEventosMes(){
        //leer BD mirar como hacerlo con mes
        String origenCalendario;
        String destinoCalendario;
        String horaCalendario;
        String fechaCalendario;
        String mesCalendario;
        String yearCalendrio;

        //Events evento = new Events(origenCalendario);
    }

    private void guardarSolicitud(){



    }
}
