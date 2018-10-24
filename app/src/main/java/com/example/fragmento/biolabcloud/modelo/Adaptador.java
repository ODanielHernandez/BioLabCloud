package com.example.fragmento.biolabcloud.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmento.biolabcloud.R;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;



    Context contexto;
    String[][] datos;
    int[] datosImg;

    public Adaptador(Context contexto, String[][] datos, int[] imagenes){
        this.contexto = contexto;
        this.datos = datos;
        this.datosImg = imagenes;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View vista = inflater.inflate(R.layout.elemento_lista, null);
        TextView titulo = (TextView) vista.findViewById(R.id.titulo);
        TextView value = (TextView) vista.findViewById(R.id.value);
        ImageView imagen = (ImageView) vista.findViewById(R.id.iVimagen);
        titulo.setText(datos[i][0]);
        value.setText(datos[i][1]);
        imagen.setImageResource(datosImg[i]);
        return vista;
    }

    @Override
    public int getCount() {
        return datosImg.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
