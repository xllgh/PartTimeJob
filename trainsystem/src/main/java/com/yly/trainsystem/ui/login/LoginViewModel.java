package com.yly.trainsystem.ui.login;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.yly.trainsystem.data.LoginRepository;
import com.yly.trainsystem.data.Result;
import com.yly.trainsystem.data.model.LoggedInUser;
import com.yly.trainsystem.R;

public class LoginViewModel extends ViewModel {

    public ObservableField<String> userName = new ObservableField<>();

    public ObservableField<String> password = new ObservableField<>();

}
