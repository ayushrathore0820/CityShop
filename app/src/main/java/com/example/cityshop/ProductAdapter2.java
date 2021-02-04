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

public class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.ProductViewHolder2> {


    private Context mCtx;
    private List<Product> productList2;

    public ProductAdapter2(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList2 = productList;
    }

    @NonNull
    @Override
    public ProductAdapter2.ProductViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview2, null);
        return new ProductAdapter2.ProductViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder2 holder, int position) {
        final Product product = productList2.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(product.getTitle());
        final String temp = product.getTitle();
        final String status = product.getStatus();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {
                    Toast.makeText(mCtx,status, Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(mCtx,CategoryProductService.class);
                    intent.putExtra("category_name", temp);
                    intent.putExtra("status", status);
                    mCtx.startActivity(intent);
                     */
                    Intent intent = new Intent(mCtx,ViewProductOfVendor.class);
                    // intent.putExtra("category_name", temp);
                    intent.putExtra("mobile", status);
                    mCtx.startActivity(intent);
                }
                //Toast.makeText(mCtx,product.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList2.size();
    }
    class ProductViewHolder2 extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView textViewTitle;
        ImageView imageView;

        public ProductViewHolder2(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle2);
            imageView = itemView.findViewById(R.id.imageView2);
            relativeLayout = itemView.findViewById(R.id.relativelayout2);
        }
    }
}
