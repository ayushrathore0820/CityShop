package com.example.cityshop;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    SQLiteDatabase sqLiteDatabaseObj;

    Context context;
    ArrayList<String> ID;
    ArrayList<String> Name;
    ArrayList<String> Price;
    ArrayList<String> Mrp;
    ArrayList<String> Img;
    ArrayList<String> Qnt;


    public ListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> phone,
            ArrayList<String> mrp,
            ArrayList<String> img,
            ArrayList<String> qnt
    )
    {

        this.context = context2;
        this.ID = id;
        this.Name = name;
        this.Price = phone;
        this.Mrp = mrp;
        this.Img = img;
        this.Qnt = qnt;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.item, null);

            holder = new Holder();

            //holder.ID_TextView = (TextView) child.findViewById(R.id.textViewID);
            holder.Name_TextView = (TextView) child.findViewById(R.id.textViewNAME);
            holder.PhoneNumberTextView = (TextView) child.findViewById(R.id.textViewPHONE_NUMBER);
            holder.MrpTextView = (TextView) child.findViewById(R.id.textViewMrp);
            holder.ImgTextView = (ImageView) child.findViewById(R.id.textViewImg);
            holder.QuntTextView = (TextView) child.findViewById(R.id.textViewQnt);
            holder.Delete = (ImageView) child.findViewById(R.id.cart_item_delete);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }

        Glide.with(context)
                .load(Img.get(position))
                .into(holder.ImgTextView);
        //holder.ID_TextView.setText(ID.get(position));
        holder.Name_TextView.setText(Name.get(position));
        holder.PhoneNumberTextView.setText(Price.get(position));
        holder.MrpTextView.setText(Mrp.get(position));
        //holder.ImgTextView.setText(Img.get(position));
        holder.QuntTextView.setText(Qnt.get(position));
        final String id = ID.get(position).toString().trim();

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabaseObj = context.openOrCreateDatabase("AndroidJSonDataBase3", Context.MODE_PRIVATE, null);
                sqLiteDatabaseObj.delete("AddToCartTable", "id" + "=" + id, null);
                Toast.makeText(context,"Item deleted Successfully", Toast.LENGTH_LONG).show();

            }
        });

        return child;
    }

    public class Holder {

        TextView ID_TextView;
        TextView Name_TextView;
        TextView PhoneNumberTextView;
        TextView MrpTextView;
        ImageView ImgTextView;
        TextView QuntTextView;
        ImageView Delete;
    }

}
