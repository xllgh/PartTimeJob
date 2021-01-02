package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityNoticeDetailBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.bean.NoticeInfo;
import com.xll.xinsheng.bean.NoticeItem;
import com.xll.xinsheng.cache.Cache;

public class NoticeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoticeDetailBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_notice_detail);
        Intent intent = getIntent();
        String id =  intent.getStringExtra("id");
        if (!TextUtils.isEmpty(id)) {
            Cache cache = new Cache(this, Cache.KEY_NOTICE);
            Gson gson = new Gson();
            NoticeInfo info =  gson.fromJson(cache.getStrInfo(), NoticeInfo.class);
            if (info != null && info.getList() !=null) {
                for(NoticeItem item : info.getList()) {
                    if (id.equals(item.getId())) {
                        //这种写法可以正确解码
                        //支持JS
                        WebSettings settings = binding.webview.getSettings();
                        settings.setSupportZoom(true);
                        settings.setBuiltInZoomControls(true);
                        settings.setJavaScriptEnabled(true);
                        settings.setDisplayZoomControls(false);
                        binding.webview.loadData(item.getContent(), "text/html; charset=UTF-8", null);
                       // binding.textView.setText(Html.fromHtml(item.getContent()));
                        return;

                    }
                }
            }


        }

    }
}
