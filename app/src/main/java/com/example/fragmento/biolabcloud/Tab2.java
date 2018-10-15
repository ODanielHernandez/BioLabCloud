package com.example.fragmento.biolabcloud;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.modelo.Adaptador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class Tab2 extends Fragment {
    private OnFragmentInteractionListener mListener;
    DatabaseReference Freference, Freference2, Freference3;
    FirebaseDatabase Fdatabase;


    GraphView hum,temp,pres;
    Double Humidity,Temp,Bmp,Humo;
    int cont=0;
    LineGraphSeries series,series2,series3;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd-hh");


    public Tab2() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);


        FirebaseUser userid = FirebaseAuth.getInstance().getCurrentUser() ;

        Fdatabase = FirebaseDatabase.getInstance();
        Freference = Fdatabase.getReference("Data").child("time");


        hum = (GraphView) v.findViewById(R.id.hum);
        temp = (GraphView) v.findViewById(R.id.temp);

        series = new LineGraphSeries();
        series2 = new LineGraphSeries();
        series3 = new LineGraphSeries();

        GridLabelRenderer gridLabelRenderer = hum.getGridLabelRenderer();
        GridLabelRenderer gridLabelRenderer2 = temp.getGridLabelRenderer();

        hum.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    double minX = hum.getViewport().getMinX(false);
                    double maxX = hum.getViewport().getMaxX(false);
                    double diff = maxX - minX;

                    Date date = new Date((long) value*1000);
                    String label =  null;

                    if(diff >= 86400){
                        label = DateFormat.format("MM/dd-hh", date).toString();
                    }else{
                        label = DateFormat.format("hh:mm", date).toString();
                    }

                    return label;
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        hum.getGridLabelRenderer().setGridColor(Color.WHITE);
        hum.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        hum.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        hum.getGridLabelRenderer().setNumHorizontalLabels(3);

        hum.getViewport().setScalable(true);
        hum.getViewport().setScrollable(true);
        hum.getViewport().setXAxisBoundsManual(true);
        hum.getViewport().setMinX(hum.getViewport().getMinX(false));
        hum.getViewport().setMaxX(hum.getViewport().getMinX(false) + 24*60*60*1000);


        series.setTitle("Humedad");
        series.setColor(Color.rgb(117,224,156));
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(20, 117, 224, 156));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10f);


        temp.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    double minX = temp.getViewport().getMinX(false);
                    double maxX = temp.getViewport().getMaxX(false);
                    double diff = maxX - minX;

                    Date date = new Date((long) value*1000);
                    String label =  null;

                    if(diff >= 86400){
                        label = DateFormat.format("MM/dd-hh", date).toString();
                    }else{
                        label = DateFormat.format("hh:mm", date).toString();
                    }

                    return label;
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        temp.getGridLabelRenderer().setGridColor(Color.WHITE);
        temp.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        temp.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        temp.getGridLabelRenderer().setNumHorizontalLabels(3);

        temp.getViewport().setScalable(true);
        temp.getViewport().setScrollable(true);
        temp.getViewport().setXAxisBoundsManual(true);
        temp.getViewport().setMinX(temp.getViewport().getMinX(false));
        temp.getViewport().setMaxX(temp.getViewport().getMinX(false) + 24*60*60*1000);


        series2.setTitle("Temp C°");
        series2.setColor(Color.rgb(117,224,156));
        series2.setDrawBackground(true);
        series2.setBackgroundColor(Color.argb(20, 117, 224, 156));
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10f);


        hum.getLegendRenderer().setVisible(true);
        hum.getLegendRenderer().setTextSize(25);
        hum.getLegendRenderer().setBackgroundColor(Color.argb(150, 255, 255, 255));
        hum.getLegendRenderer().setTextColor(Color.rgb(0,0,0));
        hum.getLegendRenderer().setFixedPosition(0, 0);



        temp.getLegendRenderer().setVisible(true);
        temp.getLegendRenderer().setTextSize(25);
        temp.getLegendRenderer().setBackgroundColor(Color.argb(150, 255, 255, 255));
        temp.getLegendRenderer().setTextColor(Color.rgb(0,0,0));
        temp.getLegendRenderer().setFixedPosition(0, 0);

        hum.addSeries(series);
        temp.addSeries(series2);



        recuperarDatosFirebase();
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        long x = new Date().getTime();

        Freference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    guardarDatosFirebaseH(pointValue.getHumidity(),pointValue.gettime());
                    guardarDatosFirebaseT(pointValue.getTemp(),pointValue.gettime());
                    guardarDatosFirebaseB(pointValue.getBmp(),pointValue.gettime());

                    if(cont == 9){
                        String[] Datos = getActivity().fileList();
                        File file=new File(getActivity().getFilesDir().getAbsolutePath()+"/"+ "Datos.txt");
                        if(file.exists())file.delete();
                    }else{
                        cont++;
                    }
                    //JALAR DATOS DE GRÁFICA y ALMACENAR EN TXT

                    /*PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    dp[index]= new DataPoint(pointValue.gettime(),pointValue.gethumid());
                    */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void guardarDatosFirebaseH(double valor, long tiempo){
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getActivity().openFileOutput(
                    "Datos.txt", Activity.MODE_PRIVATE));
            archivo.write( String.valueOf(valor)  + "/" + String.valueOf(tiempo));
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
    }
    public void guardarDatosFirebaseT(double valor, long tiempo){
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getActivity().openFileOutput(
                    "Datos2.txt", Activity.MODE_PRIVATE));
            archivo.write( String.valueOf(valor)  + "/" + String.valueOf(tiempo));
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
    }
    public void guardarDatosFirebaseB(double valor, long tiempo){
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getActivity().openFileOutput(
                    "Datos3.txt", Activity.MODE_PRIVATE));
            archivo.write( String.valueOf(valor)  + "/" + String.valueOf(tiempo) + "/n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
    }

    private boolean existe(String[] Datos, String archbusca) {
        for (int f = 0; f < Datos.length; f++)
            if (archbusca.equals(Datos[f]))
                return true;
        return false;
    }

    public void recuperarDatosFirebase(){
        String[] Datos = getActivity().fileList();

        if (existe(Datos, "Datos.txt"))
            try {
                InputStreamReader archivo = new InputStreamReader(
                        getActivity().openFileInput("Datos.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                DataPoint[] dp= new DataPoint[10];
                int index= 0;
                while (linea != null) {

                    StringTokenizer tokens = new StringTokenizer(todo, "/");
                    String Humedad = tokens.nextToken();
                    String tiempo = tokens.nextToken();

                        dp[index]= new DataPoint(Float.valueOf(tiempo),Long.valueOf(Humedad));

                        index++;
                    linea = br.readLine();
                }
                series.resetData(dp);
                br.close();
                archivo.close();



            } catch (IOException e) {
            }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
