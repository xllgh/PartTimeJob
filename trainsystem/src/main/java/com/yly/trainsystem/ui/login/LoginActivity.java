package com.yly.trainsystem.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yly.trainsystem.R;
import com.yly.trainsystem.SharedUtils;
import com.yly.trainsystem.databinding.ActivityLoginBinding;
import com.yly.trainsystem.volley.HttpUtils;
import com.yly.trainsystem.volley.ServerUserInfoResponse;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    //调用接口成功的回调事件监听
    private final Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            binding.setProcess(false);
            Gson gson = new Gson();
           ServerUserInfoResponse serverUserInfoResponse = gson.fromJson(response, ServerUserInfoResponse.class);
           int code = serverUserInfoResponse.getCode();
           UserInfo userInfo =  serverUserInfoResponse.getData();

           if (code == HttpUtils.LOGIN_FAILED) {
               //登录失败
               Toast.makeText(LoginActivity.this,  R.string.login_failed , Toast.LENGTH_LONG).show();
           } else if (code == HttpUtils.LOGIN_SUCCESS) {
               //登录成功
               SharedUtils.getInstance(LoginActivity.this).putInt("userId", userInfo.getUserId());
               SharedUtils.getInstance(LoginActivity.this).putString("userName", userInfo.getUserName());
               Toast.makeText(LoginActivity.this,  R.string.login_success , Toast.LENGTH_LONG).show();
           } else if (code == HttpUtils.REGISTER_SUCCESS) {
               //注册成功
               SharedUtils.getInstance(LoginActivity.this).putInt("userId", userInfo.getUserId());
               SharedUtils.getInstance(LoginActivity.this).putString("userName", userInfo.getUserName());
               Toast.makeText(LoginActivity.this,  R.string.register_success , Toast.LENGTH_LONG).show();
           } else {
               //注册失败
               Toast.makeText(LoginActivity.this,  R.string.register_failed , Toast.LENGTH_LONG).show();
           }
           LoginActivity.this.setResult(1);
           LoginActivity.this.finish();

        }
    };

    //调用接口失败的回调事件监听
    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            binding.setProcess(false);
            Toast.makeText(LoginActivity.this,  R.string.net_error , Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LoginViewModel loginViewModel = new LoginViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLogin(loginViewModel);
        //注册用户登录的事件监听
        binding.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的用户名
                String userName = loginViewModel.userName.get();

                //展示进度条
                binding.setProcess(true);
                //用户名不能为空
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, R.string.username_empty, Toast.LENGTH_LONG).show();
                    binding.setProcess(false);
                    return;
                }

                //密码不能为空
                String password = loginViewModel.password.get();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, R.string.password_empty, Toast.LENGTH_LONG).show();
                    binding.setProcess(false);
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("userName", userName);
                map.put("password", password);
                //调用登录-注册接口，当用户没有注册时即注册，注册后就登录，listener为调用接口成功的回调，errorListener为调用接口异常的回调
                HttpUtils.post(HttpUtils.LOGIN_REGISTER, map, listener, errorListener);


            }
        });

    }
}
