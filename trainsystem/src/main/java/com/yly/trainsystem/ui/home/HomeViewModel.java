package com.yly.trainsystem.ui.home;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public final ObservableField<String> destination = new ObservableField<>();

    public final ObservableField<String>  departure = new ObservableField<>();

    public final ObservableField<String>  departureTime = new ObservableField<>();

    public final ObservableField<String> oldOrderId = new ObservableField<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }


    public LiveData<String> getText() {
        return mText;
    }
}