package com.xll.xinsheng.tools;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.xinsheng.R;
import com.xll.xinsheng.bean.LoginInfo;
import com.xll.xinsheng.bean.Session;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.ui.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils<T> {

    private static final String TAG = "HttpUtils";

    public static final String HOST_REMOTE = "http://39.98.167.156:8081/xslw";

    public static final String LOGIN = HOST_REMOTE + "/admin/login/doLogin";
    public static final String REIMBURSE = HOST_REMOTE + "/admin/objpayment/paymentCreateSave";
    public static final String INITIAL_DATA = HOST_REMOTE + "/admin/objpayment/getInitData";
    public static final String PENDING_ORDER = HOST_REMOTE + "/admin/objpayment/getDaiBanList";
    public static final String DONE_ORDER = HOST_REMOTE + "/admin/objproject/getYiBanList";
    public static final String DONE_LIST = HOST_REMOTE + "/admin/objproject/getYiBanList";
    public static final String INVOICE_TYPE = HOST_REMOTE + "/admin/objpayment/getkmxx";
    public static final String FILE_URL_HEADER = "http://39.98.167.156:8081/upload/";
    public static final String ORDER_EDIT = HOST_REMOTE + "/admin/objproject/getOrderEditPage";
    public static final String REIMBURSE_DEAL = HOST_REMOTE + "/admin/objpayment/paymentEditSave";
    public static final String PAY_LOAN = HOST_REMOTE + "/admin/objloan/loanEditSave";
    public static final String TP_PROCESS = HOST_REMOTE + "/admin/objspeapp/speAppEditSave";
    public static final String NOTICE = HOST_REMOTE + "/admin/objproject/getNotice";
    public static final String PAY_LOAN_REQUEST = HOST_REMOTE + "/admin/objloan/loanCreateSave";
    public static final String CHANNELID_UPDATE = HOST_REMOTE + "/admin/objsendnotice/channelUpdate";




    public interface XinResponseListener {
        void onResponse(String response);
    }


    public static void login(Context context, String url, final HashMap<String, String> requestParam, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (requestParam != null) {
                    return requestParam;
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Connection", "keep-alive");
                return map;
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    public static void post(final String url, final HashMap<String, String> requestParam, final XinResponseListener listener) {

        Log.i(TAG, "url:" + url);
        final HashMap<String, String> map = new HashMap<>();

        if (requestParam != null) {
            Log.d(TAG, url + ":request:" + requestParam.toString());
            try {
                for (Map.Entry<String, String> entry : requestParam.entrySet()) {
                    String value = entry.getValue();
                    String key = entry.getKey();
                    if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(key)) {
                        String t = URLEncoder.encode( value, "utf-8");
                        map.put(key, value);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        final Context context = MyApplication.getMyApplication();
        Cache<Session> cache = new Cache<>(context, Cache.LOGIN_INFO);
        final LoginInfo loginInfo = cache.getLoginInfo();
        if (loginInfo == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        Session session = loginInfo.getSession();
        final String id;

        if (session == null || TextUtils.isEmpty(id = session.getId() )) {
            Toast.makeText(MyApplication.getMyApplication(), "id is null, please login", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getMyApplication());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, url +":response:" + response);
                if (TextUtils.isEmpty(response)) {
                    Toast.makeText(context, R.string.request_null, Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    if (listener != null) {
                        listener.onResponse(response);
                    }
                } catch (JSONException e) {

                    if(!TextUtils.isEmpty(response) && response.contains("登录 | 鑫升劳务")) {
                        //当作session失效，跳转到登录界面
                        Toast.makeText(context, R.string.login_session_expired, Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    } else {
                        //系统的数据返回异常
                        Toast.makeText(context, R.string.login_session_expired, Toast.LENGTH_LONG).show();
                    }
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response", url +":error:" + error.getMessage()+ "|" + error.networkResponse);
                Toast.makeText(context, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                Log.i("HttpUtils", "sessionId:" + id);
                 map.put("Content-Type", "application/x-www-form-urlencoded");
                map.put("Cookie", "JSESSIONID=" + id);
                map.put("Accept","*/*");
                map.put("Connection", "keep-alive");
                return map;
            }
        };

        //4633
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }
}
