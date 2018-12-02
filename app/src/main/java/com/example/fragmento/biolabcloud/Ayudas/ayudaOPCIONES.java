package com.example.fragmento.biolabcloud.Ayudas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fragmento.biolabcloud.R;

public class ayudaOPCIONES extends AppCompatActivity {

    LinearLayout L1, L2, L3, L4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_opciones);

        L1 = findViewById(R.id.biolabtab);
        L2 = findViewById(R.id.chatAsistant);
        L3 = findViewById(R.id.laboratory);
        L4 = findViewById(R.id.mapIcon);


        L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ayudaOPCIONES.this, ayudaBIOLAB.class);
                startActivity(i);
            }
        });

        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ayudaOPCIONES.this, ayudaCHAT.class);
                startActivity(i);
            }
        });

        L3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ayudaOPCIONES.this, ayudaESTADO.class);
                startActivity(i);
            }
        });

        L4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ayudaOPCIONES.this, ayudaMAPA.class);
                startActivity(i);
            }
        });


    }
}
