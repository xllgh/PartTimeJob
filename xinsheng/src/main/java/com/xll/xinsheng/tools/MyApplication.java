package com.xll.xinsheng.tools;

import android.app.Application;
import android.util.Log;

import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.cache.Cache;

import org.json.JSONException;
import org.json.JSONObject;

public class MyApplication extends Application {

    private static MyApplication myApplication;
    private static final String TAG = "MyApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        myApplication = this;
        initialData();
    }

    public static void initialData() {
        final Cache cache = new Cache(MyApplication.getMyApplication(), Cache.INITIAL_DATA);
        InitialData data = cache.getInitialData();
        String oldUserName = null;
        if (data != null) {
            oldUserName = data.getUsername();
        }

        Cache generalCache = new Cache(MyApplication.getMyApplication(), Cache.GENERAL_INFO);
        String curUserName = generalCache.getString(Cache.KEY_USERNAME);

        if (cache.isExpired(10 * 60 * 1000, System.currentTimeMillis()) ||
                oldUserName == null || !oldUserName.equals(curUserName)) {
            Log.i(TAG, "initialData cache expired, reload");
            HttpUtils.post(HttpUtils.INITIAL_DATA, null, new HttpUtils.XinResponseListener() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        cache.saveInfo(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String response) {

                }
            });
        }
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }
}
