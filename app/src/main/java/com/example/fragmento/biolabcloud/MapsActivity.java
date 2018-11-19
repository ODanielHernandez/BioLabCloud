package com.example.fragmento.biolabcloud;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fragmento.biolabcloud.Tabs.Tab2;
import com.example.fragmento.biolabcloud.modelo.Organismo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;
import java.util.StringTokenizer;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    View mapView;
    static String ubicacion;

    Organismo objeto;
    int dedondevengo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        objeto = (Organismo) getIntent().getExtras().getSerializable("objeto");
        dedondevengo = getIntent().getExtras().getInt("dedondevengo");


        if(Tab2.Lat!=0 && Tab2.Long!=0){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(Tab2.Lat, Tab2.Long));
            markerOptions.title(Tab2.Lat + " : " + Tab2.Long);
            mMap.addMarker(markerOptions);
            formularios.pointUbication= Tab2.Lat + "," + Tab2.Long + ";";
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (dedondevengo == 3) {
            StringTokenizer token = new StringTokenizer(objeto.getUbicacion(), ",;");

            //Toast.makeText(getApplicationContext(),objUbi,Toast.LENGTH_SHORT).show();
            while(token.hasMoreTokens()) {
                String Latitud = token.nextToken();
                String Longitud = token.nextToken();

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(Double.parseDouble(Latitud), Double.parseDouble(Longitud)));
                markerOptions.title(objeto.getNombre());

                mMap.addMarker(markerOptions);
            }

        }else{
            setMapLongClick(mMap);
        }


        mMap.setMyLocationEnabled(true);
        moveMarker();
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

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final String snippet = String.format(Locale.getDefault(),
                        "%1$.5f,%2$.5f;",
                        latLng.latitude,
                        latLng.longitude);

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MapsActivity.this);
                dialogo1.setTitle("Agregar Reporte");
                dialogo1.setMessage("¿Esta seguro que deseas agregar un Reporte aquí?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        formularios.pointUbication = snippet;
                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Clears the previously touched position
                        map.clear();

                        // Animating to the touched position
                        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        map.addMarker(markerOptions);

                        Tab2.Lat=latLng.latitude;
                        Tab2.Long=latLng.longitude;
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(MapsActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogo1.show();
            }
        });
    }


}
