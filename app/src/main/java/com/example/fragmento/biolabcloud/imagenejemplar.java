package com.example.fragmento.biolabcloud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class imagenejemplar extends AppCompatActivity {

    ImageView imagenseleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenejemplar);

        imagenseleccionada = findViewById(R.id.imagenEjemplar);
        imagenseleccionada.setImageBitmap(formularios.imagenEjemplar);

    }
}
