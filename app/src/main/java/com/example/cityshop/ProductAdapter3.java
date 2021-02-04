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

public class ProductAdapter3 extends RecyclerView.Adapter<ProductAdapter3.ProductViewHolder3> {


    private Context mCtx;
    private List<Product> productList3;

    public ProductAdapter3(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList3 = productList;
    }

    @NonNull
    @Override
    public ProductAdapter3.ProductViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview3, null);
        return new ProductAdapter3.ProductViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder3 holder, int position) {
        final Product product = productList3.get(position);

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
                    Toast.makeText(mCtx,temp, Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(mCtx,CategoryProductService.class);
                    intent.putExtra("category_name", temp);
                    intent.putExtra("status", status);
                    mCtx.startActivity(intent);

                     */
                }
                //Toast.makeText(mCtx,product.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList3.size();
    }
    class ProductViewHolder3 extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView textViewTitle;
        ImageView imageView;

        public ProductViewHolder3(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle2);
            imageView = itemView.findViewById(R.id.imageView2);
            relativeLayout = itemView.findViewById(R.id.relativelayout2);
        }
    }
}
