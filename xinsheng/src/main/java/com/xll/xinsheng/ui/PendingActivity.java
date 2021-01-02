package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityPendingBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.ProcessPendingAdapter;
import com.xll.xinsheng.bean.DaiBanInfo;
import com.xll.xinsheng.bean.DaiBanItem;
import com.xll.xinsheng.model.PendingProcess;
import com.xll.xinsheng.tools.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class PendingActivity extends XinActivity {
    private static final String TAG = "PendingActivity";
    ActivityPendingBinding binding;
    ProcessPendingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pending);
        Intent intent = getIntent();
        if (intent != null) {
            final ArrayList<PendingProcess> list  = intent.getParcelableArrayListExtra("pendingData");
            if (list != null && binding != null) {
                adapter = new ProcessPendingAdapter(PendingActivity.this, list);
                binding.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent");
        getPendingInfo();
    }



    private void getPendingInfo() {
        HttpUtils.post(HttpUtils.PENDING_ORDER, null, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DaiBanInfo daiBanInfo = gson.fromJson(response, DaiBanInfo.class);

                Log.i(TAG, response);
                List<DaiBanItem> daiBanList = daiBanInfo.getDaiBanList();
                if (daiBanList != null) {
                    final ArrayList<PendingProcess> list = new ArrayList<>();
                    for (DaiBanItem daiBanItem : daiBanList) {
                        PendingProcess process = new PendingProcess();
                        process.setOrderId(daiBanItem.getOrderId());
                        process.setOrderName(daiBanItem.getOrderName());
                        process.setOrderCreator(daiBanItem.getName());
                        process.setName(daiBanItem.getName());
                        process.setOrderCreateTime(daiBanItem.getCreateTime());
                        process.setOrderType(daiBanItem.getProcessType());
                        process.setOrderLastNode(daiBanItem.getLastName());
                        list.add(process);
                    }

                    if ( adapter != null) {
                        adapter.update(list);
                    } else {
                        binding.setAdapter(new ProcessPendingAdapter(PendingActivity.this, list));
                    }
                }

            }
        });
    }
}
