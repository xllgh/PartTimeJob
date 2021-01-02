package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityPendingDealBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.handler.PendingDealHandler;
import com.xll.xinsheng.model.PendingDealModel;

public class PendingDealActivity extends XinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       ActivityPendingDealBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_pending_deal);
        Intent intent = getIntent();
        String detailInfo = intent.getStringExtra("PendingDetail");
        if (!TextUtils.isEmpty(detailInfo)) {
            Gson gson = new Gson();
            PendingDetailInfo pendingDetailInfo = gson.fromJson(detailInfo, PendingDetailInfo.class);
            PendingDealHandler handler = new PendingDealHandler(pendingDetailInfo);
            binding.setHandler(handler);
            binding.setModel(new PendingDealModel());
        }

    }
}
