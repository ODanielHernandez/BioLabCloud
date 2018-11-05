package com.example.fragmento.biolabcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.modelo.Adaptador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Tab1 extends Fragment {
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    SubmitButton btnData, logOut;
    Switch luz, extractor;
    DatabaseReference root, primary;
    Integer arreglo[] = new Integer[4];
    Integer Humidity,Temp,Bmp,humo;
    Integer global; // variable global para hacer giribillas
    ListView lista;
    SwipeRefreshLayout nswipeRefreshLayout;
    String valorHumo;

    int[] datosImg = {R.drawable.gauge,R.drawable.humidity,R.drawable.thermometerc,R.drawable.firealarm};

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_tab1, container, false);

        FirebaseUser userid = FirebaseAuth.getInstance().getCurrentUser() ;

        logOut = (SubmitButton) v.findViewById(R.id.logOut);
        btnData =  (SubmitButton) v.findViewById(R.id.recibirData);
        lista = (ListView) v.findViewById(R.id.lista);
        luz = (Switch) v.findViewById(R.id.luz);
        extractor = (Switch) v.findViewById(R.id.extractor);
        nswipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);


        //database reference pointing to root of database
        root = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        //primary = root.child("Users").child(userid.getUid()).child("iHumid");
        primary = root.child("Data");

        setupFirebaseListener();


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Switch StateSwitchChanger(final Switch value, final String child){
        value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(value.isChecked()){
                    root.child(child).setValue(0);
                }else{
                    root.child(child).setValue(1);
                }
            }
        });
        return null;
    }

    private Integer getFirebaseDataForAct(final String child, final Switch actuador){
        root.child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int valor = dataSnapshot.getValue(Integer.class);
                if(valor==1){
                    actuador.setChecked(false);
                }else{
                    actuador.setChecked(true);
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

    @Override
    public void onResume() {
        super.onResume();
        recuperarDatosFirebase();
        getFirebaseDataForAct("Act1",luz);
        getFirebaseDataForAct("Act2",extractor);
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

    private void setupFirebaseListener(){
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                }else{
                    Toast.makeText(getActivity(), "Sesión Cerrada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), InicioSesion.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuthListener);
        recuperarDatosFirebase();
        getFirebaseDataForAct("Act1",luz);
        getFirebaseDataForAct("Act2",extractor);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(firebaseAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(firebaseAuthListener);
        }
    }

}
