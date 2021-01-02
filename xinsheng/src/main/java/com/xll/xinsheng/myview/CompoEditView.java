package com.xll.xinsheng.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.LayoutEditBinding;

public class CompoEditView extends ConstraintLayout {

    LayoutEditBinding binding;

    public CompoEditView(Context context) {
        super(context);
        initView(context);
    }

    public CompoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttr(context, attrs);
    }

    public CompoEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttr(context, attrs);
    }
    public String getEditValue() {
        if (binding != null) {
            return binding.getValue();
        }
        return null;
    }

    public void setEditValue(String value) {
        if (binding != null && !TextUtils.isEmpty(value)) {
            binding.setValue(value);
        }
    }

    private void initView(Context context) {
      binding =  DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_edit, this, true );
    }

    public interface OnEditChangeListener{
        void onEditChange(String str);
    }

    public void setOnEditChangeListener(final OnEditChangeListener listener) {
        if (binding != null && listener != null) {
            binding.edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        listener.onEditChange(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private void initAttr(Context context, AttributeSet set) {
        TypedArray array = context.obtainStyledAttributes(set, R.styleable.CompoEditView);
        String prompt = array.getString(R.styleable.CompoEditView_editPrompt);
        if (binding != null && !TextUtils.isEmpty(prompt)) {
            binding.setKey(prompt);
        }

        int line = array.getInt(R.styleable.CompoEditView_editLine, 0);
        if (binding != null && line != 0) {
            binding.edit.setLines(line);
        }

        int background = array.getResourceId(R.styleable.CompoEditView_editBackground, -1);
        if(binding != null && background != -1) {
            binding.edit.setBackgroundResource(background);
        }

        int inputType = array.getInteger(R.styleable.CompoEditView_android_inputType, -1);
        if(binding != null && inputType != -1) {
            binding.edit.setInputType(inputType);
        }



        array.recycle();
    }
}
