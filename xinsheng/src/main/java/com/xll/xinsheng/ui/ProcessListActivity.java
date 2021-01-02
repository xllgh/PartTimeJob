package com.xll.xinsheng.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityProcessListBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.ProcessPendingAdapter;
import com.xll.xinsheng.tools.DoneProcessResponse;
import com.xll.xinsheng.tools.HttpUtils;

public class ProcessListActivity extends XinActivity {

    private ActivityProcessListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_process_list);
        HttpUtils.post(HttpUtils.DONE_LIST, null, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DoneProcessResponse done = gson.fromJson(response, DoneProcessResponse.class);
                ProcessPendingAdapter adapter = new ProcessPendingAdapter(ProcessListActivity.this, done.getRows());
                binding.setAdapter(adapter);
            }
        });
    }
}
