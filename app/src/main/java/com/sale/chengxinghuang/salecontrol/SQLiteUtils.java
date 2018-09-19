package com.sale.chengxinghuang.salecontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteUtils {

    public static final String KEY_ID = "ID";
    public static final String KEY_NUMBER = "NUMBER";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_TIME = "TIME";
    public static final String KEY_PRICE = "PRICE";
    public static final String KEY_TOTAL_PRICE = "TOTAL_PRICE";
    public static final String KEY_MANUFACTURER = "MANUFACTURER";
    public static final String KEY_AGENT = "AGENT";
    public static final String KEY_CUSTOMER = "CUSTOMER";
    public static final String KEY_TYPE = "TYPE";
    public static final String DETAIL_TABLE_NAME = "detail";
    public static final String STOCK_TABLE_NAME = "stock";
    public static final int TYPE_IN = 1;
    public static final int TYPE_OUT = 2;
    public static final int TYPE_RETURN = 3;

    private static final int DB_VERSION = 0x01;

    private static SQLiteDatabase mGoodsSqlDb;

    public static void initSQLite(Context context){
        GoodsSQLiteOpenHelper sqLiteOpenHelper = new GoodsSQLiteOpenHelper(context, "goods.db", null, DB_VERSION);
        mGoodsSqlDb = sqLiteOpenHelper.getWritableDatabase();
    }

    public static long insert(String table, ContentValues values){
        return mGoodsSqlDb.insert(table, null, values);
    }

    public static int delete(String table, String whereClause, String[] whereArgs){
        return mGoodsSqlDb.delete(table, whereClause, whereArgs);
    }

    public static int update(String table, ContentValues values, String whereClause, String[] whereArgs){
        return mGoodsSqlDb.update(table, values, whereClause, whereArgs);
    }

    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
        return mGoodsSqlDb.query(table, columns, selection, selectionArgs, null, null, null);
    }

    public static void execSQL(String sql){
        mGoodsSqlDb.execSQL(sql);
    }

    public static void beginTransaction(){
        mGoodsSqlDb.beginTransaction();
    }

    public static void setTransactionSuccessful(){
        mGoodsSqlDb.setTransactionSuccessful();
    }

    public static void endTransaction(){
        mGoodsSqlDb.endTransaction();
    }
}

class GoodsSQLiteOpenHelper extends SQLiteOpenHelper {

    public GoodsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + SQLiteUtils.DETAIL_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID TEXT NOT NULL, " +
                "NUMBER TEXT NOT NULL, " +
                "DATE TEXT NOT NULL, " +
                "TIME TEXT NOT NULL, " +
                "TYPE INTEGER, " +
                "PRICE TEXT, " +
                "TOTAL_PRICE TEXT, " +
                "MANUFACTURER TEXT, " +
                "AGENT TEXT, " +
                "CUSTOMER TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + SQLiteUtils.STOCK_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID TEXT NOT NULL, " +
                "NUMBER TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

