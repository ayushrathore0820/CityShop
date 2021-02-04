package com.example.cityshop.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cityshop.HttpParse;
import com.example.cityshop.MainActivity;
import com.example.cityshop.R;
import com.example.cityshop.itemModelSubcategory;
import com.example.cityshop.ui.dashboard.DashboardViewModel;
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

public class ProfileFragment extends Fragment {

    ViewGroup viewGroup;

    ArrayList<itemModelSubcategory> arrayList1;
    ListView listview1;
    ImageView imageView1;
    String image1;
    ProgressDialog progressDialog2;


    TextView shop_title,business_name,shop_name,mobile_number,shop_address,gst_number,shop_email,facebook,instagram,privacy_policy,about_shop,twitter;
    TextView shop_title_detail,business_name_detail,shop_name_detail,mobile_number_detail,shop_address_detail,gst_number_detail,shop_email_detail,facebook_detail,instagram_detail,privacy_policy_detail,about_shop_detail,twitter_detail;
    TextView title_dialog;
    EditText business_profile_edittxt;
    String shop_title_string;
    Boolean CheckEditText ;
    ImageView logo;
    Button logout;
    String shop_title_Holder,user_id_Holder,name_cheeck_holder;

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ProgressDialog progressDialog ;
    String HttpURL = "https://macona.in/city_shop_18_api/business_progile_dialog_data";

    private DashboardViewModel dashboardViewModel2;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel2 =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

         viewGroup = root.findViewById(android.R.id.content);

        getData();

        shop_title = root.findViewById(R.id.titleheadline);
        business_name = root.findViewById(R.id.txtbuss_name);
        shop_name = root.findViewById(R.id.username_title);
        mobile_number = root.findViewById(R.id.mobile_title);
        shop_address = root.findViewById(R.id.address_title);
        gst_number = root.findViewById(R.id.gst_title);
        shop_email = root.findViewById(R.id.email_title);
        facebook = root.findViewById(R.id.txtfackebook);
        instagram = root.findViewById(R.id.txtinsta);
        privacy_policy = root.findViewById(R.id.txtprivacy);
        about_shop = root.findViewById(R.id.txtabout);
        logo = root.findViewById(R.id.logo);
        twitter = root.findViewById(R.id.txttwit);
        logout= root.findViewById(R.id.logout);

        shop_title_detail = root.findViewById(R.id.txtaboutheadline);
        business_name_detail = root.findViewById(R.id.txtaboutbuss_name);
        shop_name_detail = root.findViewById(R.id.txt_username_subtitle);
        mobile_number_detail = root.findViewById(R.id.txt_mobile_subtitle);
        shop_address_detail = root.findViewById(R.id.txt_address_subtitle);
        gst_number_detail = root.findViewById(R.id.txt_gst_subtitle);
        shop_email_detail = root.findViewById(R.id.txt_email_subtitle);
        facebook_detail = root.findViewById(R.id.txtaboutsocial);
        instagram_detail = root.findViewById(R.id.txtaboutinsta);
        privacy_policy_detail = root.findViewById(R.id.txtaboutpriyacy);
        about_shop_detail = root.findViewById(R.id.txtaboutabout);
        twitter_detail = root.findViewById(R.id.txtabouttwit);

        mobile_number_detail.setText(user_id);

        shop_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = shop_title.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
                // Toast.makeText(BusinessProfile.this,shop_title_Holder, Toast.LENGTH_LONG).show();
                //shop_title_detail.setText(shop_title_Holder);
            }
        });
        business_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = business_name.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        shop_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = shop_name.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        shop_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = shop_address.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        gst_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = gst_number.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        shop_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = shop_email.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = facebook.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = instagram.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = twitter.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = privacy_policy.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        about_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop_title_string = about_shop.getText().toString();
                name_cheeck_holder = shop_title_string.trim();
                showCustomDialog();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences =getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.business_profile_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        Button apply = (Button) dialogView.findViewById (R.id.Apply);
        business_profile_edittxt = dialogView.findViewById(R.id.business_profile_edittext);
        title_dialog = (TextView) dialogView.findViewById (R.id.headtxt);
        title_dialog.setText(shop_title_string);

        apply.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent intent_ok = new Intent(AddProduct.this,AddProduct.class);
                //startActivity(intent_ok);
                //finish();
                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    CategoryAddFunction(user_id_Holder,shop_title_Holder,name_cheeck_holder);
                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(getContext(), "Fill the Field", Toast.LENGTH_LONG).show();
                }

            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void CheckEditTextIsEmptyOrNot(){

        user_id_Holder = user_id.toString();
        shop_title_Holder = business_profile_edittxt.getText().toString();
        // name_cheeck_holder = title_dialog.getText().toString();

        // otp_Holder = otp.getText().toString();


        if(TextUtils.isEmpty(shop_title_Holder) || TextUtils.isEmpty(user_id_Holder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }
    public void CategoryAddFunction( final String user_id_, final String category_,final String name_cheeck_ ){

        class CategoryAddFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),"Updating new Information",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(getContext(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("user_id",params[0]);

                hashMap.put("value",params[1]);

                hashMap.put("name_check",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        CategoryAddFunctionClass categoryAddFunctionClass = new CategoryAddFunctionClass();

        categoryAddFunctionClass.execute(user_id_,category_,name_cheeck_);
    }

    private void getData() {

        String url = "https://macona.in/city_shop_18_api/bussiness_profile_show?mobileno="+user_id;

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
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String name_ = jo.getString("name");
                String shopname_ = jo.getString("Shopname");
                String mobileno_ = jo.getString("mobileno");
                String email_ = jo.getString("email");
                String shopaddress_ = jo.getString("shopaddress");
                String gstno_ = jo.getString("gstno");
                String id_ = jo.getString("id");
                String shop_title_ = jo.getString("shop_title");
                String business_name_ = jo.getString("business_name");
                String facebook_link_ = jo.getString("facebook_link");
                String instagram_link_ = jo.getString("instagram_link");
                String privacy_policy_ = jo.getString("privacy_policy");
                String about_shop_ = jo.getString("about_shop");
                String twitter_link = jo.getString("twitter_link");
                String shop_logo= jo.getString("vendor_image");

                shop_name_detail.setText(shopname_);
                shop_title_detail.setText(shop_title_);
                business_name_detail.setText(business_name_);
                shop_address_detail.setText(shopaddress_);
                gst_number_detail.setText(gstno_);
                facebook_detail.setText(facebook_link_);
                instagram_detail.setText(instagram_link_);
                privacy_policy_detail.setText(privacy_policy_);
                about_shop_detail.setText(about_shop_);
                shop_email_detail.setText(email_);
                twitter_detail.setText(twitter_link);

                Picasso.with(getContext())
                        .load(shop_logo)
                        .into(logo);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}