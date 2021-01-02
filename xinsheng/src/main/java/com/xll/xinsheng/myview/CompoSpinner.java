package com.xll.xinsheng.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.CompoSpinnerBinding;
import com.xll.xinsheng.model.CompoSpinnerMode;

public class CompoSpinner extends ConstraintLayout {

    private static final String TAG = "CompoSpinner";
   private CompoSpinnerBinding binding;
    public CompoSpinner(Context context) {
        super(context);
        initView(context);

    }

    public CompoSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttr(context, attrs);

    }

    public CompoSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttr(context, attrs);

    }

    public int  getCurrentPosition() {
        if (mode != null) {
            return  mode.getCurPosition();
        }
        return 0;
    }

    public void setOnSelectItemListener(@NonNull  AdapterView.OnItemSelectedListener listener) {
        if (binding != null) {
            binding.setListener(listener);
           // binding.spinner.setOnItemSelectedListener(listener);
            Log.e(TAG, "binding is not null");
        }
        Log.e(TAG, "binding is null");
    }


    private void initView(Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.compo_spinner, this, true);
    }

    CompoSpinnerMode mode;

    String[] arrays;

    public  void setEntries(String[] entries) {
        this.arrays = entries;
        if (arrays != null) {
            mode.setEntries(arrays);
            binding.setModel(mode);
        }
    }



    private void initAttr(Context context, AttributeSet set) {
        TypedArray array = context.obtainStyledAttributes(set, R.styleable.CompoSpinner) ;
        String key = array.getString(R.styleable.CompoSpinner_title);
        setText(key);
        mode = new CompoSpinnerMode();
        array.recycle();
    }

    private void setText(String text) {
        if (binding != null && !TextUtils.isEmpty(text)) {
            binding.setTitle(text);
        }
    }
}
