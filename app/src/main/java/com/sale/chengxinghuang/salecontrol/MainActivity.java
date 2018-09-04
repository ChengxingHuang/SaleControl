package com.sale.chengxinghuang.salecontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_in:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_out:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_factory:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_custom:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            switch (i){
                case 0: mBottomNavigationView.setSelectedItemId(R.id.navigation_in);break;
                case 1: mBottomNavigationView.setSelectedItemId(R.id.navigation_out);break;
                case 2: mBottomNavigationView.setSelectedItemId(R.id.navigation_factory);break;
                case 3: mBottomNavigationView.setSelectedItemId(R.id.navigation_custom);break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationPagerAdapter adapter = new BottomNavigationPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.viewpager_launch);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

}
