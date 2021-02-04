package com.example.cityshop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.URLEncoder;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.media.CamcorderProfile.get;
import static com.example.cityshop.ViewProductOfVendor.CHECK_ADDTOCART;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ProductViewHolder2> {


    public static String CHECK_CART;
    String ProuctNameHolder, PriceHolder,MrpHolder,QuantityHolder,ImageHolder, SQLiteDataBaseQueryHolder,imagestring;
    SQLiteDatabase sqLiteDatabaseObj;

    private Context mCtx;
    private List<Service> vendorList;
    String string_final_total;
    int final_total;
   // public TextView detail_count;

    public VendorAdapter(Context mCtx, List<Service> vendorList) {
        this.mCtx = mCtx;
        this.vendorList = vendorList;
    }

    @NonNull
    @Override
    public VendorAdapter.ProductViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardviewvendor, null);
        return new VendorAdapter.ProductViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder2 holder, final int position) {
        final Service service = vendorList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(service.getImage())
                .into(holder.product_img);

        holder.productname_textview.setText(service.getTitle());
        holder.price_txtview.setText(service.getStatus());
        holder.mrp_txtview.setText(service.getMobile());
        final String image = service.getImage().toString().trim();
        final String temp = service.getTitle();
        final int product_id = service.getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {
                    String id = String.valueOf(product_id);
                    Intent intent_ = new Intent(mCtx,Search2.class);
                    intent_.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // intent.putExtra("category_name", temp);
                    intent_.putExtra("id", id);
                   mCtx.startActivity(intent_);
                }
                //Toast.makeText(mCtx,product.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.detail_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.count>=1){
                    holder.count++;
                    holder.detail_count.setText(String.valueOf(holder.count));
                    /*product_mrp.setText(String.valueOf(res1));
                    product_ourmrp.setText(String.valueOf(res2));
                    product_saving.setText(String.valueOf(res3));
                     */
                }

            }
        });

        holder.detail_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.count>1){
                    holder.count--;
                    holder.detail_count.setText(String.valueOf(holder.count));
                    /*product_mrp.setText(String.valueOf(res1));
                    product_ourmrp.setText(String.valueOf(res2));
                    product_saving.setText(String.valueOf(res3));*/
                }
                else
                    Toast.makeText(mCtx,"Item can not be Zero",Toast.LENGTH_LONG).show();
            }
        });

        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProuctNameHolder = holder.productname_textview.getText().toString().trim();
                PriceHolder = holder.price_txtview.getText().toString().trim();
                MrpHolder = holder.mrp_txtview.getText().toString().trim();
                ImageHolder = image;
                QuantityHolder = holder.detail_count.getText().toString().trim();
                int qunat_count = Integer.parseInt(QuantityHolder.replaceAll("[^0-9?!\\.]",""));
                int price_count = Integer.parseInt(PriceHolder.replaceAll("[^0-9?!\\.]",""));

                final_total = qunat_count*price_count;
                string_final_total = String.valueOf(final_total);
               // Toast.makeText(mCtx, string_final_total, Toast.LENGTH_SHORT).show();

                //SQLiteDatabase db = AddToCartTable.getWritableDatabase();

                sqLiteDatabaseObj = mCtx.openOrCreateDatabase("AndroidJSonDataBase3", Context.MODE_PRIVATE, null);
                long NoOfRows = DatabaseUtils.queryNumEntries(sqLiteDatabaseObj,"AddToCartTable");

               // Toast.makeText(mCtx,icount,Toast.LENGTH_LONG).show();

                if(CHECK_CART==null || CHECK_CART==CHECK_ADDTOCART || NoOfRows==0) {

                    CHECK_CART = CHECK_ADDTOCART;

                    SQLiteDataBaseBuild();

                    SQLiteTableBuild();

                    InsertDataIntoSQLiteDatabase();
                }
                else {
                    Toast.makeText(mCtx,"Can not Add more than one vendor products in cart",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorList.size();
    }
    class ProductViewHolder2 extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        CardView call_service,whatsapp_service;
        public Button detail_decrease , detail_increase , addtocart;
        TextView productname_textview,price_txtview,mrp_txtview,detail_count;
        ImageView product_img;
        public int count=1;

        public ProductViewHolder2(View itemView) {
            super(itemView);

            detail_decrease = itemView.findViewById(R.id.detail_decrease);
            detail_increase = itemView.findViewById(R.id.detail_increase);
            detail_count = itemView.findViewById(R.id.detail_count);
            addtocart = itemView.findViewById(R.id.addtocart);
            price_txtview = itemView.findViewById(R.id.price_vendor);
            mrp_txtview = itemView.findViewById(R.id.mrp_vendor);
            productname_textview = itemView.findViewById(R.id.shopname2);
            product_img = itemView.findViewById(R.id.product_vendor_image);
            call_service = itemView.findViewById(R.id.call);
            whatsapp_service = itemView.findViewById(R.id.product_whatsapp_share);
            relativeLayout = itemView.findViewById(R.id.relativelayout2);
        }
    }

    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = mCtx.openOrCreateDatabase("AndroidJSonDataBase3", Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild(){

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS AddToCartTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, productname VARCHAR, price VARCHAR,mrp VARCHAR,img VARCHAR,quntity VARCHAR,total VARCHAR);");

    }
    public void InsertDataIntoSQLiteDatabase(){

        SQLiteDataBaseQueryHolder = "INSERT INTO AddToCartTable (productname,price,mrp,img,quntity,total) VALUES('"+ProuctNameHolder+"', '"+PriceHolder+"','"+MrpHolder+"','"+ImageHolder+"','"+QuantityHolder+"','"+final_total+"');";

            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            Toast.makeText(mCtx,"Data Inserted Successfully ", Toast.LENGTH_LONG).show();
    }
}
