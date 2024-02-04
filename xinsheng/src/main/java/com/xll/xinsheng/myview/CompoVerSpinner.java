package com.xll.xinsheng.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.AdapterViewBindingAdapter.OnItemSelected;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.CompoSpinnerBinding;
import com.example.xinsheng.databinding.CompoVerSpinnerBinding;
import com.xll.xinsheng.model.CompoSpinnerMode;

public class CompoVerSpinner extends ConstraintLayout {

    private static final String TAG = "CompoVerSpinner";

    private static final int Vertical = 0;
    private static final int Horizontal = 1;
    private CompoVerSpinnerBinding verticalBinding;
    private CompoSpinnerBinding horizontalBinding;
    private final CompoSpinnerMode mode = new CompoSpinnerMode();;
    private String[] arrays;
    private int orientation;

    public CompoVerSpinner(Context context) {
        super(context);
        initView(context);

    }

    public CompoVerSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initView(context);
    }

    public CompoVerSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView(context);
    }

    public int getCurrentPosition() {
        if (mode != null && verticalBinding != null) {
            return mode.getCurPosition();
        }
        return 0;
    }


    public int setCurrentPosition(int position) {
        if (mode != null) {
            mode.setCurPosition(position);
            return position;
        }
        return -1;
    }


    public void setOnItemSelectListener(OnItemSelected selectListener) {
        if (Vertical == orientation) {
            if (verticalBinding != null) {
                verticalBinding.setItemSelected(selectListener);
            }
        } else {
            if (horizontalBinding != null) {
                horizontalBinding.setItemSelected(selectListener);
            }
        }

    }

    private void initView(Context context) {

        if (Horizontal == orientation) {
            horizontalBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.compo_spinner, this, true);
            horizontalBinding.setModel(mode);
        } else {
            verticalBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.compo_ver_spinner, this, true);
            verticalBinding.setModel(mode);
        }


    }


    public void setEntries(String[] entries) {
        this.arrays = entries;
        if (arrays != null) {
            mode.setEntries(arrays);
            //binding.setModel(mode);
        }
    }


    private void initAttr(Context context, AttributeSet set) {
        TypedArray array = context.obtainStyledAttributes(set, R.styleable.CompoSpinner);
        String key = array.getString(R.styleable.CompoSpinner_title);
        orientation = array.getInt(R.styleable.CompoSpinner_orientationLayout, 0);
        setText(key);
        array.recycle();
    }

    private void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mode.setTitle(text);
        }


    }
}
