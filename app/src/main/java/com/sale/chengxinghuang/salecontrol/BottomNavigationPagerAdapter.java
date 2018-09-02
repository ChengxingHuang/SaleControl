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

    public static final int CATEGORY_FRAGMENT_POSITION = 0x00;
    public static final int LIST_FRAGMENT_POSITION = 0x01;
    private int mCurrentPosition;

    public BottomNavigationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        if(0 == position){
//            return "Category";
//        }else {
//            return "List";
//        }
//    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new InFragment();
            case 1: return new OutFragment();
            case 2: return new ReturnToFactoryFragment();
            case 3: return new CustomReturnFragment();
        }
        Log.d("huangcx", "Fatal Error!!!");
        return null;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentPosition = position;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public int getCurrentPosition(){
        return mCurrentPosition;
    }
}
