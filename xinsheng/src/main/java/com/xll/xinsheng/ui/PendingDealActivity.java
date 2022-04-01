package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityPendingDealBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.handler.PendingDealHandler;
import com.xll.xinsheng.model.PendingDealModel;

import java.util.List;

public class PendingDealActivity extends XinActivity {

    private static final String Initiator = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       ActivityPendingDealBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_pending_deal);
        Intent intent = getIntent();
        String detailInfo = intent.getStringExtra("PendingDetail");
        Log.i("PendingDetail", detailInfo);
        binding.setIsInitiator(false);

        if (!TextUtils.isEmpty(detailInfo)) {
            Gson gson = new Gson();
            PendingDetailInfo pendingDetailInfo = gson.fromJson(detailInfo, PendingDetailInfo.class);
            List<PaymentItem> paymentItems = pendingDetailInfo.getPaymentList();

            PendingDealHandler handler = new PendingDealHandler(pendingDetailInfo);
            for(PaymentItem item:paymentItems){
                if (Initiator.equals(item.getNow_node())) {
                    binding.setIsInitiator(true);
                    break;
                }
            }
            binding.setHandler(handler);
            binding.setModel(new PendingDealModel());
        }

    }
}
