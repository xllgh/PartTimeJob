package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityLoginBinding;
import com.xll.xinsheng.bean.LoginInfo;
import com.xll.xinsheng.bean.Session;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.LoginModel;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends XinActivity {

    private final LoginModel model = new LoginModel();

    private static final String TAG = "LoginActivity";

    private Cache generalCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ActivityLoginBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_login);
       generalCache = new Cache(this, Cache.GENERAL_INFO);
        model.setCheck(generalCache.getBoolean(Cache.KEY_SAVE_LOGIN));
        if (generalCache.getBoolean(Cache.KEY_SAVE_LOGIN)) {
           String strUsername = generalCache.getString(Cache.KEY_USERNAME);
           if (strUsername != null) {
               model.setUserName(strUsername);
           }

           String strPassword = generalCache.getString(Cache.KEY_PASSWORD);
            if (strPassword != null) {
                model.setPassword(strPassword);
            }
        }
        binding.setModel(model);
        binding.setListener(loginListener);
        binding.setCheckListener(checkListener);
    }

    View.OnClickListener checkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            generalCache.saveGeneralInfo(Cache.KEY_SAVE_LOGIN,  model.getCheck());
        }
    };

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                HashMap<String, String> param = new HashMap<>();

                String userName = model.getUserName();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, R.string.username_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                String password = model.getPassword();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, R.string.password_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                model.setEnable(false);
                model.setLoading(true);
                final String url = HttpUtils.LOGIN + "?username=" + userName + "&password="+password+"&captcha=APP";
                HttpUtils.login(LoginActivity.this, url , param, listener, errorListener);

        }
    };

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                Cache<Session> cache = new Cache<>(LoginActivity.this, Cache.LOGIN_INFO);
                JSONObject jsonObject = new JSONObject(response);
                cache.saveInfo(jsonObject.toString());
                final LoginInfo loginInfo = cache.getLoginInfo();
                Session session = loginInfo.getSession();
                String id = null;
                if (session != null) {
                    id = session.getId();
                }

                if (!TextUtils.isEmpty(id)) {
                    Log.i(TAG, "session:" + id);
                    if (model.getCheck()) {
                        generalCache.saveGeneralInfo(Cache.KEY_USERNAME, model.getUserName());
                        generalCache.saveGeneralInfo(Cache.KEY_PASSWORD, model.getPassword());
                    }
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_LONG).show();
                    MyApplication.initialData();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this , MainActivity.class));
                    LoginActivity.this.finish();
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();

        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            model.setEnable(true);
            model.setLoading(false);
            Toast.makeText(LoginActivity.this, R.string.net_error, Toast.LENGTH_LONG).show();

        }
    };
}
