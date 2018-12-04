package com.example.fragmento.biolabcloud.Tabs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.Ayudas.ayudaOPCIONES;
import com.example.fragmento.biolabcloud.InicioSesion;
import com.example.fragmento.biolabcloud.R;
import com.example.fragmento.biolabcloud.contacto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;


public class Tab5 extends Fragment {
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private OnFragmentInteractionListener mListener;
    SubmitButton  logOut,Contacto,Ayuda;

    public Tab5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_tab5, container, false);

        logOut = v.findViewById(R.id.logOut);
        Contacto = v.findViewById(R.id.Contacto);
        Ayuda = v.findViewById(R.id.Ayuda);
        setupFirebaseListener();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        Ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ayudaOPCIONES.class);
                startActivity(i);
            }
        });

        Contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), contacto.class);
                startActivity(i);
            }
        });

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuthListener);
        return v;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
