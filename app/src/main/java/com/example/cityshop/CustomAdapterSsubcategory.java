package com.example.cityshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

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

import static android.app.Activity.RESULT_OK;
import static com.example.cityshop.Login.user_id;

public class CustomAdapterSsubcategory extends BaseAdapter {



    //String server_url = "https://mahendrakiranastore.in/Android/addtocart.php";
    String server_url1 = "https://macona.in/city_shop_18_api/product_delete";
    Context context1;
    ViewGroup viewGroup;
    ArrayList<itemModelSubcategory> arrayList1;
    TextView p_name,p_price,p_mrp,p_info;
    String user_id_holder,p_name_holder,p_price_holder,p_mrp_holder,p_info_holder,p_id;

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ProgressDialog progressDialog ;
    String HttpURL = "https://macona.in/city_shop_18_api/edit_vendor_product";


    public CustomAdapterSsubcategory(Context context1, ArrayList<itemModelSubcategory> arrayList1) {
        this.context1 = context1;
        this.arrayList1 = arrayList1;
    }

    @Override
    public int getCount() {
        return arrayList1.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList1.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView ==  null) {
            convertView = LayoutInflater.from(context1).inflate(R.layout.samplesubcategory2, parent, false);
        }


        final TextView name1,original_price,product_mrp;
        final Button inc,dec,add_to_cart;
        ImageView image1;
        final ImageView delete,product_whatsapp_share,addmoreimage;
        name1 = (TextView) convertView.findViewById(R.id.sub_data) ;
        original_price = (TextView) convertView.findViewById(R.id.page);
        product_mrp = (TextView) convertView.findViewById(R.id.mrp);
        image1 = (ImageView) convertView.findViewById(R.id.sub_date);
        delete=convertView.findViewById(R.id.delete);
        product_whatsapp_share = convertView.findViewById(R.id.product_whatsapp_share);
        addmoreimage = convertView.findViewById(R.id.addmore);
         viewGroup = convertView.findViewById(android.R.id.content);

        //setting into text..
        name1.setText(arrayList1.get(position).getName());
        original_price.setText(arrayList1.get(position).getOriginal_price());
        product_mrp.setText(arrayList1.get(position).getInfo());


        //getting id from database.on item click..
        final String mainproduct_id = Integer.toString(Integer.parseInt(arrayList1.get(position).getId()));



      /* add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context1,ViewProductDetaiils.class);
                intent.putExtra("id", mainproduct_id);
                context1.startActivity(intent);
                Toast.makeText(context1,mainproduct_id, Toast.LENGTH_SHORT).show();


            }
        });
*/

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(context1,ViewProductDetaiils.class);
                //intent.putExtra("id", mainproduct_id);
                //context1.startActivity(intent);
                Toast.makeText(context1,mainproduct_id, Toast.LENGTH_SHORT).show();
                p_id = mainproduct_id;
            }
        });

        Picasso.with(context1)
                .load(arrayList1.get(position).image)
                .into(image1);
        //Toast.makeText(context1,mainproduct_id,Toast.LENGTH_LONG).show();


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent2 = new Intent(context1, MainActivity2.class);
                                context1.startActivity(intent2);
                                Toast.makeText(context1,"Item Deleted ",Toast.LENGTH_LONG).show();
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context1,"Error..",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("id",mainproduct_id);
                        params.put("user_id",user_id);
                        return params;
                    }
                };
                MySingleton.getInstance(context1).addTorequestque(stringRequest);


            }
        });

        product_whatsapp_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
        addmoreimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context1,MoreImages.class);
                intent.putExtra("id", mainproduct_id);
                context1.startActivity(intent);
                //showCustomDialog2();
            }
        });

        return convertView;
    }

    private void showCustomDialog() {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context1).inflate(R.layout.editvendor_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context1);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        Button apply = (Button) dialogView.findViewById (R.id.edit_confirm);
        p_name = dialogView.findViewById(R.id.p_name);
        p_price = (TextView) dialogView.findViewById (R.id.p_price);
        p_mrp = (TextView) dialogView.findViewById (R.id.p_mrp);
        p_info = (TextView) dialogView.findViewById (R.id.p_info);

        //title_dialog.setText(shop_title_string);

        apply.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent intent_ok = new Intent(AddProduct.this,AddProduct.class);
                //startActivity(intent_ok);
                //finish();
                user_id_holder = user_id.toString();
                p_name_holder = p_name.getText().toString();
                p_price_holder = p_price.getText().toString()+" Rs";
                p_mrp_holder = p_mrp.getText().toString()+" Rs";
                p_info_holder = p_info.getText().toString();
                //Toast.makeText(context1,user_id_holder+p_name_holder+p_price_holder+p_mrp_holder+p_info_holder+p_id, Toast.LENGTH_LONG).show();
                    CategoryAddFunction(user_id_holder,p_name_holder,p_price_holder,p_mrp_holder,p_info_holder,p_id);
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void CategoryAddFunction( final String user_id_,final String p_name_,final String p_price_,final String p_mrp_,final String p_info_,final String p_id_){

        class CategoryAddFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(context1,"Updating new Information",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(context1,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("user_id",params[0]);

                hashMap.put("name",params[1]);

                hashMap.put("price",params[2]);

                hashMap.put("mrp",params[3]);

                hashMap.put("info",params[4]);

                hashMap.put("id",params[5]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        CategoryAddFunctionClass categoryAddFunctionClass = new CategoryAddFunctionClass();

        categoryAddFunctionClass.execute(user_id_,p_name_,p_price_,p_mrp_,p_info_,p_id_);
    }


}
