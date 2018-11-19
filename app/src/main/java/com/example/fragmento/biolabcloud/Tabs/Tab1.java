package com.example.fragmento.biolabcloud.Tabs;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.R;
import com.example.fragmento.biolabcloud.modelo.Adaptador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Tab1 extends Fragment {

    SubmitButton btnData;
    SwitchCompat luz, extractor;
    DatabaseReference root, primary;
    Integer arreglo[] = new Integer[4];
    Integer Humidity,Temp,Bmp,humo, global;
    ListView lista;
    SwipeRefreshLayout nswipeRefreshLayout;
    TextView txtLuz, txtExtractor;

    int[] datosImg = {R.drawable.gauge,R.drawable.humidity,R.drawable.thermometerc,R.drawable.firealarm};


    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_tab1, container, false);

        btnData =  v.findViewById(R.id.recibirData);
        lista = v.findViewById(R.id.lista);
        luz = v.findViewById(R.id.luz);
        txtLuz = v.findViewById(R.id.txtLuz);
        txtExtractor = v.findViewById(R.id.txtExtractor);
        extractor = v.findViewById(R.id.extractor);
        nswipeRefreshLayout = v.findViewById(R.id.swiperefresh);


        root = FirebaseDatabase.getInstance().getReference();
        primary = root.child("Data");

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDatosFirebase();
                getFirebaseDataForAct("Act1",luz);
                getFirebaseDataForAct("Act2",extractor);
                guardarDatosFirebase();
            }
        });

        nswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actualizarDatosFirebase();
                guardarDatosFirebase();
                nswipeRefreshLayout.setRefreshing(false); //para parar la animacion de refrescado
            }
        });

        getFirebaseDataForAct("Act1",luz);
        getFirebaseDataForAct("Act2",extractor);

        StateSwitchChanger(luz,"Act1");
        StateSwitchChanger(extractor,"Act2");


        actualizarDatosFirebase();
        recuperarDatosFirebase();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        recuperarDatosFirebase();
        getFirebaseDataForAct("Act1",luz);
        getFirebaseDataForAct("Act2",extractor);
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDatosFirebase();
        getFirebaseDataForAct("Act1",luz);
        getFirebaseDataForAct("Act2",extractor);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Switch StateSwitchChanger(final SwitchCompat value, final String child){
        value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(value.isChecked()){
                    root.child(child).setValue(0);
                    if(value == luz){
                        txtLuz.setText("ON");
                    }else if(value == extractor){
                        txtExtractor.setText("ON");
                    }
                }else{
                    root.child(child).setValue(1);
                    if(value == luz){
                        txtLuz.setText("OFF");
                    }else if(value == extractor){
                        txtExtractor.setText("OFF");
                    }
                }
            }
        });
        return null;
    }

    private Integer getFirebaseDataForAct(final String child, final SwitchCompat actuador){
        root.child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int valor = dataSnapshot.getValue(Integer.class);
                if(valor==1){
                    actuador.setChecked(false);
                    if(actuador == luz){
                        txtLuz.setText("OFF");
                    }else if(actuador == extractor){
                        txtExtractor.setText("OFF");
                    }
                }else{
                    actuador.setChecked(true);
                    if(actuador == luz){
                        txtLuz.setText("ON");
                    }else if(actuador == extractor){
                        txtExtractor.setText("ON");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("error", "onCancelled: "+databaseError.getMessage());
            }
        });
        return null;
    }

        private Integer getFirebaseDataFromChild(final String child, final Integer pos) {
            primary.child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                global = dataSnapshot.getValue(Integer.class);
                //Toast.makeText(getActivity().getApplicationContext(), child+" : "+ global, Toast.LENGTH_SHORT).show();
                arreglo[pos] = global;
                 }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error", "onCancelled: "+databaseError.getMessage());
                global = 0;
            }
        });
        return null;
    }

    public void actualizarDatosFirebase(){
        Bmp= getFirebaseDataFromChild("Bmp", 0);
        Humidity = getFirebaseDataFromChild("Humidity", 1);
        Temp= getFirebaseDataFromChild("Temp", 2);
        humo= getFirebaseDataFromChild("humo", 3);

        Bmp = arreglo[0];
        Humidity = arreglo[1];
        Temp = arreglo[2];
        humo = arreglo[3];

        String [][] datos = {
                {"Presión Atmosferica", String.valueOf(Bmp).concat(" atm")},
                {"Humedad Relativa", String.valueOf(Humidity).concat("%")},
                {"Temperatura", String.valueOf(Temp).concat("°C")},
                {"Humo", String.valueOf(humo)}

        };
        lista.setAdapter(new Adaptador(getActivity().getApplicationContext(),datos,datosImg));

    }
    public String convercion(Integer valHum){
        String resultado="";

        if(valHum == 1){
             resultado = "Despejado";
        }else if (valHum == 0){
            resultado = "ALERTA";
        }

        return resultado;
    }

    public void guardarDatosFirebase(){
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getActivity().openFileOutput(
                    "valores.txt", Activity.MODE_PRIVATE));
            archivo.write( String.valueOf(Bmp)  + "/" + String.valueOf(Humidity) + "/" + String.valueOf(Temp) + "/" + String.valueOf(humo));
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
        }
        Toast t = Toast.makeText(getActivity(), "Datos almacenados",Toast.LENGTH_SHORT);
        t.show();
    }

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }

    public void recuperarDatosFirebase(){
        String[] archivos = getActivity().fileList();

        if (existe(archivos, "valores.txt"))
            try {
                InputStreamReader archivo = new InputStreamReader(
                        getActivity().openFileInput("valores.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                while (linea != null) {
                    todo = todo + linea + "\n";
                    linea = br.readLine();
                }
                br.close();
                archivo.close();

                StringTokenizer tokens = new StringTokenizer(todo, "/");
                String Bmp = tokens.nextToken();
                String Humedad = tokens.nextToken();
                String Temperatura = tokens.nextToken();
                String humo = tokens.nextToken();


                String [][] datos = {
                        {"Presión Atmosferica", String.valueOf(Bmp).concat(" atm")},
                        {"Humedad Relativa", String.valueOf(Humedad).concat("%")},
                        {"Temperatura", String.valueOf(Temperatura).concat("°C")},
                        {"Humo", humo}

                };
                lista.setAdapter(new Adaptador(getActivity().getApplicationContext(),datos,datosImg));
            } catch (IOException e) {
            }
    }



}
