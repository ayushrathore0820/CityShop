package com.example.cityshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewProductOfVendor extends AppCompatActivity {

     public static String CHECK_ADDTOCART;

    //search start..
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private SimpleCursorAdapter myAdapter;

    SearchView searchView;
    private String[] strArrData = {"No Suggestions"};
    //end

    SliderLayout sliderLayout;
    private List<List_Data> list_dataList;
    int image_count=0;
    Button service_tab,product_tab;
    RecyclerView recylcerView_product;
    ImageView vendor_logo_image;
    CardView cardview_1,cardview_2,cardview_3,cardview_4,cardview_5,cardview_6;
    String fb_link,insta_link,twt_link;

    //a list to store all the products
    List<Service> productList_vendor;
    List<Service> productList_service;
    private static final String  URL_PRODUCT = "https://macona.in/city_shop_18_api/vendor_product?user_id=";
    private static final String  URL_SERVICE = "https://macona.in/city_shop_18_api/vendor_service?user_id=";
    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private static final String HI = "https://macona.in/city_shop_18_api/slider_image";
     String mobile_vendor;
    ProgressDialog progressDialog2;

    // URL of object to be parsed
    String JsonURL = "https://macona.in/city_shop_18_api/bussiness_profile_show?mobileno=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_of_vendor);
        service_tab =findViewById(R.id.service_tab);
        product_tab =findViewById(R.id.product_tab);
        vendor_logo_image = findViewById(R.id.vendor_logo_img);
        cardview_1 = findViewById(R.id.cardview_1);
        cardview_2 = findViewById(R.id.cardview_2);
        cardview_3 = findViewById(R.id.cardview_3);
        cardview_4 = findViewById(R.id.cardview_4);
        cardview_5 = findViewById(R.id.cardview_5);
        cardview_6 = findViewById(R.id.cardview_6);
         mobile_vendor= getIntent().getStringExtra("mobile");
         CHECK_ADDTOCART = mobile_vendor;
        Toast.makeText(getApplicationContext(),CHECK_ADDTOCART,Toast.LENGTH_LONG).show();
        getData(mobile_vendor);
        progressDialog2 = ProgressDialog.show(ViewProductOfVendor.this,null,"Loading Product details please wait....",true,true);


        recylcerView_product = findViewById(R.id.recylcerView_vendor_tab);
        recylcerView_product.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recylcerView_product.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        productList_vendor = new ArrayList<>();
        loadProduct();
        productList_service = new ArrayList<>();


        setSliderViews();
        sliderLayout = findViewById(R.id.imageSlider3);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.THIN_WORM);
        list_dataList=new ArrayList<>();
        sliderLayout.setScrollTimeInSec(2);
        service_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadService();
                service_tab.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                service_tab.setEnabled(false);
                product_tab.setEnabled(true);
                product_tab.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

            }
        });
        product_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProduct();
                product_tab.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                product_tab.setEnabled(false);
                service_tab.setEnabled(true);
                service_tab.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
        });
        cardview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(fb_link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        cardview_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(insta_link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        cardview_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getApplication().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String message = "Customer from City Shop 18 \n"+"Hii";

                try {
                    String url = "https://api.whatsapp.com/send?phone=91"+mobile_vendor.toString().trim()+"&text=" + URLEncoder.encode(message, "UTF-8");
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
        cardview_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=mobile_vendor.toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_DENIED) {

                    // Requesting the permission
                    ActivityCompat.requestPermissions((Activity) ViewProductOfVendor.this,
                            new String[] { Manifest.permission.CALL_PHONE },1);
                }
                else {
                    startActivity(callIntent);
                }
            }
        });
        cardview_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "App will be shared when published on playstore";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        cardview_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(twt_link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        //search
        final String[] from = new String[] {"name"};
        final int[] to = new int[] {android.R.id.text1};

        // setup SimpleCursorAdapter
        myAdapter = new SimpleCursorAdapter(ViewProductOfVendor.this, android.R.layout.simple_spinner_dropdown_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Fetch data from mysql table using AsyncTask
        new AsyncFetch().execute();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item1) {
            //do your function here
            Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.item2) {
            //do your function here
            Intent intent = new Intent(ViewProductOfVendor.this,AddToCart.class);
            startActivity(intent);
        }
        if (id == R.id.item3) {
            //do your function here
            Toast.makeText(this, "vendor Login", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
    //sliding view coded main function...
    private void setSliderViews() {
        request = new JsonArrayRequest(HI, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    image_count++;
                    try {
                        jsonObject = response.getJSONObject(i);
                        List_Data listData = new List_Data(jsonObject.getString("image_path"));
                        list_dataList.add(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setupdata(list_dataList);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
    private void setupdata(List<List_Data> list_dataList) {

        for (int i = 0; i <= image_count-1; i++) {
            final List_Data ld = list_dataList.get(i);
            SliderView sliderView = new SliderView(this);
            sliderView.setImageUrl(ld.getImageurl());

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            final int finalI = i;
            sliderLayout.addSliderView(sliderView);

        }
    }
    //recycler view code starts..
    //recycler view code starts..

    private void loadProduct() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCT+mobile_vendor.toString().trim(),
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
                                productList_vendor.add(new Service(
                                        product.getInt("id"),
                                        product.getString("product_name"),
                                        product.getString("image_url"),
                                        product.getString("price"),
                                        product.getString("mrp")

                                ));
                            }

                            progressDialog.dismiss();
                            //creating adapter object and setting it to recyclerview
                            VendorAdapter adapter = new VendorAdapter(getApplication(), productList_vendor);
                            recylcerView_product.setAdapter(adapter);
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
        Volley.newRequestQueue(this).add(stringRequest);


    }
    private void loadService() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SERVICE+mobile_vendor.toString().trim(),
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
                                productList_service.add(new Service(
                                        product.getInt("id"),
                                        product.getString("service_name"),
                                        product.getString("image_url"),
                                        product.getString("category"),
                                        product.getString("user_id")

                                ));
                            }

                            progressDialog.dismiss();
                            //creating adapter object and setting it to recyclerview
                            ServiceAdapter adapter = new ServiceAdapter(getApplication(), productList_service);
                            recylcerView_product.setAdapter(adapter);
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
        Volley.newRequestQueue(this).add(stringRequest);


    }

    private void getData(String main_id) {

        String value = main_id;

        if (value.equals("")) {
            Toast.makeText(this, "Please Enter Data Value", Toast.LENGTH_LONG).show();
            return;
        }

        String url =JsonURL + main_id.trim();



        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
                progressDialog2.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewProductOfVendor.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSON(final String response) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                 fb_link = jo.getString("facebook_link");
                 insta_link = jo.getString("instagram_link");
                 twt_link = jo.getString("twitter_link");
                String imagepro = jo.getString("vendor_image");

                Picasso.with(this)
                        .load(imagepro)
                        .into(vendor_logo_image);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //search code start..

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_activity,menu);
        //return super.onCreateOptionsMenu(menu);
        // adds item to action bar
        // getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service search_action_mainitems
        searchView = (SearchView) findViewById(R.id.search_product);
        searchView.setIconified(true);
        SearchManager searchManager = (SearchManager) ViewProductOfVendor.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchView != null) {
            searchView.setIconified(true);
            //searchView = (SearchView) searchView.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ViewProductOfVendor.this.getComponentName()));
            searchView.setIconified(true);
            searchView.setSuggestionsAdapter(myAdapter);
            // Getting selected (clicked) item suggestion
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionClick(int position) {

                    // Add clicked text to search box
                    CursorAdapter ca = searchView.getSuggestionsAdapter();
                    Cursor cursor = ca.getCursor();
                    cursor.moveToPosition(position);
                    searchView.setQuery(cursor.getString(cursor.getColumnIndex("name")),false);
                    String test = String.valueOf(cursor.getString(cursor.getColumnIndex("name")));
                    Toast.makeText(ViewProductOfVendor.this,test, Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(Main_Items.this,BestSelling.class);
                    getSearch(test);
                    //intent.putExtra("id", serach_console);
                    //serach_console = strArrData2;
                    //Toast.makeText(Dashboard.this,serach_console, Toast.LENGTH_LONG).show();
                    //startActivity(intent);
                    return true;
                }

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    // Filter data
                    final MatrixCursor mc = new MatrixCursor(new String[]{ BaseColumns._ID, "name" });
                    for (int i=0; i<strArrData.length; i++) {
                        if (strArrData[i].toLowerCase().startsWith(s.toLowerCase()))
                            mc.addRow(new Object[] {i, strArrData[i]});
                    }
                    myAdapter.changeCursor(mc);
                    return false;
                }
            });
        }

        return true;
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }

            // User entered text and pressed search button. Perform task ex: fetching data from database and display

        }
    }


    // Create class AsyncFetch
    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(ViewProductOfVendor.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides or your JSON file address
                url = new URL("https://macona.in/city_shop_18_api/search");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we receive data
                conn.setDoOutput(true);
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            ArrayList<String> dataList = new ArrayList<String>();
            pdLoading.dismiss();


            if(result.equals("No Results Found.")) {

                // Do some action if no data from database

            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList
                    for ( int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        //dataList.add(json_data.getString("name"));
                        dataList.add(json_data.getString("product_name"));
                    }

                    strArrData = dataList.toArray(new String[dataList.size()]);

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Toast.makeText(ViewProductOfVendor.this, e.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(Main_Items.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    private void getSearch(String main_id) {

        String value = main_id;

        String url = "https://macona.in/city_shop_18_api/search_console?name="+main_id;



        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject jObj = null;
                try {
                    jObj = arr.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String date = jObj.getString("id");
                    //serach_console = date;
                    Intent intent = new Intent(ViewProductOfVendor.this,Search2.class);
                    intent.putExtra("id", date);
                    //serach_console = strArrData2;
                    Toast.makeText(ViewProductOfVendor.this,date, Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    // Toast.makeText(Dashboard.this,serach_console, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewProductOfVendor.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //serach code end...

}