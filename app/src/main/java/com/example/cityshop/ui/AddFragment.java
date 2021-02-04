package com.example.cityshop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cityshop.AddProduct;
import com.example.cityshop.AddServices;
import com.example.cityshop.FreeImage;
import com.example.cityshop.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.example.cityshop.Login.package_type;

public class AddFragment extends Fragment implements View.OnClickListener{

    CardView newproduct,newservice,viewproduct,viewservice,freeimagecard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        newproduct = root.findViewById(R.id.card_addproduct);
        newservice = root.findViewById(R.id.card_addservice);
        viewproduct = root.findViewById(R.id.card_viewproduct);
        viewservice = root.findViewById(R.id.card_viewservice);
        freeimagecard = root.findViewById(R.id.card_freeimage);

        newproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(package_type.equals("4250") || package_type.equals("5000")) {
                    Intent intent = new Intent(getContext(), AddProduct.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(),"You are not subscribe to this pack", Toast.LENGTH_SHORT).show();
                }
            }
        });
        newservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(package_type.equals("1999") || package_type.equals("5000")) {
                    Intent intent = new Intent(getContext(), AddServices.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(),"You are not subscribe to this pack", Toast.LENGTH_SHORT).show();
                }
            }
        });
        freeimagecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FreeImage.class);
                startActivity(intent);
            }
        });

        viewproduct.setOnClickListener(this);
        viewservice.setOnClickListener(this);

        return root;
    }
    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.card_viewproduct:
                fragment = new ProductFragment();
                replaceFragment(fragment);
                break;

            case R.id.card_viewservice:
                fragment = new ServiceFragment();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment2, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
