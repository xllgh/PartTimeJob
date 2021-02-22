package com.xll.xinsheng.tools;

import android.Manifest;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static final String REIMBURSE = "bx-";

    public static final int REIMBURSEMENT = 1;
    public static final int PAY_LOAN = 2;
    public static final int TP = 3;
    public static final int UNKNOWN = -1;

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
}
