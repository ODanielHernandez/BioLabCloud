package com.example.fragmento.biolabcloud;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class Tab3 extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Tab3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab3, container, false);


        /*ImageButton facebook = (ImageButton) v.findViewById(R.id.facebook);
        ImageButton instagram = (ImageButton) v.findViewById(R.id.instagram);
        ImageButton reddit = (ImageButton) v.findViewById(R.id.reddit);

        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.facebook.com/"));
                startActivity(viewIntent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.instagram.com/"));
                startActivity(viewIntent);
            }
        });
        reddit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.reddit.com/"));
                startActivity(viewIntent);
            }
        });
*/
        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
