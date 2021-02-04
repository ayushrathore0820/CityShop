package com.example.cityshop.ui.home;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cityshop.AddToCart;
import com.example.cityshop.List_Data;
import com.example.cityshop.Login;
import com.example.cityshop.MainActivity;
import com.example.cityshop.MainActivity2;
import com.example.cityshop.Product;
import com.example.cityshop.ProductAdapter2;
import com.example.cityshop.ProductAdapter3;
import com.example.cityshop.R;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.cityshop.Login.package_type;
import static com.example.cityshop.Login.payment_type_try;
import static com.example.cityshop.Login.user_id;
import static com.example.cityshop.Login.user_mobile;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    SliderLayout sliderLayout;
    private List<List_Data> list_dataList;
    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private static final String HI = "https://macona.in/city_shop_18_api/slider_image";
    private static final String  URL_PRODUCTS = "https://macona.in/city_shop_18_api/show_all_product";
    private static final String  URL_PRODUCTS2 = "https://macona.in/city_shop_18_api/show_category";

    RecyclerView recyclerView2,recyclerView3;

    //a list to store all the products
    List<Product> productList2;
    List<Product> productList3;
    //the recyclerview
    int image_count=0;
    SharedPreferences sharedPreferences , user_pref,payment_pref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setSliderViews();
        sliderLayout = root.findViewById(R.id.imageSlider);
        recyclerView2 = root.findViewById(R.id.recylcerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView3 = root.findViewById(R.id.recylcerView3);
        recyclerView3.setHasFixedSize(true);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.THIN_WORM);

        list_dataList=new ArrayList<>();
        sliderLayout.setScrollTimeInSec(2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView2.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
       RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(manager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList2 = new ArrayList<>();
        loadProducts();
        productList3 = new ArrayList<>();
        loadProducts2();

        sharedPreferences = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        user_pref  = getActivity().getSharedPreferences("user_id",MODE_PRIVATE);
        payment_pref  = getActivity().getSharedPreferences("payment_shared",MODE_PRIVATE);
        //password1 = findViewById(R.id.password);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        user_id = user_pref.getString("user_id",user_mobile);
        package_type = payment_pref.getString("payment_shared",payment_type_try);

        return root;
    }
    /*Enable options menu in this fragment*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.main_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        if (id == R.id.item1) {
            //do your function here
            Toast.makeText(getActivity(), "notification", Toast.LENGTH_SHORT).show();
            //addNotification();
        }
        if (id == R.id.item2) {
            //do your function here
            Toast.makeText(getActivity(), "cart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), AddToCart.class);
            startActivity(intent);
        }
        if (id == R.id.item3) {
            //do your function here
            Toast.makeText(getActivity(), "vendor Login", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),package_type,Toast.LENGTH_LONG).show();
            if(sharedPreferences.getBoolean("logged",false))
            {
                Intent i = new Intent(getContext(), MainActivity2.class);
                startActivity(i);
            }
            else{
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
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


        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }
    private void setupdata(List<List_Data> list_dataList) {

        for (int i = 0; i <= image_count-1; i++) {
            final List_Data ld = list_dataList.get(i);
            SliderView sliderView = new SliderView(getActivity());
            sliderView.setImageUrl(ld.getImageurl());

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            final int finalI = i;
            sliderLayout.addSliderView(sliderView);

        }
    }
    //recycler view code starts..

    private void loadProducts() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                            ProductAdapter2 adapter = new ProductAdapter2(getActivity(), productList2);
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
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }
    //recycer view code ends..

    private void loadProducts2() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS2,
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
                                productList3.add(new Product(
                                        product.getInt("id"),
                                        product.getString("category"),
                                        product.getString("image"),
                                        product.getString("status")

                                ));
                            }

                            progressDialog.dismiss();
                            //creating adapter object and setting it to recyclerview
                            ProductAdapter3 adapter = new ProductAdapter3(getActivity(), productList3);
                            recyclerView3.setAdapter(adapter);
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
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }

}