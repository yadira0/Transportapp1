package com.test.transportapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class adaptadorCeldas extends ArrayAdapter {
    List<Date> fechas;
    Calendar calendario;
    List<Events> eventos;
    LayoutInflater inflador;
    public adaptadorCeldas(@NonNull Context context, List <Date> fechas, Calendar calendario, List<Events> eventos) {
        super(context, R.layout.celdas);
        this.fechas=fechas;
        this.calendario=calendario;
        this.eventos=eventos;
        inflador = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date mesFecha = fechas.get(position);
        Calendar fechaCalendario = Calendar.getInstance();
        fechaCalendario.setTime(mesFecha);
        int diaNum = fechaCalendario.get(Calendar.DAY_OF_MONTH);
        int muestraMes= fechaCalendario.get(Calendar.MONTH)+1;
        int muestaYear = fechaCalendario.get(Calendar.YEAR);
        int manejadorMes = calendario.get(Calendar.MONTH)+1;
        int manejadorYear = calendario.get(Calendar.YEAR);

        View vista1 = convertView;
        if(vista1 == null){
            vista1 = inflador.inflate(R.layout.celdas, parent,false);
        }

        if(muestraMes== manejadorMes && muestaYear == manejadorYear){
            vista1.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
        }
        else{
            vista1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        }

        TextView numeroDia = vista1.findViewById(R.id.celda_columna);
        TextView numEvento = vista1.findViewById(R.id.eventos_celda);
        numeroDia.setText(String.valueOf(diaNum));
        Calendar eventosCalendario = Calendar.getInstance();
        ArrayList <String> array =new ArrayList<>();
        for(int cont=0; cont< eventos.size(); cont ++){
            eventosCalendario.setTime(convertirStringaFecha(eventos.get(cont).getFecha()));
            if(diaNum == eventosCalendario.get(Calendar.DAY_OF_MONTH) && muestraMes == eventosCalendario.get(Calendar.MONTH)
                    && muestaYear == eventosCalendario.get(Calendar.YEAR)){

                array.add(eventos.get(cont).getDestino());
                array.add(eventos.get(cont).getHora());
                numEvento.setText(array.size()+ "Viajes");
            }
        }

        return vista1;
    }

    private Date convertirStringaFecha(String eventoFecha){
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-ddd", Locale.ENGLISH);
        Date fechaMe = null;
        try{
            fechaMe = formato.parse(eventoFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaMe;
    }

    @Override
    public int getCount() {
        return fechas.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return fechas.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return fechas.get(position);
    }
}
