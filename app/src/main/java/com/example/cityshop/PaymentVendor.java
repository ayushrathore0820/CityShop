package com.example.cityshop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityshop.ui.vendorlogin.VendorLoginFragment;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentVendor extends AppCompatActivity implements PaymentResultListener {

    private Button startpayment;
    private TextView orderamount;
    RadioButton radio_product,radio_service,radio_combo;
    EditText ba_edittxt,cuponcode;
    Button applycode;
    private String TAG =" main";
    String paymet_mobile,payment_email;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;
    String finalResult, payment_amount,cuponcode_holder;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://macona.in/city_shop_18_api/transaction";
    String HttpURL2 = "https://macona.in/city_shop_18_api/apply_cupon";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_vendor);


        paymet_mobile = getIntent().getStringExtra("payment_mobile_number");
        payment_email = getIntent().getStringExtra("payment_email");


        startpayment = (Button) findViewById(R.id.startpayment);
        orderamount = (TextView) findViewById(R.id.orderamount);
         radioGroup = (RadioGroup) findViewById(R.id.radio_group1);
         ba_edittxt = findViewById(R.id.ba);
        cuponcode = findViewById(R.id.cuponcode);
        applycode = findViewById(R.id.codeapply);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(PaymentVendor.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    orderamount.setText(rb.getText());
                    payment_amount = rb.getText().toString();
                }

            }
        });
        startpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startPayment();
            }
        });
        applycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuponcode_holder = cuponcode.getText().toString().trim();
                //Toast.makeText(PaymentVendor.this, cuponcode_holder, Toast.LENGTH_SHORT).show();
                Applycode(cuponcode_holder,paymet_mobile.toString().trim(),payment_email.toString().trim());
            }
        });
    }
    public void onClear(View v) {
        /* Clears all selected radio buttons to default */
        radioGroup.clearCheck();
    }

    public void onSubmit(View v) {
        RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        Toast.makeText(PaymentVendor.this, rb.getText(), Toast.LENGTH_SHORT).show();
    }

    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "City Shop 18");
            options.put("description", "Vendor Subscription Pay");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = orderamount.getText().toString();
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", payment_email);
            preFill.put("contact", 91+paymet_mobile);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        // payment successfull pay_DGU19rDsInjcF2
        Log.e(TAG, " payment successfull "+ s.toString());
        Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
        UserRegister(payment_email, paymet_mobile, payment_amount.toString(),payment_amount.toString(),s.toString(),ba_edittxt.getText().toString().trim());
        /*Intent intent = new Intent(PaymentVendor.this,Login.class);
        startActivity(intent);
        finish();
         */
    }
    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG,  "error code "+String.valueOf(i)+" -- Payment failed ".toString()  );
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
    public void UserRegister( final String email_ ,final String userid_, final String packtype_ ,final String amount_,final String trans_id_,final String ba_txt){

        class UserRegisterClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(PaymentVendor.this,"Making Payment wait ",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(PaymentVendor.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PaymentVendor.this, Login.class);
                startActivity(intent);
                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("user_id",params[1]);

                hashMap.put("package_type",params[2]);

                hashMap.put("amount",params[3]);

                hashMap.put("trans_id",params[4]);

                hashMap.put("ba_code",params[5]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterClass userRegisterClass = new UserRegisterClass();

        userRegisterClass.execute(email_,userid_,packtype_,amount_,trans_id_,ba_txt);
    }


    public void Applycode(final String code_,final String user_id__,final String email__){

        class UserRegisterFunctionClass2 extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(PaymentVendor.this,"Registration in progress ",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(PaymentVendor.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PaymentVendor.this, Login.class);
                startActivity(intent);
                finish();
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("code",params[0]);

                hashMap.put("user_id",params[1]);

                hashMap.put("email",params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL2);

                return finalResult;
            }
        }

        UserRegisterFunctionClass2 userRegisterFunctionClass2 = new UserRegisterFunctionClass2();

        userRegisterFunctionClass2.execute(code_,user_id__,email__);
    }
}