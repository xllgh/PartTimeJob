package com.xll.xinsheng.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.FragmentPendingBasicBinding;
import com.xll.xinsheng.adapter.ReimburseDetailAdapter;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.bean.OrderDetailItem;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.tools.Utils;

import java.util.List;

public class PendingBasicFragment extends Fragment {

    private PendingDetailInfo info;

    public PendingBasicFragment(@NonNull  PendingDetailInfo info) {
        super();
        this.info = info;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       FragmentPendingBasicBinding basicBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending_basic, container, false);

        if (info == null) {
            return basicBinding.getRoot();
        }
        basicBinding.setIsReimburse(Utils.isReimburse(info.getOrderId()));
        List<PaymentItem> paymentItems = info.getPaymentList();
        if (paymentItems != null && paymentItems.size() > 0) {
           PaymentItem item = paymentItems.get(0);
           item.setOrderId(info.getOrderId());
           basicBinding.setPaymentInfo(item);

            List<OrderDetailItem> orderDetailItems = info.getDetailList();
            List<ItemType> itemTypes = info.getItemTypeList();

            if (orderDetailItems != null && orderDetailItems.size() > 0) {
                if (itemTypes != null && itemTypes.size() > 0) {
                    for (ItemType type : itemTypes) {
                        for (OrderDetailItem detailItem : orderDetailItems) {
                            if(!TextUtils.isEmpty(type.getTypeId()) && type.getTypeId().equals(detailItem.getBxDx())) {
                                detailItem.setInvoiceType(type.getTypeName());
                            }
                        }
                    }
                }
                basicBinding.setReimburseNoData(false);
                basicBinding.setDetailAdapter(new ReimburseDetailAdapter(getActivity(), orderDetailItems));
            } else {
                basicBinding.setReimburseNoData(true);
            }
       }



        return basicBinding.getRoot();
    }
}
