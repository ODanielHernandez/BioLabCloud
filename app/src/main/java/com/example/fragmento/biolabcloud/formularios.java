package com.example.fragmento.biolabcloud;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.Tabs.Tab2;
import com.example.fragmento.biolabcloud.modelo.Organismo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;


public class formularios extends AppCompatActivity implements Serializable {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Calendar calendario = Calendar.getInstance();
    EditText nomP, appP,correoP,passwordP,cantiO,fechaO;
    TextView txtUbi;
    ImageView cameraButton;
    LinearLayout btnUbicacion;
    static Uri uriFoto;
    Bitmap bmp;
    int z=0;
    private StorageReference mStorageRef;
    public static Bitmap imagenEjemplar;

    public static String pointUbication;
    static boolean imagenTomada;

    Organismo objeto;
    int dedondevengo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios);

        inicializarFirebase();

        objeto = (Organismo) getIntent().getExtras().getSerializable("objeto");
        dedondevengo = getIntent().getExtras().getInt("dedondevengo");
        imagenTomada = false;

        nomP = findViewById(R.id.txt_nombrePersona);
        appP = findViewById(R.id.txt_appPersona);
        correoP = findViewById(R.id.txt_correoPersona);
        passwordP = findViewById(R.id.txt_passwordPersona);
        cantiO = findViewById(R.id.txt_Cantidad);
        fechaO = findViewById(R.id.txt_Fecha);
        cameraButton = findViewById(R.id.cameraButton);
        txtUbi = findViewById(R.id.textviewUbicacion);
        btnUbicacion = findViewById(R.id.btnUbicacion);

        android.support.v7.widget.Toolbar actionToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionToolbar);
        setTitle("Formulario");
        actionToolbar.setTitleTextColor(Color.WHITE);

        if(dedondevengo== 1){

            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tomarfoto();
                }
            });

        }else if(dedondevengo == 2){
            String nombreTab = "Familia " + objeto.getFamilia();
            setTitle(nombreTab);
            txtUbi.setText("Modificar Ubicación");

            nomP.setText(objeto.getNombre());
            appP.setText(objeto.getDescripcion());
            correoP.setText(objeto.getFamilia());
            passwordP.setText(objeto.getLugar());
            cantiO.setText(objeto.getCantidad());
            fechaO.setText(objeto.getFecha());
            pointUbication = objeto.getUbicacion();

            imagenTomada = true;

            DescargarImagen("Observacion",objeto.getUid());

            TextView tlt = findViewById(R.id.formulario_titulo);
            String cadena = "Modificar " + objeto.getNombre();

            tlt.setText(cadena);

            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tomarfoto();
                }
            });

        }else if(dedondevengo == 3){

            String nombreTab = "Familia " + objeto.getFamilia();
            setTitle(nombreTab);
            txtUbi.setText("Ver Ubicación");

            nomP.setEnabled(false);
            appP.setEnabled(false);
            correoP.setEnabled(false);
            passwordP.setEnabled(false);
            cantiO.setEnabled(false);
            fechaO.setEnabled(false);

            nomP.setFocusable(false);
            appP.setFocusable(false);
            correoP.setFocusable(false);
            passwordP.setFocusable(false);
            cantiO.setFocusable(false);
            fechaO.setFocusable(false);


            nomP.setText(objeto.getNombre());
            appP.setText(objeto.getDescripcion());
            correoP.setText(objeto.getFamilia());
            passwordP.setText(objeto.getLugar());
            cantiO.setText(objeto.getCantidad());
            fechaO.setText(objeto.getFecha());
            DescargarImagen("Observacion",objeto.getUid());

            imagenTomada = true;
            pointUbication = objeto.getUbicacion();

            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(formularios.this, imagenejemplar.class);
                    startActivity(i);
                }
            });


            TextView tlt = findViewById(R.id.formulario_titulo);
            String cadena = "Ejemplar " + objeto.getNombre();

            tlt.setText(cadena);

        }else if(dedondevengo == 4) {

            String tagNombre =  getIntent().getExtras().getString("nombre");
            String tagFamilia =  getIntent().getExtras().getString("familia");
            String tagDescripcion =  getIntent().getExtras().getString("descripcion");
            String tagLugar =  getIntent().getExtras().getString("lugar");
            String tagCantidad =  getIntent().getExtras().getString("cantidad");
            String tagFecha =  getIntent().getExtras().getString("fecha");
            String tagUid =  getIntent().getExtras().getString("uid");

            String nombreTab = "Familia " + tagFamilia;
            setTitle(nombreTab);

            btnUbicacion.setVisibility(View.INVISIBLE);

            nomP.setEnabled(false);
            appP.setEnabled(false);
            correoP.setEnabled(false);
            passwordP.setEnabled(false);
            cantiO.setEnabled(false);
            fechaO.setEnabled(false);

            nomP.setFocusable(false);
            appP.setFocusable(false);
            correoP.setFocusable(false);
            passwordP.setFocusable(false);
            cantiO.setFocusable(false);
            fechaO.setFocusable(false);


            nomP.setText(tagNombre);
            appP.setText(tagDescripcion);
            correoP.setText(tagFamilia);
            passwordP.setText(tagLugar);
            cantiO.setText(tagCantidad);
            fechaO.setText(tagFecha);
            DescargarImagen("Observacion", tagUid);

            imagenTomada = true;

            cameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(formularios.this, imagenejemplar.class);
                    startActivity(i);
                }
            });


            TextView tlt = findViewById(R.id.formulario_titulo);
            String cadena = "Ejemplar " + tagNombre;

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

        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(formularios.this, MapsActivity.class);
                i.putExtra("objeto", objeto);
                i.putExtra("dedondevengo",dedondevengo);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo,menu);
        dedondevengo = getIntent().getExtras().getInt("dedondevengo");

        if(dedondevengo == 3 || dedondevengo == 4){
            MenuItem item = menu.findItem(R.id.icon_nuevo);
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        objeto = (Organismo) getIntent().getExtras().getSerializable("objeto");
        dedondevengo = getIntent().getExtras().getInt("dedondevengo");


        final String nombre = nomP.getText().toString();
        final String correo = correoP.getText().toString();
        final String password = passwordP.getText().toString();
        final String app = appP.getText().toString();
        final String canti = cantiO.getText().toString();
        final String fecha= fechaO.getText().toString();
        cameraButton = findViewById(R.id.cameraButton);

        switch (item.getItemId()) {

            case R.id.icon_nuevo: {
                if (nombre.equals("") || correo.equals("") || password.equals("") || app.equals("") || canti.equals("") || fecha.equals("") || !imagenTomada || pointUbication == null){
                    validacion();

                } else {
                    if (dedondevengo == 2){
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                        dialogo1.setTitle("Alerta");
                        dialogo1.setMessage("¿Es " + pointUbication + " la ubicación correcta?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                                objeto.setNombre(nombre);
                                objeto.setDescripcion(app);
                                objeto.setFamilia(correo);
                                objeto.setLugar(password);
                                objeto.setCantidad(canti);
                                objeto.setFecha(fecha);
                                objeto.setUbicacion(pointUbication);
                                if (uriFoto==null){
                                }else{
                                    SubirFoto(objeto.getUid(), "Observacion", uriFoto);
                                }
                                databaseReference.child("Organismo").child(objeto.getUid()).setValue(objeto);

                                Toast.makeText(formularios.this, "Modificado", Toast.LENGTH_LONG).show();
                                Tab2.Lat=0;
                                Tab2.Long=0;


                                Intent i = new Intent(formularios.this, MainActivity.class);
                                startActivity(i);
                            }
                        });

                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(formularios.this, "Cancelado", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogo1.show();
                    }else{
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                            dialogo1.setTitle("Alerta");
                            dialogo1.setMessage("¿Es " + pointUbication + " la ubicación correcta?");
                            dialogo1.setCancelable(false);
                            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    Organismo p = new Organismo();
                                    p.setUid(UUID.randomUUID().toString());
                                    p.setNombre(nombre);
                                    p.setDescripcion(app);
                                    p.setFamilia(correo);
                                    p.setLugar(password);
                                    p.setCantidad(canti);
                                    p.setFecha(fecha);
                                    p.setUbicacion(pointUbication);
                                    SubirFoto(p.getUid(), "Observacion", uriFoto);
                                    databaseReference.child("Organismo").child(p.getUid()).setValue(p);
                                    Tab2.Lat=0;
                                    Tab2.Long=0;

                                    Toast.makeText(formularios.this, "Agregado", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(formularios.this, MainActivity.class);
                                    startActivity(i);
                                }
                            });

                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    Toast.makeText(formularios.this, "Cancelado", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialogo1.show();
                    }
                }
                break;
            }
            case R.id.icon_cancelar: {
                Tab2.Lat=0;
                Tab2.Long=0;
                finish();
            }
            break;

        }
        return true;
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
            cantiO.setError("Required");
        }
        else if (fecha.equals("")){
            fechaO.setError("Required");
        }
        else if(!imagenTomada){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Alerta");
            dialogo1.setMessage("Asegurese de capturar una imagen del organismo");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }
        else if (pointUbication == null){
            AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);
            dialogo2.setTitle("Alerta");
            dialogo2.setMessage("Asegurese de colocar la posición en el mapa");
            dialogo2.setCancelable(false);
            dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo2.show();
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

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void DescargarImagen(String Referencia, String Nombre){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://biolabcloud.appspot.com/" + Referencia).child(Nombre + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    cameraButton.setImageBitmap(bitmap);
                    imagenEjemplar = bitmap;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(),"Error al obtener la imagen",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e ) {}
    }
    public void Tomarfoto(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagenTomada = true;
        startActivityForResult(i,0);
    }


    public void SubirFoto(String Nombre, String Referencia, Uri uri){
        mStorageRef = FirebaseStorage.getInstance().getReference(Referencia);
        final StorageReference storageReference = mStorageRef.child( Nombre + ".jpg");
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(formularios.this, "Dato agregado correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");
            uriFoto= getImageUri(formularios.this, bmp);
            cameraButton.setImageURI(getImageUri(formularios.this, bmp));
        }
    }


}
