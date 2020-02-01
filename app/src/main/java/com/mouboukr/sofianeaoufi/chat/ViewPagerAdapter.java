package com.mouboukr.sofianeaoufi.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 2018-03-22.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {




    private  final List<Fragment> lstFragment=new ArrayList<>();
    private final  List<String> lstTitle=new ArrayList<>();


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lstTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitle.get(position);
    }



     public void AddFragment(Fragment fragment,String title)
     {
         lstFragment.add(fragment);
         lstTitle.add(title);


     }



}
