package com.example.cityshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="AndroidJSonDataBase3";

    public static final String TABLE_NAME="AddToCartTable";

    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Name="productname";

    public static final String Table_Column_2_Price="price";

    public static final String Table_Column_3_Mrp="mrp";

    public static final String Table_Column_4_Image="img";

    public static final String Table_Column_5_Quantity="quntity";

    public static final String Table_Column_6_Total="total";

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY, "+Table_Column_1_Name+" VARCHAR, "+Table_Column_2_Price+" VARCHAR, "+Table_Column_3_Mrp+" VARCHAR, "+Table_Column_4_Image+" VARCHAR, "+Table_Column_5_Quantity+" VARCHAR, "+Table_Column_6_Total+" VARCHAR)";
        database.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

}
