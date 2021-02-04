package com.example.cityshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Search2 extends AppCompatActivity {

    String JsonURL = "https://macona.in/city_shop_18_api/search_details2?id=";
    private static final String  URL_PRODUCTS = "https://macona.in/city_shop_18_api/show_all_product";

    ProgressDialog progressDialog;
    Button addtocart,detail_decrease,detail_increase;
    TextView detail_count,product_name,product_category,product_price,product_contact_number,product_quantity,description;
    ImageView product_image,product_image1,product_image2,product_image3;
    String imagepro1="",imagepro2="",imagepro3="";
    int num_originalmrp,num_ourmrp,num_saving;

    RecyclerView recyclerView2;

    //a list to store all the products
    List<Product> productList2;
    //the recyclerview
    int image_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);


        addtocart = (Button) findViewById(R.id.whatsapp_addtocart);
        product_name = (TextView) findViewById(R.id.product_name);
        product_category = (TextView) findViewById(R.id.product_mrp);
        product_price = (TextView) findViewById(R.id.product_ourmrp);
        product_contact_number = (TextView) findViewById(R.id.product_saving);
        product_image = (ImageView) findViewById(R.id.product_image);
        description = (TextView) findViewById(R.id.discription);
        product_image1 = (ImageView) findViewById(R.id.product_imageimage1);
        product_image2 = (ImageView) findViewById(R.id.product_imageimage2);
        product_image3 = (ImageView) findViewById(R.id.product_imageimage3);

        description.setMovementMethod(new ScrollingMovementMethod());

        final String product_id = getIntent().getStringExtra("id");
        getData(product_id);
        progressDialog = ProgressDialog.show(Search2.this, "Loading Your Item..", null, true, true);

        //aligning button to buttom
        /*RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) addtocart.getLayoutParams();

        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addtocart.setLayoutParams(lp);*/

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getApplication().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String message = "Thank You City Shop 18 I found such a good Product i am looking from so long\n"+"Product name  - "+product_name.getText().toString()+
                        "\nI visited Your product and I am Intrested to buy this product \n Please share some more details to me \n";

                try {
                    String url = "https://api.whatsapp.com/send?phone=91"+product_contact_number.getText().toString().trim()+"&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

            product_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imagepro1.equals(""))
                    { }
                    else {
                        Picasso.with(Search2.this)
                                .load(imagepro1)
                                .resize(800, 800)
                                .into(product_image);
                    }
                }
            });
            product_image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imagepro2.equals(""))
                    { }
                    else {
                        Picasso.with(Search2.this)
                                .load(imagepro2)
                                .resize(800, 800)
                                .into(product_image);
                    }
                }
            });
            product_image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imagepro3.equals(""))
                    { }
                    else {
                        Picasso.with(Search2.this)
                                .load(imagepro3)
                                .resize(800, 800)
                                .into(product_image);
                    }
                }
            });

        recyclerView2 = findViewById(R.id.recylcerView5);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(Search2.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(manager);
        productList2 = new ArrayList<>();
        loadProducts();

    }
    private void getData(String main_id) {

        String value = main_id;

        if (value.equals("")) {
            //Toast.makeText(this, "Please Enter Data Value", Toast.LENGTH_LONG).show();
            return;
        }

        String url = JsonURL + main_id.trim();


        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
                progressDialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Search2.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(Search2.this);
        requestQueue.add(stringRequest);

    }

    private void showJSON(final String response) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String namepro = jo.getString("name");
                String contactno_pro = jo.getString("user_id");
                String pricepro = jo.getString("price");
                String categorypro = jo.getString("category");
                String imagepro = jo.getString("image_url");
                String descpro = jo.getString("description");
                 imagepro1 = jo.getString("image_one");
                 imagepro2 = jo.getString("image_two");
                 imagepro3 = jo.getString("image_three");


                product_name.setText(namepro);
                product_contact_number.setText(contactno_pro);
                product_price.setText(pricepro);
                product_category.setText(categorypro);
                description.setText(descpro);
                Picasso.with(Search2.this)
                        .load(imagepro)
                        .resize(800, 800)
                        .into(product_image);
                if(imagepro1.equals("")) {
                }
                else {
                    Picasso.with(Search2.this)
                            .load(imagepro1)
                            .resize(800, 800)
                            .into(product_image1);
                }
                if(imagepro2.equals("")) {
                }
                else {
                    Picasso.with(Search2.this)
                            .load(imagepro2)
                            .resize(800, 800)
                            .into(product_image2);
                }
                if(imagepro3.equals("")) {
                }
                else {
                    Picasso.with(Search2.this)
                            .load(imagepro3)
                            .resize(800, 800)
                            .into(product_image3);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //recycler view code starts..

    private void loadProducts() {

        final ProgressDialog progressDialog = new ProgressDialog(Search2.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                productList2.add(new Product(
                                        product.getInt("id"),
                                        product.getString("product_name"),
                                        product.getString("image_url"),
                                        product.getString("user_id")

                                ));
                            }

                            progressDialog.dismiss();
                            //creating adapter object and setting it to recyclerview
                            ProductAdapter2 adapter = new ProductAdapter2(getApplication(), productList2);
                            recyclerView2.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Connection Failed: ", String.valueOf(error));
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }
    //recycer view code ends..
}