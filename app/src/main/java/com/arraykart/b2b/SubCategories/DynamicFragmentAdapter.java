package com.arraykart.b2b.SubCategories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DynamicFragmentAdapter extends FragmentStateAdapter {
    private int mNumOfTabs;

    public DynamicFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int NumOfTabs) {
        super(fragmentManager, lifecycle);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        Fragment fragment = DynamicFragment.newInstance();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }


}
