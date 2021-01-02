package com.xll.xinsheng;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xll.xinsheng.adapter.ProcessDoneAdapter;
import com.xll.xinsheng.bean.YiBanInfo;
import com.xll.xinsheng.bean.YiBanItem;
import com.xll.xinsheng.model.DoneProcess;
import com.xll.xinsheng.model.SearchModel;
import com.xll.xinsheng.tools.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoneHandler {
    private static String TAG = "DoneHandler";

    public void onReset(View view , SearchModel model) {
        model.setContent(null);
        model.setInitiator(null);
        model.setName(null);
        model.setOrderId(null);
        model.setSearchVisible(false);
        view.setVisibility(View.GONE);


    }

    public void onSearch(ProcessDoneAdapter adapter, SearchModel model) {
        model.setSearchVisible(false);
        getDoneInfo(adapter, model);

    }


    private void getDoneInfo(@NonNull final ProcessDoneAdapter adapter, SearchModel model) {
        HashMap<String, String> map = new HashMap<>();
        map.put("listName1", model.getOrderId()+"");
        map.put("listName2", model.getName()+"");
        map.put("listName3", model.getInitiator()+"");
        map.put("listName4", model.getContent()+"");
        HttpUtils.post(HttpUtils.DONE_ORDER, map, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                final YiBanInfo yiBanInfo = gson.fromJson(response, YiBanInfo.class);
                Log.i(TAG, "doneInfo:" + yiBanInfo.toString());
                Log.i(TAG, "doneResponse:" + response);
                List<YiBanItem> yiBanItems = yiBanInfo.getYiBanItemList();
                Log.i(TAG, response);
                if (yiBanItems != null) {
                    final ArrayList<DoneProcess> list = new ArrayList<>();
                    for (YiBanItem yiBanItem : yiBanItems) {
                        DoneProcess process = new DoneProcess();
                        process.setOrderId(yiBanItem.getOrderId());
                        process.setOrderName(yiBanItem.getOrderName());
                        process.setOrderStatus(yiBanItem.getStatus());
                        process.setProcessType(yiBanItem.getProcessType());
                        process.setName(yiBanItem.getName());
                        list.add(process);
                    }
                    adapter.resetUpdate(list);
                }
            }
        });
    }
}
