package com.sale.chengxinghuang.salecontrol;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String ERROR_UPDATE_STOCK = "ERROR_UPDATE_STOCK";
    public static final String ERROR_INSERT_STOCK = "ERROR_INSERT_STOCK";
    public static final String ERROR_NO_ID_IN_STOCK = "ERROR_NO_ID_IN_STOCK";
    public static final String ERROR_INSERT_DETAILS = "ERROR_INSERT_DETAILS";

    private static final String TAG = "MainActivity";

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
                case R.id.navigation_custom:
                    mViewPager.setCurrentItem(2);
                    return true;
//                case R.id.navigation_custom:
//                    mViewPager.setCurrentItem(3);
//                    return true;
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
                case 2: mBottomNavigationView.setSelectedItemId(R.id.navigation_custom);break;
                //case 3: mBottomNavigationView.setSelectedItemId(R.id.navigation_custom);break;
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

        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationPagerAdapter adapter = new BottomNavigationPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager_launch);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        SQLiteUtils.initSQLite(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.onActionViewCollapsed();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor cursor = SQLiteUtils.query(SQLiteUtils.STOCK_TABLE_NAME,
                        new String[]{SQLiteUtils.KEY_ID, SQLiteUtils.KEY_NUMBER},
                        SQLiteUtils.KEY_ID + "=?",
                        new String[]{query});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int currentNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SQLiteUtils.KEY_NUMBER)));
                    showAlertDialog(getString(R.string.store_count) + currentNumber);
                }else{
                    showAlertDialog(getString(R.string.no_record));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void updateTables(final String id, final String number, final int type, final String price, final String totalPrice, final String manufacturer, final String agent, final String customer){
        try {
            // SQLite事务，必须两个表一起更新
            SQLiteUtils.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(SQLiteUtils.KEY_ID, id);
            values.put(SQLiteUtils.KEY_NUMBER, number);

            /*
             ********************************
             ******** 更新stock table *******
             ********************************
             */
            Cursor cursor = SQLiteUtils.query(SQLiteUtils.STOCK_TABLE_NAME,
                    new String[]{SQLiteUtils.KEY_ID, SQLiteUtils.KEY_NUMBER},
                    SQLiteUtils.KEY_ID + "=?",
                    new String[]{id});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int currentNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SQLiteUtils.KEY_NUMBER)));
                // ID存在：入库、出库、退货处理，更新数据
                if(SQLiteUtils.TYPE_IN == type || SQLiteUtils.TYPE_RETURN == type){
                    currentNumber += Integer.parseInt(number);
                }else if(SQLiteUtils.TYPE_OUT == type){
                    currentNumber -= Integer.parseInt(number);
                }

                values.put(SQLiteUtils.KEY_NUMBER, currentNumber + "");
                Log.d(TAG, "update id = " + id + ", number = " + currentNumber);
                if (1 != SQLiteUtils.update(SQLiteUtils.STOCK_TABLE_NAME, values, "ID=?", new String[]{id})) {
                    throw new Exception(ERROR_UPDATE_STOCK);
                }
            } else {
                // ID不存在：插入数据or提示用户
                if(SQLiteUtils.TYPE_IN == type || SQLiteUtils.TYPE_RETURN == type){
                    if(-1 == SQLiteUtils.insert(SQLiteUtils.STOCK_TABLE_NAME, values)){
                        throw new Exception(ERROR_INSERT_STOCK);
                    }
                }else if(SQLiteUtils.TYPE_OUT == type){
                    throw new Exception(ERROR_NO_ID_IN_STOCK);
                }
            }

            /*
             ********************************
             ******* 更新details table ******
             ********************************
             */
            DateFormat df = SimpleDateFormat.getDateInstance();
            DateFormat dfTime = SimpleDateFormat.getTimeInstance();
            String date = df.format(new Date());
            String time = dfTime.format(new Date());
            Log.d(TAG, "current date:" + date + ", current time:" + time);

            values.put(SQLiteUtils.KEY_PRICE, price);
            values.put(SQLiteUtils.KEY_TOTAL_PRICE, totalPrice);
            values.put(SQLiteUtils.KEY_MANUFACTURER, manufacturer);
            values.put(SQLiteUtils.KEY_DATE, date);
            values.put(SQLiteUtils.KEY_TIME, time);
            values.put(SQLiteUtils.KEY_TYPE, type);
            values.put(SQLiteUtils.KEY_AGENT, agent);
            values.put(SQLiteUtils.KEY_CUSTOMER, customer);
            if (-1 == SQLiteUtils.insert(SQLiteUtils.DETAIL_TABLE_NAME, values)) {
                throw new Exception(ERROR_INSERT_DETAILS);
            }

            SQLiteUtils.setTransactionSuccessful();

            switch(type){
                case SQLiteUtils.TYPE_IN:
                    Toast.makeText(MainActivity.this, R.string.success_in, Toast.LENGTH_SHORT).show();
                    break;
                case SQLiteUtils.TYPE_OUT:
                    Toast.makeText(MainActivity.this, R.string.success_out, Toast.LENGTH_SHORT).show();
                    break;
                case SQLiteUtils.TYPE_RETURN:
                    Toast.makeText(MainActivity.this, R.string.success_return, Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (Exception e){
            showAlertDialog(getString(R.string.handler_sql_fail));
        }finally {
            SQLiteUtils.endTransaction();
        }
    }

    private void showAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.attention);
        builder.setMessage(message);
        builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
