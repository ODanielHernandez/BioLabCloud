package com.example.fragmento.biolabcloud;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class contacto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        ImageButton facebook = (ImageButton) findViewById(R.id.facebook);
        ImageButton instagram = (ImageButton) findViewById(R.id.instagram);

        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.facebook.com/FragmentoEstudioWeb/"));
                startActivity(viewIntent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.instagram.com/fragmentoestudioweb/"));
                startActivity(viewIntent);
            }
        });

    }
}
