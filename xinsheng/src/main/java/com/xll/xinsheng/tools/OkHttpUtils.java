package com.xll.xinsheng.tools;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xll.xinsheng.bean.LoginInfo;
import com.xll.xinsheng.bean.Session;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.ui.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    private static String TAG = "OkHttpUtils";

    private static OkHttpUtils okHttpUtils;
    private final OkHttpClient okHttpClient;

    public static final String UPLOAD_FILE_URL = "http://39.98.167.156:8081/xslw/admin/objproject/upFile";

    public static OkHttpUtils get() {
        if (okHttpUtils == null) {
            okHttpUtils = new OkHttpUtils();
        }

        return okHttpUtils;
    }

    public OkHttpUtils() {
        okHttpClient = new OkHttpClient();
    }

    /**
     *上传文件
     * @param actionUrl 接口地址
     * @param paramsMap 参数
     * @param callBack 回调
     * @param <T>
     */
    public <T>void upLoadFile(String actionUrl, HashMap<String, Object> paramsMap, final OnProcessListener callBack) {
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

        try {
            //补全请求地址
            MultipartBody.Builder builder = new MultipartBody.Builder();
            StringBuilder urlBuilder = new StringBuilder(actionUrl).append("?");
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    if (object != null) {
                        urlBuilder.append(key).append("=").append(object.toString());
                    }
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
                }
            }

            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(urlBuilder.toString()).post(body).addHeader("Cookie", "JSESSIONID=" + id).build();
            //单独设置参数 比如读取超时时间
            Log.e(TAG, request.toString());
            final Call call = okHttpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                    callBack.onFailed(null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        callBack.onSuccess(null, "success");
                    } else {
                        callBack.onFailed(null);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

   /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称
     * @param listener     下载监听
     */
    public void download(final String url, final String destFileDir, final String destFileName, final OnProcessListener listener) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                final Request request = new Request.Builder()
                        .url(url)
                        .build();
                Log.d(TAG, "url:" + url);

                OkHttpClient client = new OkHttpClient();

                try {
                    Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //异步请求
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 下载失败监听回调
                        listener.onFailed(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;

                        //储存下载文件的目录
                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        File file = new File(dir, destFileName);

                        try {
                            if (response.body() == null) {
                                listener.onFailed(null);
                                return;
                            }
                            is = response.body().byteStream();
                            long total = response.body().contentLength();
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {

                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                //下载中更新进度条
                                listener.onProcessing(progress);
                            }

                            fos.flush();
                            //下载完成
                            listener.onSuccess(file, null);
                        } catch (Exception e) {
                            listener.onFailed(e);
                        }finally {
                            try {
                                if (is != null) {
                                    is.close();
                                }
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {

                            }
                        }
                    }
                });
            }
        }.start();


    }



    public interface OnProcessListener {
        //下载成功之后的文件
        void onSuccess(File file, String str);
        //下载进度
        void onProcessing(int progress);
        //下载异常信息
        void onFailed(Exception e);
    }

}
