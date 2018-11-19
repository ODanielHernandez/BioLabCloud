package com.example.fragmento.biolabcloud.Tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.FirebaseReference;
import com.example.fragmento.biolabcloud.MainActivity;
import com.example.fragmento.biolabcloud.R;
import com.example.fragmento.biolabcloud.formularios;
import com.example.fragmento.biolabcloud.modelo.Organismo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;


public class Tab2 extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener,Serializable {
    private Tab2.OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    View mapView;
    Random random= new Random();


    ArrayList<Organismo> listPerson = new ArrayList<Organismo>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,primary;

    ImageButton newMarker;
    int z=0;
    public static double Lat= 0;
    public static double Long= 0;
    final String[] pos = new String[1];


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
        listarDatos();

        newMarker = (ImageButton) v.findViewById(R.id.btn_draw_State);
        newMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        mapFragment.getMapAsync(this);
        return v;
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

    private void listarDatos() {
        primary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Organismo p = objSnaptshot.getValue(Organismo.class);
                    listPerson.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void ubicarReportes(final GoogleMap googleMap) {
        primary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    Organismo p = objSnaptshot.getValue(Organismo.class);
                    // Creating a marker

                    StringTokenizer st = new StringTokenizer(p.getUbicacion(), ",;");
                    while(st.hasMoreTokens()) {
                        String Latitud = st.nextToken();
                        String Longitud = st.nextToken();
                        LatLng reporte = new LatLng(Double.parseDouble(Latitud), Double.parseDouble(Longitud));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(reporte);
                        markerOptions.title(p.getNombre());
                        googleMap.addMarker(markerOptions);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                Intent i = new Intent(getContext(), formularios.class);
                i.putExtra("location", pos[0]);
                i.putExtra("dedondevengo", 1);
                startActivity(i);
            }
        });
        return null;
    }

}
