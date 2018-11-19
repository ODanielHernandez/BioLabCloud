package com.example.fragmento.biolabcloud;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fragmento.biolabcloud.Tabs.Tab1;
import com.example.fragmento.biolabcloud.Tabs.Tab2;
import com.example.fragmento.biolabcloud.Tabs.Tab3;
import com.example.fragmento.biolabcloud.Tabs.Tab4;


public class MainActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener,Tab3.OnFragmentInteractionListener,Tab4.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("BioLab"));
        tabLayout.addTab(tabLayout.newTab().setText("Mapa"));
        tabLayout.addTab(tabLayout.newTab().setText("Estado"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Menu"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TextView tv1 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
        tv1.setScaleY(-1);
        TextView tv2 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(1));
        tv2.setScaleY(-1);
        TextView tv3 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(2)).getChildAt(1));
        tv3.setScaleY(-1);
        TextView tv4 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(3)).getChildAt(1));
        tv4.setScaleY(-1);
        TextView tv5 = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(4)).getChildAt(1));
        tv5.setScaleY(-1);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    /**
     *  una sola funcion para todas las peticiones
     * @param
     * @return
     */


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}


