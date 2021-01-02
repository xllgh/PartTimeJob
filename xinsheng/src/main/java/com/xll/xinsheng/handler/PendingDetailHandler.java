package com.xll.xinsheng.handler;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.ui.PendingDealActivity;

public class PendingDetailHandler {

    private PendingDetailInfo info;

    public PendingDetailHandler(PendingDetailInfo info) {
        this.info = info;
    }

    public void onDealClick(Context context) {
        Intent intent = new Intent(context, PendingDealActivity.class);
        if (info != null) {
            intent.putExtra("PendingDetail", new Gson().toJson(info));
        }
        context.startActivity(intent);
    }

}
