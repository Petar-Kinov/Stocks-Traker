package com.example.stonks.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.stonks.Fragments.CompanyFragment;
import com.example.stonks.Fragments.OverviewFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {
            return new OverviewFragment();
        }
        return new CompanyFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
