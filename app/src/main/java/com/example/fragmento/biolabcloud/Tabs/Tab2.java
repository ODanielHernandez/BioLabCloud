package com.example.fragmento.biolabcloud.Tabs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.R;
import com.example.fragmento.biolabcloud.formularios;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;


public class Tab2 extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener,Serializable {
    private Tab2.OnFragmentInteractionListener mListener;


    private GoogleMap mMap;
    View mapView;
    Random random = new Random();


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, primary;

    ImageButton newMarker,btn_refresh;
    public static double Lat = 0;
    public static double Long = 0;
    final String[] pos = new String[1];
    Marker EventMarker;
    Bitmap bitmap;

    ImageView imagenOrganismo;
    public static Bitmap imagenEjemplar;

    public Tab2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        inicializarFirebase();

        newMarker = (ImageButton) v.findViewById(R.id.btn_draw_State);
        btn_refresh = v.findViewById(R.id.btn_refresh);

        newMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {RotateAnimation rotate = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1000);
                rotate.setRepeatCount(0);
                rotate.setInterpolator(new LinearInterpolator());
                btn_refresh.startAnimation(rotate);

                mMap.clear();
                ubicarReportes(mMap);
                Toast.makeText(getContext(), "Marcadores Actualizados", Toast.LENGTH_SHORT).show();

                btn_refresh.startAnimation(rotate);
            }
        });

        mapFragment.getMapAsync(this);
        return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();

        primary = databaseReference.child("Organismo");
    }

    private void ubicarReportes(final GoogleMap googleMap) {
        primary.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Map<String, String> value = (Map<String, String>)dataSnapshot.getValue();

                String Latitud="";
                String Longitud="";

                StringTokenizer st = new StringTokenizer(value.get("ubicacion"), ",;");
                while(st.hasMoreTokens()) {
                    Latitud = st.nextToken();
                     Longitud = st.nextToken();
                    EventMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(Latitud), Double.parseDouble(Longitud))));
                }


                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public View getInfoWindow(Marker marker) {
                        final Map<String, String> value = (Map<String, String>)marker.getTag();

                        final String titulo = value.get("nombre");
                        final String arg1 = value.get("familia");
                        final String arg2 = value.get("descripcion");
                        final String arg3 = value.get("cantidad");
                        final String arg4 = value.get("fecha");
                        final String arg5 = value.get("lugar");

                        View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                        final TextView nombre = v.findViewById(R.id.organismo);
                        final TextView Familia = v.findViewById(R.id.familia);
                        final TextView Descipcion = v.findViewById(R.id.descipcion);
                        final TextView Cantidad = v.findViewById(R.id.cantidad);
                        final TextView Lugar = v.findViewById(R.id.lugar);
                        final TextView Fecha = v.findViewById(R.id.fecha);
                        imagenOrganismo = v.findViewById(R.id.imagenOrganismo);

                        nombre.setText("Organismo: " + titulo);
                        Familia.setText("Familia: " + arg1);
                        Descipcion.setText("Descripción: " + arg2);
                        Lugar.setText("Lugar: " + arg5);
                        Cantidad.setText("Cantidad: " + arg3);
                        Fecha.setText("Fecha: " + arg4);

                        imagenOrganismo.setImageBitmap(DescargarImagen("Observacion",value.get("uid")));

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                Intent i = new Intent(getContext(), formularios.class);
                                i.putExtra("nombre",titulo);
                                i.putExtra("familia",arg1);
                                i.putExtra("descripcion",arg2);
                                i.putExtra("lugar",arg5);
                                i.putExtra("cantidad",arg3);
                                i.putExtra("fecha",arg4);
                                i.putExtra("uid",value.get("uid"));
                                i.putExtra("dedondevengo", 4);
                                startActivity(i);

                            }
                        });

                        return v;
                    }
                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
                EventMarker.setTag(value);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    public Bitmap DescargarImagen(String Referencia, final String Nombre){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://biolabcloud.appspot.com/" + Referencia).child(Nombre + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getContext(),"Error al obtener la imagen",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e ) {}
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapLongClick(mMap);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        moveMarker();
        ubicarReportes(mMap);

    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final String snippet = String.format(Locale.getDefault(),
                        "%1$.5f,%2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                dialogo1.setTitle("Agregar Reporte");
                dialogo1.setMessage("¿Esta seguro que deseas agregar un Reporte aquí?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Intent x = new Intent(getContext(), formularios.class);
                        x.putExtra("location", snippet);
                        x.putExtra("dedondevengo", 1);
                        Lat= latLng.latitude;
                        Long= latLng.longitude;
                        startActivity(x);
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogo1.show();
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "Info window clicked",Toast.LENGTH_SHORT).show();
    }

    public void moveMarker(){
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_options, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getLocation(){
        final double[] lat = new double[1];
        final double[] lang = new double[1];
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                lat[0] =location.getLatitude();
                lang[0] = location.getLongitude();

                pos[0] = String.valueOf(lat[0]) + ","+ String.valueOf(lang[0]);

                Lat= lat[0];
                Long= lang[0];
            }
        });
        Intent i = new Intent(getContext(), formularios.class);
        i.putExtra("location", pos[0]);
        i.putExtra("dedondevengo", 1);
        startActivity(i);
        return null;
    }

}
