package com.sale.chengxinghuang.salecontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by huang on 2018/5/30.
 */

public class BottomNavigationPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "NavigationPagerAdapter";

    public BottomNavigationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new InFragment();
            case 1: return new OutFragment();
            case 2: return new ReturnToFactoryFragment();
            case 3: return new CustomReturnFragment();
        }
        Log.d(TAG, "Fatal Error:NavigationPager more than 4!!!");
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
