package com.example.cityshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText username,login_password;
    Button login;
    TextView signuptxt;

    String PasswordHolder, EmailHolder;
    String finalResult;
    String HttpURL = "https://macona.in/city_shop_18_api/login";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail = "";
    public static String user_id,package_type,user_mobile,payment_type_try;
    SharedPreferences sp,sp1,sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        signuptxt = findViewById(R.id.txtsignup);
        login = findViewById(R.id.login);

        sp = getSharedPreferences("login",MODE_PRIVATE);
        sp1 = getSharedPreferences("user_id",MODE_PRIVATE);
        sp2 = getSharedPreferences("payment_shared",MODE_PRIVATE);


        signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Login.this,Login.class);
                //startActivity(intent);
                //finish();
                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {

                    UserLoginFunction(EmailHolder, PasswordHolder);

                } else {

                    Toast.makeText(Login.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    public void CheckEditTextIsEmptyOrNot(){

        EmailHolder = username.getText().toString();
        PasswordHolder = login_password.getText().toString();

        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    public void UserLoginFunction(final String email, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Login.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("1999")||httpResponseMsg.equalsIgnoreCase("4250")||httpResponseMsg.equalsIgnoreCase("5000")){

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra(UserEmail,email);
                    //user_id = email;
                    user_mobile = email;
                    payment_type_try = httpResponseMsg;

                    sp.edit().putBoolean("logged",true).apply();
                    sp1.edit().putString("user_id",user_mobile).apply();
                    sp2.edit().putString("payment_shared",payment_type_try).apply();
                    //user_id = sp1.getString("user_id",user_mobile);

                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),package_type, Toast.LENGTH_SHORT).show();
                    finish();

                }
                else if(httpResponseMsg.equalsIgnoreCase("Customer Login Scucessfull")){

                    Intent intent = new Intent(Login.this, MainActivity2.class);
                    intent.putExtra(UserEmail,email);
                    //user_id = email;
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),user_id, Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{

                    Toast.makeText(Login.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("mobileno",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(email,password);
    }
}