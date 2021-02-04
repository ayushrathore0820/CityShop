package com.example.cityshop.ui.vendorlogin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VendorLoginViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VendorLoginViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }
}