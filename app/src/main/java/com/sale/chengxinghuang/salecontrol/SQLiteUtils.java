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
    public static final String TABLE_NAME = "goods";

    private static SQLiteDatabase mGoodsSqlDb;

    public static void initSQLite(Context context){
        GoodsSQLiteOpenHelper sqLiteOpenHelper = new GoodsSQLiteOpenHelper(context, "goods.db", null, 0x01);
        mGoodsSqlDb = sqLiteOpenHelper.getWritableDatabase();
    }

    public static long insert(ContentValues values){
        return mGoodsSqlDb.insert(TABLE_NAME, null, values);
    }

    public static int delete(String whereClause, String[] whereArgs){
        return mGoodsSqlDb.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public static int update(ContentValues values, String whereClause, String[] whereArgs){
        return mGoodsSqlDb.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return mGoodsSqlDb.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    public static void execSQL(String sql){
        mGoodsSqlDb.execSQL(sql);
    }

}

class GoodsSQLiteOpenHelper extends SQLiteOpenHelper {

    public GoodsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + SQLiteUtils.TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID TEXT NOT NULL, " +
                "NUMBER TEXT NOT NULL, " +
                "DATE TEXT NOT NULL, " +
                "TIME TEXT NOT NULL, " +
                "PRICE TEXT, " +
                "TOTAL_PRICE TEXT, " +
                "MANUFACTURER TEXT, " +
                "AGENT TEXT, " +
                "CUSTOMER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

