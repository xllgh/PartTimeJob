package com.yly.trainsystem.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yly.trainsystem.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils<T> {
    public static final String HOST_REMOTE = "47.96.228.89:8080";

    public static final String QUERY_TICKET = "http://"+HOST_REMOTE + "/queryTicket";
    public static final String QUERY_USER_TICKET = "http://"+HOST_REMOTE + "/queryUserOrder";
    public static final String SUBMIT_ORDER ="http://"+HOST_REMOTE + "/purchaseTicket";
    public static final String LOGIN_REGISTER = "http://"+HOST_REMOTE + "/loginRegister";
    public static final String REFUND_TICKET = "http://"+HOST_REMOTE + "/refundTicket";
    public static final String CHANGE_TICKET = "http://"+HOST_REMOTE + "/changeTicket";


    public static final int LOGIN_SUCCESS = 1;

    public static final int LOGIN_FAILED = 2;

    public static final int REGISTER_SUCCESS = 3;

    public static final int REGISTER_FAILED = 4;


    public static void post(String url, final HashMap<String, String> requestParam,
                            Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getMyApplication());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParam;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/x-www-form-urlencoded");
                return map;
            }
        };
        queue.add(stringRequest);
    }

    public static void post(String url, final HashMap<String, String> requestParam) {

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getMyApplication());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("response", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response", "error" + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestParam;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/x-www-form-urlencoded");
                return map;
            }
        };
        queue.add(stringRequest);
    }
}
