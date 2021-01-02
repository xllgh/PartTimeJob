package com.xll.xinsheng.model;

import android.widget.Spinner;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import com.example.xinsheng.BR;

@InverseBindingMethods({
        @InverseBindingMethod(type = Spinner.class, attribute = "android:selectedItemPosition"),
})
public class CompoSpinnerMode extends BaseObservable {

    private String[] entries;

    private int curPosition;

    @Bindable
    public String[] getEntries() {
        return entries;
    }

    public void setEntries(String[] entries) {
        this.entries = entries;
        notifyPropertyChanged(BR.entries);

    }

    @Bindable
    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        notifyPropertyChanged(BR.curPosition);
    }

    public int getPosition(Spinner spinner) {
        return spinner.getSelectedItemPosition();
    }
}
