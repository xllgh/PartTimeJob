package com.xll.xinsheng.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.CompoTextviewBinding;
import com.example.xinsheng.databinding.CompoVerTextviewBinding;

public class CompoVerTextView extends ConstraintLayout {

    private CompoVerTextviewBinding binding;

    public CompoVerTextView(Context context) {
        super(context);
        initView(context);
    }

    public CompoVerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttr(context, attrs);
    }

    public CompoVerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttr(context, attrs);
    }

    private void initView(Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.compo_ver_textview, this, true);
    }

    public void setListener(OnClickListener listener) {
        binding.setClickListener(listener);
    }

    public void setTextValue(String str) {
        if (binding != null) {
            if (TextUtils.isEmpty(str)) {
                binding.setValue("");
            } else {
                binding.setValue(str);
            }
        }
    }

    private void initAttr(Context context, AttributeSet set) {
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.CompoTextView);
        String key = typedArray.getString(R.styleable.CompoTextView_textName);
        String value = typedArray.getString(R.styleable.CompoTextView_textValue);
        if (binding != null && !TextUtils.isEmpty(key)) {
            binding.setKey(key);
        }

        if (binding != null && !TextUtils.isEmpty(value)) {
            binding.setValue(value);
        }
        typedArray.recycle();
    }


}
