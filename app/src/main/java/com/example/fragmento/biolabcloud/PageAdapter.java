package com.example.fragmento.biolabcloud;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter{

    int numTabs;

    public PageAdapter(FragmentManager fm, int numberTabs) {
        super(fm);
        this.numTabs = numberTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                 Tab3 tab3 = new Tab3();
                 return tab3;
            case 2:
                 Tab4 tab4 = new Tab4();
                 return tab4;
            case 3:
                Tab5 tab5 = new Tab5();
                return tab5;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
