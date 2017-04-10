package com.coco.csdnapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.coco.csdnapp.fragment.MainFragment;

/**
 * TabLayout的adapter
 */

public class TabAdapter extends FragmentPagerAdapter {

    public static final String[] TITLES = new String[] { "业界", "移动", "研发", "程序员", "云计算" };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position % TITLES.length];
    }

    @Override
    public Fragment getItem(int position) {
        MainFragment fragment = new MainFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
