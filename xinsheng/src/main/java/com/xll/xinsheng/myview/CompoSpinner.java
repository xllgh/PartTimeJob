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
import com.xll.xinsheng.model.CompoSpinnerMode;

public class CompoSpinner extends ConstraintLayout {

    private static final String TAG = "CompoSpinner";
    private CompoSpinnerBinding binding;
    private CompoSpinnerMode mode;
    private String[] arrays;

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

    public int getCurrentPosition() {
        if (mode != null && binding != null) {
            return binding.spinner.getSelectedItemPosition();
        }
        return 0;
    }

    public int setCurrentPosition(int position) {
        if (position < 0 )
            return -1;
        if (mode != null && binding != null ) {
            Log.e(TAG, "setCurrentPosition:" + position );
            mode.setCurPosition(0);
            return position;
        }
        return -1;
    }

    public void setOnItemSelectListener(OnItemSelected selectListener) {
        if (binding != null) {
            binding.setItemSelected(selectListener);
        }
    }


    private void initView(Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.compo_spinner, this, true);
        mode = new CompoSpinnerMode();
        binding.setModel(mode);
    }


    public void setEntries(String[] entries) {
        this.arrays = entries;
        if (arrays != null) {
            mode.setEntries(arrays);
        }
    }


    private void initAttr(Context context, AttributeSet set) {
        TypedArray array = context.obtainStyledAttributes(set, R.styleable.CompoSpinner);
        String key = array.getString(R.styleable.CompoSpinner_title);
        array.recycle();
    }


}
