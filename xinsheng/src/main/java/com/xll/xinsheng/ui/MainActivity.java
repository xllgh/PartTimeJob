package com.xll.xinsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.LoadMoreRecyclerAdapter;
import com.xll.xinsheng.adapter.NoticeAdapter;
import com.xll.xinsheng.adapter.WorkstationAdapter;
import com.xll.xinsheng.bean.DaiBanInfo;
import com.xll.xinsheng.bean.DaiBanItem;
import com.xll.xinsheng.bean.Notice;
import com.xll.xinsheng.bean.NoticeInfo;
import com.xll.xinsheng.bean.NoticeItem;
import com.xll.xinsheng.bean.YiBanInfo;
import com.xll.xinsheng.bean.YiBanItem;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.handler.MainEventHandler;
import com.xll.xinsheng.model.DoneProcess;
import com.xll.xinsheng.model.PendingProcess;
import com.xll.xinsheng.model.WorkViewModel;
import com.xll.xinsheng.tools.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends XinActivity {

    private static String TAG = "MainActivity";
    ActivityMainBinding binding;
    int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
       setOnline(binding);
        binding.setHandler(new MainEventHandler());
        getPendingInfo();
        getDoneInfo();
        getNotice(pageNumber);
        binding.noticeRecycler.addOnScrollListener(scrollListener);
    }

    LoadMoreRecyclerAdapter.EndlessRecyclerOnScrollListener scrollListener = new LoadMoreRecyclerAdapter.EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            getNotice(++pageNumber);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        getPendingInfo();
        getDoneInfo();
    }

    private void getNotice(int pageNumber) {
        binding.setNoticeLoadFinish(false);
        HashMap<String, String> map = new HashMap<>();
        map.put("PageNumber", String.valueOf(pageNumber));
        map.put("pageSize", "10");
        final Cache cache = new Cache(MainActivity.this, Cache.KEY_NOTICE);

        Gson gson = new Gson();
        NoticeInfo info =  gson.fromJson(cache.getStrInfo(), NoticeInfo.class);
        if (info != null && info.getList() != null) {
            binding.setNoticeLoadFinish(true);
            binding.setNoticeAdapter(new NoticeAdapter(info.getList(), MainActivity.this, info.getTotalRow()));
        }
        if (!cache.isExpired(2*60*60*1000, System.currentTimeMillis())) {
            return;
        }

        HttpUtils.post(HttpUtils.NOTICE, map, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                binding.setNoticeLoadFinish(true);
                Notice notice = gson.fromJson(response, Notice.class);

                NoticeInfo noticeList = notice.getNoticeList();
                if (noticeList != null) {
                    scrollListener.setTotal(noticeList.getTotalRow());
                    List<NoticeItem> list = noticeList.getList();
                    binding.setNoticeAdapter(new NoticeAdapter(list, MainActivity.this, noticeList.getTotalRow()));
                    cache.saveInfo(gson.toJson(noticeList));
                }
            }
        });
    }

    private void getDoneInfo() {

        HashMap<String, String> map = new HashMap<>();
        map.put("pageNumber", String.valueOf(1));
        map.put("pageSize", "10");

        HttpUtils.post(HttpUtils.DONE_ORDER, map, new HttpUtils.XinResponseListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                final YiBanInfo yiBanInfo = gson.fromJson(response, YiBanInfo.class);
                Log.i(TAG, "doneInfo:" + yiBanInfo.toString());
                Log.i(TAG, "doneResponse:" + response);
                binding.setYiBanCount(String.valueOf(yiBanInfo.getTotal()));
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
                    binding.setDoneListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, DoneActivity.class);
                            intent.putParcelableArrayListExtra("doneData", list);
                            intent.putExtra("total", yiBanInfo.getTotal());
                            startActivity(intent);
                        }
                    });
                }
            }
        });
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

                    binding.setDaiBanCount(String.valueOf(list.size()));
                    binding.setPendingListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, PendingActivity.class);
                            intent.putParcelableArrayListExtra("pendingData", list);
                            startActivity(intent);

                        }
                    });
                }

            }
        });
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, R.string.building, Toast.LENGTH_LONG).show();
        }
    };

    private void setOnline(ActivityMainBinding binding) {

        WorkViewModel process = new WorkViewModel(getString(R.string.loan_pay_request), R.drawable.process, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoanPayRequestActivity.class);
                startActivity(intent);
            }
        });
        WorkViewModel project = new WorkViewModel(getString(R.string.projectManage), R.drawable.project, listener);
        WorkViewModel apply = new WorkViewModel(getString(R.string.applyManage), R.drawable.apply, listener);
        WorkViewModel reimburse = new WorkViewModel(getString(R.string.reimburseManage), R.drawable.reimburse, listener/*, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReimburseManageActivity.class);
                startActivity(intent);
            }
        }*/);
        WorkViewModel doc = new WorkViewModel(getString(R.string.docManage), R.drawable.doc, listener);
        WorkViewModel human = new WorkViewModel(getString(R.string.humanManage), R.drawable.human, listener);

        List<WorkViewModel> list = new ArrayList<>();
        list.add(process);
        list.add(project);
        list.add(apply);
        list.add(reimburse);
        list.add(doc);
        list.add(human);

        GridLayoutManager grid = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        binding.setGrid1(grid);
        WorkstationAdapter onlineAdapter = new WorkstationAdapter(this, list);
        binding.setOnlineAdapter(onlineAdapter);
    }
}
