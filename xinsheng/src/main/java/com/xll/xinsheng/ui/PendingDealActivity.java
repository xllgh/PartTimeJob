package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityPendingDealBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.bean.InvoiceType;
import com.xll.xinsheng.bean.OrderDetailItem;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.handler.PendingDealHandler;
import com.xll.xinsheng.model.PendingDealModel;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.Utils;

import java.util.HashMap;
import java.util.List;

public class PendingDealActivity extends XinActivity {


    private static final String TAG = "PendingDealActivity";

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

            PendingDealHandler handler = new PendingDealHandler(this, pendingDetailInfo);
            for(PaymentItem item:paymentItems){
                if (Utils.Initiator.equals(item.getNow_node())) {
                    binding.setIsInitiator(true);
                    break;
                }
            }

            List<OrderDetailItem> detailList = pendingDetailInfo.getDetailList();
            if(detailList != null && !detailList.isEmpty()) {
                for (OrderDetailItem detailItem : detailList) {
                    getInvoiceType(detailItem.getBxProject(),detailItem.getBxDx());
                }
            }
            binding.setHandler(handler);
            binding.setModel(new PendingDealModel());
        }
    }


    private void getInvoiceType(final String projectListId, final String bxDx) {

        if(TextUtils.isEmpty(projectListId) || TextUtils.isEmpty(bxDx)) {
            return;
        }
        final HashMap<String, String> requestMap = new HashMap<>();
        final Cache<InvoiceType> cache = new Cache<>(this, Cache.KMXX_INFO);
        requestMap.put("id", bxDx);
        requestMap.put("projectId", projectListId);
        HttpUtils.post(HttpUtils.INVOICE_TYPE, requestMap, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                InvoiceType invoiceType = gson.fromJson(response, InvoiceType.class);
                invoiceType.setProjectListId(projectListId);
                cache.saveInvoiceType(projectListId+bxDx, new Gson().toJson(invoiceType));
                Log.i(TAG, "save cache InvoiceType");
            }

            @Override
            public void onError(String response) {

            }
        });
    }
}
