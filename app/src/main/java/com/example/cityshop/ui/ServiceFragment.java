package com.example.cityshop.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cityshop.ConfigSubcategory;
import com.example.cityshop.CustomAdapterSsubcategory;
import com.example.cityshop.CustomAdapterSsubcategory2;
import com.example.cityshop.R;
import com.example.cityshop.itemModelSubcategory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static com.example.cityshop.Login.user_id;

public class ServiceFragment extends Fragment {

    //JSON Array
    private JSONArray result;

    ProgressDialog progressDialog,progressDialog2;
    ArrayList spinnerDataList;
    ArrayList worldList;
    //end..
    ArrayList<itemModelSubcategory> arrayList1;
    ListView listview1;
    ImageView imageView1;
    String image1,Getlabelcategory2;

    //search start..
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private SimpleCursorAdapter myAdapter;

    SearchView searchView;
    String JsonURL = "https://macona.in/city_shop_18_api/bussiness_profile_show?mobileno=";
    ImageView vendor_logo_image2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_service, container, false);

        final String subcategory_id = user_id;
        //Toast.makeText(getContext(),subcategory_id,Toast.LENGTH_LONG).show();
        arrayList1 = new ArrayList<itemModelSubcategory>();
        // imageView1 = root.findViewById(R.id.sub_date);
        listview1 = (ListView) root.findViewById(R.id.listview_fragment_service);

        vendor_logo_image2 = root.findViewById(R.id.vendor_logo_img3);

        // searchView = (SearchView) root.findViewById(R.id.search_action_mainitems);
        //getting data in spinner..
        getUserProfile(subcategory_id);
        getData(subcategory_id);
        progressDialog2 = ProgressDialog.show(getContext(),null,"Loading Product details please wait....",true,true);
        //progressDialog = ProgressDialog.show(Main_Items.this,null,"loading the products..",true,true);


        return root;
    }

    private void getData(String main_id) {

        progressDialog = ProgressDialog.show(getContext(),null,"loading the Data..",true,true);

        String value = main_id;

        if (value.equals("")) {
            Toast.makeText(getContext(), "Please Enter Data Value", Toast.LENGTH_LONG).show();
            return;
        }

        String url = ConfigSubcategory.DATA_URL2 + main_id.trim();



        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void showJSON(final String response) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(ConfigSubcategory.JSON_ARRAY1);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String image = jo.getString(ConfigSubcategory.KEY_image);
                String name = jo.getString(ConfigSubcategory.KEY_name2);
                String id1 = jo.getString(ConfigSubcategory.KEY_id);
                String original_price = jo.getString(ConfigSubcategory.KEY_original_price);
                String product_mrp = jo.getString(ConfigSubcategory.KEY_mrp);

                itemModelSubcategory model1 = new itemModelSubcategory();
                model1.setId(id1);
                model1.setImage(image);
                model1.setName(name);
                model1.setOriginal_price(original_price);
                model1.setInfo(product_mrp);
                arrayList1.add(model1);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.dismiss();
        CustomAdapterSsubcategory2 adapter = new CustomAdapterSsubcategory2(getContext(), arrayList1);
        listview1.setAdapter(adapter);

       /* ListAdapter adapter = new SimpleAdapter(
                sub_category.this, list, R.layout.activity_mylist,
                new String[]{ Config5.KEY_DATE,Config5.KEY_DATA, Config5.KEY_ID},
                new int[]{R.id.date,R.id.data, R.id.tvid});

        listview.setAdapter(adapter);*/

    }
    private void getUserProfile(String main_id) {

        String value = main_id;

        String url =JsonURL + main_id.trim();



        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON2(response);
                progressDialog2.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void showJSON2(final String response) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String imagepro = jo.getString("vendor_image");

                Picasso.with(getContext())
                        .load(imagepro)
                        .into(vendor_logo_image2);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
