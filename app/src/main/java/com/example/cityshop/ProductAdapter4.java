package com.example.cityshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter4 extends RecyclerView.Adapter<ProductAdapter4.ProductViewHolder2> {


    private Context mCtx;
    private List<Product> productList4;

    public ProductAdapter4(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList4 = productList;
    }

    @NonNull
    @Override
    public ProductAdapter4.ProductViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_vendor, null);
        return new ProductAdapter4.ProductViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder2 holder, int position) {
        final Product product = productList4.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(product.getTitle());
        final String temp = product.getTitle();
        final String mobile_vendor = product.getStatus();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {
                    Toast.makeText(mCtx,mobile_vendor, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mCtx,ViewProductOfVendor.class);
                   // intent.putExtra("category_name", temp);
                    intent.putExtra("mobile", mobile_vendor);
                    mCtx.startActivity(intent);
                }
                //Toast.makeText(mCtx,product.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList4.size();
    }
    class ProductViewHolder2 extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView textViewTitle;
        ImageView imageView;

        public ProductViewHolder2(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.shopname);
            imageView = itemView.findViewById(R.id.vendor_image);
        }
    }
}
