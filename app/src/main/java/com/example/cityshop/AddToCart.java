package com.example.cityshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddToCart extends AppCompatActivity {

    Handler mHandler;

    SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    ListAdapter listAdapter ;
    ListView LISTVIEW;
    Button buynow;

    ArrayList<String> ID_Array;
    ArrayList<String> NAME_Array;
    ArrayList<String> PRICE_Array;
    ArrayList<String> MRP_Array;
    ArrayList<String> IMAGE_Array;
    ArrayList<String> QUANTITY_Array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        buynow = findViewById(R.id.buynow);

        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,5000);

        LISTVIEW = (ListView) findViewById(R.id.listView1);

        ID_Array = new ArrayList<String>();

        NAME_Array = new ArrayList<String>();

        PRICE_Array = new ArrayList<String>();

        MRP_Array = new ArrayList<String>();

        IMAGE_Array = new ArrayList<String>();

        QUANTITY_Array = new ArrayList<String>();

        sqLiteHelper = new SQLiteHelper(this);

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddToCart.this,Buy.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }

    private void ShowSQLiteDBdata() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_NAME+"", null);

        ID_Array.clear();
        NAME_Array.clear();
        PRICE_Array.clear();
        MRP_Array.clear();
        IMAGE_Array.clear();
        QUANTITY_Array.clear();

        if (cursor.moveToFirst()) {
            do {

                ID_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID)));

                NAME_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Name)));

                PRICE_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2_Price)));

                MRP_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Mrp)));

                IMAGE_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_4_Image)));

                QUANTITY_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_5_Quantity)));


            } while (cursor.moveToNext());
        }

        listAdapter = new ListAdapter(AddToCart.this,

                ID_Array,
                NAME_Array,
                PRICE_Array,
                MRP_Array,
                IMAGE_Array,
                QUANTITY_Array
        );

        LISTVIEW.setAdapter(listAdapter);

        cursor.close();
    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //Toast.makeText(AddToCart.this,"Refereshing data",Toast.LENGTH_SHORT).show();
            ShowSQLiteDBdata();

            AddToCart.this.mHandler.postDelayed(m_Runnable, 1000);
        }

    };
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();

    }

}