package com.xll.xinsheng.tools;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {

    private static final String TAG = "Utils";

    private static final String REIMBURSE = "bx-";

    public static final int REIMBURSEMENT = 1;
    public static final int PAY_LOAN = 2;
    public static final int TP = 3;
    public static final int UNKNOWN = -1;
    public static final String Initiator = "0";



    /** 魅族代理需要的魅族appid和appkey，请到魅族推送官网申请 **/
    private static final String mzAppId = "";
    private static final String mzAppKey = "";

    /** 小米代理需要的小米appid和appkey，请到小米推送官网申请 **/
    private static final String xmAppId = "";
    private static final String xmAppKey = "";

    /** OPPO代理需要的OPPO appkey和appsecret，请到OPPO推送官网申请 **/
    private static final String opAppKey = "";
    private static final String opAppSecret = "";


    // api_key 绑定
    public static void initBaiduPush(Context context) {
        // 开启华为代理，如需开启，请参考华为代理接入文档
        //！！应用需要已经在华为推送官网注册
        PushManager.enableHuaweiProxy(context, true);
        // 开启魅族代理，如需开启，请参考魅族代理接入文档
        //！！需要将mzAppId和mzAppKey修改为自己应用在魅族推送官网申请的APPID和APPKEY
        PushManager.enableMeizuProxy(context, false, mzAppId, mzAppKey);
        // 开启OPPO代理，如需开启，请参考OPPO代理接入文档
        //！！需要将opAppKey和opAppSecret修改为自己应用在OPPO推送官网申请的APPKEY和APPSECRET
        PushManager.enableOppoProxy(context, false, opAppKey, opAppSecret);
        // 开启小米代理，如需开启，请参考小米代理接入文档
        //！！需要将xmAppId和xmAppKey修改为自己应用在小米推送官网申请的APPID和APPKEY
        PushManager.enableXiaomiProxy(context, false, xmAppId, xmAppKey);
        // 开启VIVO代理，如需开启，请参考VIVO代理接入文档
        //！！需要将AndroidManifest.xml中com.vivo.push.api_key和com.vivo.push.app_id修改为自己应用在VIVO推送官网申请的APPKEY和APPID
        PushManager.enableVivoProxy(context, false);
        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
        //！！请将AndroidManifest.xml api_key 字段值修改为自己的 api_key 方可使用 ！！
        //！！ATTENTION：You need to modify the value of api_key to your own in AndroidManifest.xml to use this Demo !!
        PushManager.startWork(context,
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(context, "api_key"));
    }


    // 自定义一个注解MyState
    @IntDef({REIMBURSEMENT, PAY_LOAN, TP, UNKNOWN})
    public @interface ProcessType {
    }

    @ProcessType
    public static int getProcessType(String orderId) {

        if (!TextUtils.isEmpty(orderId)) {
            if (orderId.startsWith("bx")) {
                return REIMBURSEMENT;
            } else if (orderId.startsWith("tp")) {
                return TP;
            } else if (orderId.startsWith("jk")) {
                return PAY_LOAN;
            } else if (orderId.startsWith("zf")) {
                return PAY_LOAN;
            } else {
                return UNKNOWN;
            }
        } else {
            return UNKNOWN;
        }

    }


    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "error " + e.getMessage());
        }
        return apiKey;
    }

    /**
     * 36      * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
     **/


    public static String digitUppercase(double n) {
        String fraction[] = {"角", "分"};
        String digit[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

        String head = n < 0 ? "负" : "";
        n = Math.abs(n);

        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1) {
            s = "整";
        }
        int integerPart = (int) Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }


    public static boolean isReimburse(@NonNull String orderId) {
        return orderId.startsWith(REIMBURSE);
    }

    public static void applyPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
        }, 1);
    }

    @BindingAdapter({"app:imgUrl"})
    public static void getTransImageView(ImageView imageView, int res) {
        imageView.setImageResource(res);
    }

    @BindingAdapter({"app:textImgUrl"})
    public static void setTextViewBackground(TextView textView, int res) {
        //textView.setBackground(MyApplication.getMyApplication().getDrawable(res));
        textView.setBackgroundResource(res);
    }

    @BindingAdapter({"app:selected"})
    public static void setTextViewSelected(TextView textView, boolean value) {
        textView.setSelectAllOnFocus(true);
    }

    @BindingAdapter({"app:setOnItemSelectListener"})
    public static void setOnItemSelectListener(Spinner spinner, AdapterView.OnItemSelectedListener listener) {
        spinner.setOnItemSelectedListener(listener);
    }

    @BindingAdapter({"app:setOnCheckClickListener"})
    public static void setOnCheckClickListener(CheckBox checkBox, View.OnClickListener listener) {
        checkBox.setOnClickListener(listener);
    }

    public static String generateId(String prefix) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetime = sdf.format(date);
        return prefix + datetime.substring(0, 8) + "-" + datetime.substring(8);
    }

    public static String getProjectId() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetime = sdf.format(date);
        return "bx-" + datetime.substring(0, 8) + "-" + datetime.substring(8);
    }



    public static float parseToFloat(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        try {
          return  Float.parseFloat(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isFastClick(long lastTime, long currentTime, long interval ) {
        if (currentTime - lastTime <= interval) {
            return true;
        } else {
            return false;
        }
    }


    public static AlertDialog getDialog(@NonNull Context context, @StringRes int resId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(resId));
        builder.setCancelable(false);
        return builder.create();
    }
}
