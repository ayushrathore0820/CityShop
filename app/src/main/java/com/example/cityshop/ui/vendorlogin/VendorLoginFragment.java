package com.example.cityshop.ui.vendorlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cityshop.HttpParse;
import com.example.cityshop.PaymentVendor;
import com.example.cityshop.R;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class VendorLoginFragment extends Fragment {

    EditText name,shop_name,mobileno,email,address,shop_category,gstno,password;
    Button register_vendor;
    String email_Holder, mobileno_Holder, name_Holder, password_Holder,gstno_Holder,shop_name_Holder,shop_category_Holder,address_Holder;
    String HttpURL = "https://macona.in/city_shop_18_api/vendor_registration";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    private VendorLoginViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(VendorLoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_vendrologin, container, false);

        name = root.findViewById(R.id.name);
        shop_name = root.findViewById(R.id.shopname);
        mobileno = root.findViewById(R.id.mobileno);
        email = root.findViewById(R.id.email);
        address = root.findViewById(R.id.adress);
        shop_category = root.findViewById(R.id.shopcategory);
        gstno = root.findViewById(R.id.gst);
        password = root.findViewById(R.id.password);
        register_vendor = root.findViewById(R.id.register_vendor);

        register_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UserRegisterFunction(name_Holder,shop_name_Holder,mobileno_Holder,email_Holder,address_Holder,shop_category_Holder,gstno_Holder,password_Holder);

                }
                else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(getContext(), "Fill all the fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
    public void CheckEditTextIsEmptyOrNot(){

        email_Holder = email.getText().toString();
        mobileno_Holder = mobileno.getText().toString();
        name_Holder = name.getText().toString();
        password_Holder = password.getText().toString();
        shop_name_Holder = shop_name.getText().toString();
        gstno_Holder = gstno.getText().toString();
        shop_category_Holder = shop_category.getText().toString();
        address_Holder = address.getText().toString();
        // otp_Holder = otp.getText().toString();


        if(TextUtils.isEmpty(email_Holder) || TextUtils.isEmpty(mobileno_Holder) || TextUtils.isEmpty(name_Holder) || TextUtils.isEmpty(password_Holder) || TextUtils.isEmpty(shop_name_Holder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }

    public void UserRegisterFunction(final String email_ , final String mobileno_, final String name_, final String password_,final  String shop_name_,final  String gstno_,final  String shop_category_,final  String address_){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),"Registration in progress ",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(getContext(), httpResponseMsg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PaymentVendor.class);
                intent.putExtra("payment_mobile_number", mobileno_Holder);
                intent.putExtra("payment_email", email_Holder);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("name",params[0]);

                hashMap.put("shopname",params[1]);

                hashMap.put("mobileno",params[2]);

                hashMap.put("email",params[3]);

                hashMap.put("shopaddress",params[4]);

                hashMap.put("shopcategory",params[5]);

                hashMap.put("gstno",params[6]);

                hashMap.put("password",params[7]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(email_,mobileno_,name_,password_,shop_name_,gstno_,shop_category_,address_);
    }
}