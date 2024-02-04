package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityPendingDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.PendingPagerAdapter;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.handler.PendingDetailHandler;
import com.xll.xinsheng.tools.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcessDetailActivity extends XinActivity {

    public ObservableField<Integer> currentTab = new ObservableField<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityPendingDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_pending_detail);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("orderId");
        boolean isDone = intent.getBooleanExtra("isDone", false);
        binding.setIsDone(isDone);

        HashMap<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("isGetData", "yes");
        binding.setIsLoading(true);
        HttpUtils.post(HttpUtils.ORDER_EDIT, param, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                binding.setIsLoading(false);
                Gson gson = new Gson();
                PendingDetailInfo info = gson.fromJson(response, PendingDetailInfo.class);
                info.setOrderId(id);
                List<Fragment> fragments = new ArrayList<>();
                fragments.add(new PendingBasicFragment(info));
                fragments.add(new PendingApprovalFragment(info.getOrderLogList()));
                fragments.add(new PendingAttachmentFragment(info.getFileList()));
                binding.viewPager.setAdapter(new PendingPagerAdapter(ProcessDetailActivity.this, fragments));
                binding.setHandler(new PendingDetailHandler(info));
                final int[] titleList = new int[] {R.string.basic_info, R.string.approval_info, R.string.attachment};
                binding.viewPager.setOffscreenPageLimit(fragments.size());
                TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, true,  new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(titleList[position]);
                    }
                });
                mediator.attach();

            }

            @Override
            public void onError(String response) {

            }
        });
    }
}
