package com.example.cityshop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ProductViewHolder2> {


    private Context mCtx;
    private List<Service> serviceList;

    public ServiceAdapter(Context mCtx, List<Service> serviceList) {
        this.mCtx = mCtx;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceAdapter.ProductViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.samplesubcategory, null);
        return new ServiceAdapter.ProductViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder2 holder, final int position) {
        final Service service = serviceList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(service.getImage())
                .into(holder.sub_date);

        holder.servicename_textview.setText(service.getTitle());
        holder.servicecategory_txtview.setText(service.getStatus());
        final String temp = service.getTitle();
        final String service_name = service.getStatus();
        final String mobile = service.getMobile();
        holder.whatsapp_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PackageManager packageManager = mCtx.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String message = "Thank You City Shop 18 I found such a good Service i am looking from so long\n"+"Service name  - "+service_name.toString()+
                "\nI visited Your service and I am Intrested to Appoint you for the same \n Please share some more details to me \n";

                try {
                    String url = "https://api.whatsapp.com/send?phone=91"+mobile.toString().trim()+"&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        mCtx.startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        holder.call_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number=mobile.toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_DENIED) {

                    // Requesting the permission
                    ActivityCompat.requestPermissions((Activity) mCtx,
                            new String[] { Manifest.permission.CALL_PHONE },1);
                }
                else {
                    mCtx.startActivity(callIntent);
                }

            }
        });
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
        return serviceList.size();
    }
    class ProductViewHolder2 extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        CardView call_service,whatsapp_service;
        TextView servicename_textview,servicecategory_txtview;
        ImageView sub_date;

        public ProductViewHolder2(View itemView) {
            super(itemView);

            servicename_textview = itemView.findViewById(R.id.sub_data);
            servicecategory_txtview = itemView.findViewById(R.id.page);
            sub_date = itemView.findViewById(R.id.sub_date);
            call_service = itemView.findViewById(R.id.call);
            whatsapp_service = itemView.findViewById(R.id.product_whatsapp_share);
            relativeLayout = itemView.findViewById(R.id.relativelayout2);
        }
    }
}
