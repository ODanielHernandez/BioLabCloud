package com.example.fragmento.biolabcloud;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.modelo.Organismo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;


public class formularios extends AppCompatActivity implements Serializable {



    Calendar calendario = Calendar.getInstance();
    EditText nomP, appP,correoP,passwordP,cantiO,fechaO;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios);

        nomP = findViewById(R.id.txt_nombrePersona);
        appP = findViewById(R.id.txt_appPersona);
        correoP = findViewById(R.id.txt_correoPersona);
        passwordP = findViewById(R.id.txt_passwordPersona);
        cantiO = findViewById(R.id.txt_Cantidad);
        fechaO = findViewById(R.id.txt_Fecha);

        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionToolbar);
        setTitle("Lista de Organismos");
        actionToolbar.setTitleTextColor(Color.WHITE);
        inicializarFirebase();


        Organismo objeto = (Organismo) getIntent().getExtras().getSerializable("objeto");
        int dedondevengo = getIntent().getExtras().getInt("dedondevengo");

        if(dedondevengo == 0){

            String nombreTab = "Familia " + objeto.getFamilia();
            setTitle(nombreTab);

            nomP.setText(objeto.getNombre());
            appP.setText(objeto.getDescripcion());
            correoP.setText(objeto.getFamilia());
            passwordP.setText(objeto.getLugar());
            cantiO.setText(objeto.getCantidad());
            fechaO.setText(objeto.getFecha());

            TextView tlt = findViewById(R.id.formulario_titulo);
            String cadena = "Modificar " + objeto.getNombre();

            tlt.setText(cadena);

        }

        fechaO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(formularios.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Organismo objeto = (Organismo) getIntent().getExtras().getSerializable("objeto");
        int dedondevengo = getIntent().getExtras().getInt("dedondevengo");

        String nombre = nomP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();
        String app = appP.getText().toString();
        String canti = cantiO.getText().toString();
        String fecha= fechaO.getText().toString();
        switch (item.getItemId()) {

            case R.id.icon_nuevo: {
                if (nombre.equals("") || correo.equals("") || password.equals("") || app.equals("") || canti.equals("")) {
                    validacion();

                } else {
                    if (dedondevengo == 0){
                        objeto.setNombre(nombre);
                        objeto.setDescripcion(app);
                        objeto.setFamilia(correo);
                        objeto.setLugar(password);
                        objeto.setCantidad(canti);
                        objeto.setFecha(fecha);
                        databaseReference.child("Organismo").child(objeto.getUid()).setValue(objeto);

                        Toast.makeText(this, "Modificado", Toast.LENGTH_LONG).show();


                        Intent i = new Intent(formularios.this, MainActivity.class);
                        startActivity(i);
                    }else{

                        Organismo p = new Organismo();
                        p.setUid(UUID.randomUUID().toString());
                        p.setNombre(nombre);
                        p.setDescripcion(app);
                        p.setFamilia(correo);
                        p.setLugar(password);
                        p.setCantidad(canti);
                        p.setFecha(fecha);
                        databaseReference.child("Organismo").child(p.getUid()).setValue(p);

                        Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();

                    }
                    limpiarCajas();

                }
                break;
            }
            case R.id.icon_cancelar: {
                Intent i = new Intent(formularios.this, MainActivity.class);
                startActivity(i);
            }
            break;

        }
        return true;
    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();


    }

    private void limpiarCajas() {
        nomP.setText("");
        correoP.setText("");
        passwordP.setText("");
        appP.setText("");
        cantiO.setText("");
        fechaO.setText("");

    }


    private void validacion() {
        String nombre = nomP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();
        String app = appP.getText().toString();
        String canti= cantiO.getText().toString();
        String fecha= fechaO.getText().toString();
        if (nombre.equals("")){
            nomP.setError("Required");
        }
        else if (app.equals("")){
            appP.setError("Required");
        }
        else if (correo.equals("")){
            correoP.setError("Required");
        }
        else if (password.equals("")){
            passwordP.setError("Required");
        }
        else if (canti.equals("")){
            passwordP.setError("Required");
        }
        else if (fecha.equals("")){
            passwordP.setError("Required");
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        fechaO.setText(sdf.format(calendario.getTime()));
    }





}
