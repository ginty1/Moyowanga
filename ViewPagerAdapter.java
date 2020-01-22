package com.example.moyowanga;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int postion) {
        return fragments.get(postion);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }
}
