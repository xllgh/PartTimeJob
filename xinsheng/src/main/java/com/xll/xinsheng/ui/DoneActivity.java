package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityDoneBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.DoneHandler;
import com.xll.xinsheng.adapter.LoadMoreRecyclerAdapter;
import com.xll.xinsheng.adapter.ProcessDonePageAdapter;
import com.xll.xinsheng.bean.YiBanInfo;
import com.xll.xinsheng.bean.YiBanItem;
import com.xll.xinsheng.model.DoneProcess;
import com.xll.xinsheng.model.SearchModel;
import com.xll.xinsheng.tools.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DoneActivity extends XinActivity {

    private static final String TAG= "DoneActivity";
    private boolean isLoading = false;

    private int page = 1;
    ActivityDoneBinding binding;
    SearchModel model = new SearchModel();
    //ProcessDoneAdapter adapter;
    ProcessDonePageAdapter donePageAdapter;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_done);
        binding.setSearchModel(model);
        binding.setHandler(new DoneHandler());

        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<DoneProcess> doneList = intent.getParcelableArrayListExtra("doneData");
            total = intent.getIntExtra("total", 0);
            Log.i(TAG, "total:" + total);
           // adapter = new ProcessDoneAdapter(this, doneList, total);
            donePageAdapter = new ProcessDonePageAdapter(this, doneList, total);
            binding.setAdapter(donePageAdapter);
            binding.recyclerView.addOnScrollListener(onScrollListener);
        }


        binding.setSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding != null) {
                    binding.setSearchVisible(!visible);
                    visible = !visible;
                }
                getDoneInfo(donePageAdapter, true, model, 1);
            }
        });

        binding.setResetListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               binding.eOrderId.setText("");
               binding.eContent.setText("");
               binding.eInitiator.setText("");
               binding.eName.setText("");
            }
        });

    }


    private final Timer timer = new Timer();



    LoadMoreRecyclerAdapter.EndlessRecyclerOnScrollListener onScrollListener = new LoadMoreRecyclerAdapter.EndlessRecyclerOnScrollListener(total) {
        @Override
        public void onLoadMore() {
            if (!isLoading){
                getDoneInfo(donePageAdapter, false, model, ++page);
            }
        }
    };

    MenuItem moreItem;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        moreItem = menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, null);
        moreItem.setIcon(R.drawable.search);
        moreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean visible = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == Menu.FIRST ) {
            if (binding != null) {
                binding.setSearchVisible(!visible);
                visible = !visible;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void getDoneInfo(@NonNull final ProcessDonePageAdapter adapter, final boolean isSearch, SearchModel model, int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("listName1", model.getOrderId());
        map.put("listName2", model.getName());
        map.put("listName3", model.getInitiator());
        map.put("listName4", model.getContent());
        map.put("pageNumber", String.valueOf(page));
        map.put("pageSize", "10");
        isLoading = true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isLoading = false;
                Log.i(TAG, "isLoading" + isLoading);
            }
        };

        timer.schedule(task, 3000);

        HttpUtils.post(HttpUtils.DONE_ORDER, map, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                final YiBanInfo yiBanInfo = gson.fromJson(response, YiBanInfo.class);
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

                    adapter.setTotal(yiBanInfo.getTotal());
                    onScrollListener.setTotal(yiBanInfo.getTotal());
                    if (!isSearch) {
                        adapter.update(list);
                    } else {
                        adapter.resetUpdate(list);
                    }
                }
             isLoading = false;
            }
        });
    }



}
