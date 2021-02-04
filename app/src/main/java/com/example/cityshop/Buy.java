package com.example.cityshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.cityshop.SQLiteHelper.TABLE_NAME;
import static com.example.cityshop.VendorAdapter.CHECK_CART;
import static com.example.cityshop.ViewProductOfVendor.CHECK_ADDTOCART;

public class Buy extends AppCompatActivity {

    TextView total_saving,pay;
    RequestQueue requestQueue,requestQueue2;

    Button checkout,pay_online;
    EditText name, address, state, city,pincode,cupon,emailid;
    String name_Holder, address_Holder, state_Holder, city_Holder,pincode_Holder,cupon_Holder,emailid_Holder,amount_Holder,discount_Holder,e_sp,a_sp,n_sp;
    public  static String a_sp_,n_sp_;
    String finalResult ;
    String HttpURL = "https://sahejindia.com/Android/sahejindia_transaction.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog,progressDialog2;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    SharedPreferences shared_email,shared_address,shared_name;

    SQLiteDatabase sqLiteDatabaseObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        shared_email = getSharedPreferences("email_sp",MODE_PRIVATE);
        shared_address = getSharedPreferences("address_sp",MODE_PRIVATE);
        shared_name = getSharedPreferences("name_sp",MODE_PRIVATE);

        checkout = findViewById(R.id.checkout);
        name = findViewById(R.id.fullname2);
        address = findViewById(R.id.address2);
        state = findViewById(R.id.state2);
        city = findViewById(R.id.city2);
        emailid = findViewById(R.id.emailid2);

        //getting amount from databse starts ...
        pay =findViewById(R.id.pay);

        e_sp = shared_email.getString("email_sp",emailid_Holder);
        emailid.setText(e_sp);
        a_sp = shared_address.getString("address_sp",address_Holder);
        address.setText(a_sp);
        n_sp = shared_name.getString("name_sp",name_Holder);
        name.setText(n_sp);

        a_sp_ = shared_address.getString("address_sp",address_Holder);
        n_sp_ = shared_name.getString("name_sp",name_Holder);

        sqLiteDatabaseObj = openOrCreateDatabase("AndroidJSonDataBase3", Context.MODE_PRIVATE, null);

        Cursor cur = sqLiteDatabaseObj.rawQuery("SELECT SUM(" + (SQLiteHelper.Table_Column_6_Total) + ") FROM " + TABLE_NAME, null);
        if (cur.moveToFirst()) {

            int total = cur.getInt(0);
            pay.setText(String.valueOf(total).toString().trim());

        }
        else {

        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CheckEditTextIsEmptyOrNot();
               // Toast.makeText(Buy.this, "Fill all the fields", Toast.LENGTH_LONG).show();

                final String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
                File file = new File(directory_path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // Export SQLite DB as EXCEL FILE

                if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {

                    // Requesting the permission
                    ActivityCompat.requestPermissions((Activity) Buy.this,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
                }
                else {

                    SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), SQLiteHelper.DATABASE_NAME,directory_path);
                    sqliteToExcel.exportSingleTable("AddToCartTable", "cityshop18_newOrder.xls", new SQLiteToExcel.ExportListener() {
                        @Override
                        public void onStart() {
                            Toast.makeText(Buy.this, "start Exported", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCompleted(String filePath) {
                            Toast.makeText(Buy.this, "Successfully Exported", Toast.LENGTH_LONG).show();

                               Intent share = new Intent("android.intent.action.SEND");
                            File outputFile = new File(directory_path,"cityshop18_newOrder.xls");

                            Uri uri = Uri.fromFile(outputFile);
                               // share.setAction(Intent.ACTION_SEND);
                            share.setComponent(new ComponentName("com.whatsapp","com.whatsapp.ContactPicker"));
                                share.setType("application/ms-excel");
                                share.putExtra(Intent.EXTRA_STREAM, uri);
                            share.putExtra("jid", PhoneNumberUtils.stripSeparators("91"+CHECK_CART.toString().trim())+"@s.whatsapp.net");
                            share.putExtra(Intent.EXTRA_TEXT,"thank you");
                                share.setPackage("com.whatsapp");

                                startActivity(share);



                            }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(Buy.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                    name_Holder = name.getText().toString().trim();
                    address_Holder = address.getText().toString().trim();
                    state_Holder = state.getText().toString().trim();
                    city_Holder = city.getText().toString().trim();
                    emailid_Holder = emailid.getText().toString().trim();
                    amount_Holder= pay.getText().toString().trim();

                    shared_email.edit().putString("email_sp",emailid_Holder).apply();
                    shared_address.edit().putString("address_sp",address_Holder).apply();
                    shared_name.edit().putString("name_sp",name_Holder).apply();
                   //Toast.makeText(Buy.this, emailid_Holder, Toast.LENGTH_LONG).show();
                    showCustomDialog();
                }
            }
        });

    }

    private void showCustomDialog() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.confirm_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        Button ok = (Button) dialogView.findViewById (R.id.buttonOk2);
        TextView success = dialogView.findViewById(R.id.success2);
        success.setText(amount_Holder.toString());
        TextView addressorder = dialogView.findViewById(R.id.addressorder2);
        addressorder.setText(address_Holder.toString());
        TextView cityorder = dialogView.findViewById(R.id.cityorder2);
        cityorder.setText(name_Holder.toString());
        TextView pinorder = dialogView.findViewById(R.id.pinorder2);
        pinorder.setText(emailid_Holder.toString());
        TextView mobilenumber = dialogView.findViewById(R.id.mobileorder2);
        mobilenumber.setText(state_Holder.toString());


        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                PackageManager packageManager = getApplicationContext().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String message = " You have recieved a new order from City Shop 18 \n Customer name - "+name_Holder+"\n Mobile Number - "+state_Holder+"\n Shipping Address - "+address_Holder+"\n"+city_Holder+"\n\n ~ThankYou";

                try {
                    String url = "https://api.whatsapp.com/send?phone=91"+CHECK_CART.toString()+"&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

               /* Intent intent_ok = new Intent(Buy.this,MainActivity.class);
                startActivity(intent_ok);
                finish();*/
                sqLiteDatabaseObj.delete(TABLE_NAME, null, null);


            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}