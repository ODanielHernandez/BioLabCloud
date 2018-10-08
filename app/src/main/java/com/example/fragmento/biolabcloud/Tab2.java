package com.example.fragmento.biolabcloud;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Tab2 extends Fragment {
    private OnFragmentInteractionListener mListener;
    DatabaseReference Freference, Freference2, Freference3;
    FirebaseDatabase Fdatabase;

    long firstDate = 1532581200;

    GraphView hum,temp;
    LineGraphSeries series,series2,series3;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd-hh");


    public Tab2() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);


        FirebaseUser userid = FirebaseAuth.getInstance().getCurrentUser() ;

        Fdatabase = FirebaseDatabase.getInstance();
        Freference = Fdatabase.getReference("Users").child(userid.getUid()).child("iHumidStore");
        Freference2 = Fdatabase.getReference("Users").child(userid.getUid()).child("iHumidStoreB");
        Freference3 = Fdatabase.getReference("Users").child(userid.getUid()).child("iHumidStoreC");


        hum = (GraphView) v.findViewById(R.id.hum);
        temp = (GraphView) v.findViewById(R.id.temp);

        series = new LineGraphSeries();
        series2 = new LineGraphSeries();
        series3 = new LineGraphSeries();

        GridLabelRenderer gridLabelRenderer = hum.getGridLabelRenderer();
        GridLabelRenderer gridLabelRenderer2 = temp.getGridLabelRenderer();

        hum.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    double minX = hum.getViewport().getMinX(false);
                    double maxX = hum.getViewport().getMaxX(false);
                    double diff = maxX - minX;

                    Date date = new Date((long) value*1000);
                    String label =  null;

                    if(diff >= 86400){
                        label = DateFormat.format("MM/dd-hh", date).toString();
                    }else{
                        label = DateFormat.format("hh:mm", date).toString();
                    }

                    return label;
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        hum.getGridLabelRenderer().setGridColor(Color.WHITE);
        hum.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        hum.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        hum.getGridLabelRenderer().setNumHorizontalLabels(3);

        hum.getViewport().setScalable(true);
        hum.getViewport().setScrollable(true);
        hum.getViewport().setXAxisBoundsManual(true);
        hum.getViewport().setMinX(hum.getViewport().getMinX(false));
        hum.getViewport().setMaxX(hum.getViewport().getMinX(false) + 24*60*60*1000);


        series.setTitle("Humedad");
        series.setColor(Color.rgb(117,224,156));
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(20, 117, 224, 156));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10f);


        temp.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    double minX = temp.getViewport().getMinX(false);
                    double maxX = temp.getViewport().getMaxX(false);
                    double diff = maxX - minX;

                    Date date = new Date((long) value*1000);
                    String label =  null;

                    if(diff >= 86400){
                        label = DateFormat.format("MM/dd-hh", date).toString();
                    }else{
                        label = DateFormat.format("hh:mm", date).toString();
                    }

                    return label;
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        temp.getGridLabelRenderer().setGridColor(Color.WHITE);
        temp.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        temp.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        temp.getGridLabelRenderer().setNumHorizontalLabels(3);

        temp.getViewport().setScalable(true);
        temp.getViewport().setScrollable(true);
        temp.getViewport().setXAxisBoundsManual(true);
        temp.getViewport().setMinX(temp.getViewport().getMinX(false));
        temp.getViewport().setMaxX(temp.getViewport().getMinX(false) + 24*60*60*1000);


        series2.setTitle("Temp C°");
        series2.setColor(Color.rgb(117,224,156));
        series2.setDrawBackground(true);
        series2.setBackgroundColor(Color.argb(20, 117, 224, 156));
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10f);


        hum.getLegendRenderer().setVisible(true);
        hum.getLegendRenderer().setTextSize(25);
        hum.getLegendRenderer().setBackgroundColor(Color.argb(150, 255, 255, 255));
        hum.getLegendRenderer().setTextColor(Color.rgb(0,0,0));
        hum.getLegendRenderer().setFixedPosition(0, 0);



        temp.getLegendRenderer().setVisible(true);
        temp.getLegendRenderer().setTextSize(25);
        temp.getLegendRenderer().setBackgroundColor(Color.argb(150, 255, 255, 255));
        temp.getLegendRenderer().setTextColor(Color.rgb(0,0,0));
        temp.getLegendRenderer().setFixedPosition(0, 0);

        hum.addSeries(series);
        temp.addSeries(series2);




        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        long x = new Date().getTime();

        Freference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp= new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index=0;

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    dp[index]= new DataPoint(pointValue.gettime(),pointValue.gethumid());
                    index++;
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Freference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp= new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index=0;

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    dp[index]= new DataPoint(pointValue.gettime(),pointValue.getTempC());
                    index++;
                }
                series2.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Freference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp= new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index=0;

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    dp[index]= new DataPoint(pointValue.gettime(),pointValue.getTempF());
                    index++;
                }
                series3.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
