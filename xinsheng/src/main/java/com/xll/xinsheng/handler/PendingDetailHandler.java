package com.xll.xinsheng.handler;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.ui.PendingDealActivity;

import java.util.List;

public class PendingDetailHandler {

    private PendingDetailInfo info;

    public PendingDetailHandler(PendingDetailInfo info) {
        this.info = info;
    }

    public void onDealClick(Context context) {
        Intent intent = new Intent(context, PendingDealActivity.class);
        if (info != null) {
           final List<PaymentItem> paymentList = info.getPaymentList();
           if (paymentList != null) {
               for (PaymentItem item : paymentList) {
                    if ("1".equals(item.getNow_node())){
                        //TODO 可以编辑
                       String orderId = info.getOrderId();
                        break;
                    }
               }
           }
            intent.putExtra("PendingDetail", new Gson().toJson(info));
        }
        context.startActivity(intent);
    }

}
