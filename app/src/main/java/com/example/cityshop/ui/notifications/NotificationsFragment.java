package com.example.cityshop.ui.notifications;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cityshop.AddToCart;
import com.example.cityshop.Login;
import com.example.cityshop.MainActivity2;
import com.example.cityshop.Product;
import com.example.cityshop.ProductAdapter4;
import com.example.cityshop.R;

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

public class NotificationsFragment extends Fragment {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    private NotificationsViewModel notificationsViewModel;
    SharedPreferences sharedPreferences , user_pref,payment_pref;

    private static final String  URL_PRODUCTS3 = "https://macona.in/city_shop_18_api/show_all_vendor";

    RecyclerView recyclerView_vendor;

    //a list to store all the products
    List<Product> productList4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView_vendor = root.findViewById(R.id.recylcerView_vendor);
        recyclerView_vendor.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView_vendor.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        productList4 = new ArrayList<>();
        loadProducts();

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
        }
        if (id == R.id.item2) {
            //do your function here
            Toast.makeText(getActivity(), "cart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), AddToCart.class);
            startActivity(intent);
        }
        if (id == R.id.item3) {
            //do your function here
            Toast.makeText(getActivity(), "vendor registration", Toast.LENGTH_SHORT).show();
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS3,
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
                                productList4.add(new Product(
                                        product.getInt("id"),
                                        product.getString("shop_name"),
                                        product.getString("image_url"),
                                        product.getString("user_id")

                                ));
                            }

                            progressDialog.dismiss();
                            //creating adapter object and setting it to recyclerview
                            ProductAdapter4 adapter = new ProductAdapter4(getActivity(), productList4);
                            recyclerView_vendor.setAdapter(adapter);
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