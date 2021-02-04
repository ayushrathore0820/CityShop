package com.example.cityshop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.cityshop.Login.package_type;
import static com.example.cityshop.Login.user_id;


public class AddProduct extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    Spinner spinner1;
    //An ArrayList for Spinner Items
    private ArrayList<String> students;

    //JSON Array
    private JSONArray result;

    Bitmap bitmap;

    boolean check = true;
    Boolean CheckEditText ;
    String category_Holder,user_id_Holder;
    //AutoCompleteTextView sizetype,pagetype,rullingtype,bindtype;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    ProgressDialog progressDialog ;
    int check_image=0,check_save_image=0;
    String imageUri;

    String image_url = "image_url" ;
    String product_name_url = "product_name" ;
    String price_url = "price" ;
    String info_url = "info" ;
    String description_url = "description" ;
    String category_url = "category";

    String HttpURL = "https://macona.in/city_shop_18_api/add_category";
    String ServerUploadPath ="https://macona.in/city_shop_18_api/product_image_upload";
    String ServerUploadPath2 ="https://macona.in/city_shop_18_api/product_image_upload2";

    EditText product_name,product_price,product_info,product_description,product_category,category_edittext;
    String Getlabelname,Getlabelprice,Getlabelinfo,Getlabedesc,Getlabelcategory;
    Button save_product,add_category;
    ImageView add_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_info = findViewById(R.id.product_info);
        product_description = findViewById(R.id.product_description);
        save_product = findViewById(R.id.save_product);
       // add_category = findViewById(R.id.add_category);
        add_image = findViewById(R.id.add_image);

         imageUri = getIntent().getStringExtra("image_url");
        Picasso.with(this).load(imageUri).into(add_image);
        check_image=1;

        getDataMain(user_id);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_save_image = 1;

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });

        save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(package_type.equals("4250") || package_type.equals("5000")) {

                    Getlabelname = product_name.getText().toString();
                    Getlabelprice = product_price.getText().toString();
                    Getlabelinfo = product_info.getText().toString();
                    Getlabedesc = product_description.getText().toString();
                    if (check_save_image == 1) {
                        check_save_image = 0;
                        ImageUploadToServerFunction();
                    } else {
                        UserRegister(Getlabelname, Getlabelprice, Getlabelinfo, Getlabedesc, imageUri);
                    }
                }
                else {
                    Toast.makeText(AddProduct.this,"You are not subscribe to this pack", Toast.LENGTH_SHORT).show();
                }
                //Intent intent = new Intent(AddProduct.this,NotificationsFragment.class);
                //startActivity(intent);
            }
        });
        /*add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
         */
        //Initializing the ArrayList
        students = new ArrayList<String>();

        //Initializing Spinner
        spinner1 = (Spinner) findViewById(R.id.spinner1);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner1.setOnItemSelectedListener(this);
    }
    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        Button ok = (Button) dialogView.findViewById (R.id.buttonOk);
         category_edittext = dialogView.findViewById(R.id.category_add_edittext);

        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent intent_ok = new Intent(AddProduct.this,AddProduct.class);
                //startActivity(intent_ok);
                //finish();
                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    CategoryAddFunction(user_id_Holder,category_Holder);
                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(AddProduct.this, "Enter the category", Toast.LENGTH_LONG).show();
                }

            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void CheckEditTextIsEmptyOrNot(){

        user_id_Holder = user_id.toString();
        category_Holder = category_edittext.getText().toString();

        // otp_Holder = otp.getText().toString();


        if(TextUtils.isEmpty(category_Holder) || TextUtils.isEmpty(user_id_Holder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }
    public void CategoryAddFunction( final String user_id_, final String category_ ){

        class CategoryAddFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(AddProduct.this,"Adding new Category",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(AddProduct.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddProduct.this, AddProduct.class);
                startActivity(intent);
                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("user_id",params[0]);

                hashMap.put("category",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        CategoryAddFunctionClass categoryAddFunctionClass = new CategoryAddFunctionClass();

        categoryAddFunctionClass.execute(user_id_,category_);
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                add_image.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(AddProduct.this,"Your Product is adding","Please Wait..",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(AddProduct.this,string1,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddProduct.this,MainActivity2.class);
                startActivity(intent);

                // Setting image as transparent after done uploading.
                add_image.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(product_name_url,Getlabelname);
                HashMapParams.put(price_url,Getlabelprice);
                HashMapParams.put(info_url,Getlabelinfo);
                HashMapParams.put(description_url,Getlabedesc);
                HashMapParams.put(category_url,Getlabelcategory);
                HashMapParams.put("user_id", user_id);
                HashMapParams.put(image_url, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }
    //spinner code start

    private void getDataMain(String subcategory_id){
        //Creating a string request

        String url1 = "https://macona.in/city_shop_18_api/spinner_category?user_id=" + subcategory_id.trim();
        StringRequest stringRequest = new StringRequest(url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("result1");

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students.add(json.getString("category"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner1.setAdapter(new ArrayAdapter<String>(AddProduct.this, android.R.layout.simple_list_item_checked, students));
    }

    //Method to get student name of a particular position
    private String getName(int position){
        String main_table_id="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            main_table_id = json.getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return main_table_id;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        //Toast.makeText(getApplicationContext(),getName(position),Toast.LENGTH_LONG).show();
        final String main_id = getName(position);
        Getlabelcategory = main_id;

    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //spinner code end

    public void UserRegister( final String mobileno_, final String email_ , final String address_ ,final String name_, final String password_){

        class UserRegisterClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(AddProduct.this,"Product is Uploading ","Hold on",true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(AddProduct.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddProduct.this, MainActivity2.class);
                startActivity(intent);
                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put(product_name_url,Getlabelname);
                hashMap.put(price_url,Getlabelprice+" Rs");
                hashMap.put(info_url,Getlabelinfo);
                hashMap.put(description_url,Getlabedesc);
                hashMap.put(category_url,Getlabelcategory);
                hashMap.put("user_id", user_id);
                hashMap.put(image_url, imageUri);

                finalResult = httpParse.postRequest(hashMap,ServerUploadPath2 );

                return finalResult;
            }
        }

        UserRegisterClass userRegisterClass = new UserRegisterClass();

        userRegisterClass.execute(mobileno_,email_,address_,name_,password_);
    }

}