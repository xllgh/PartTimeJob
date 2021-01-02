package com.xll.xinsheng.ui;


import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityReimburseBinding;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.Project;
import com.xll.xinsheng.bean.UserInfo;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.handler.MainEventHandler;
import com.xll.xinsheng.model.ReimburseModel;
import com.xll.xinsheng.tools.Utils;

import java.util.List;

public class ReimburseManageActivity extends XinActivity {

    private static final String TAG = "ReimburseManageActivity";

    private String orgId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReimburseBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_reimburse);
        Cache<InitialData> cache = new Cache<>(this, Cache.INITIAL_DATA);
        InitialData data = cache.getInitialData();
        binding.setHandler(new MainEventHandler());

        if (data == null) {
            Log.i(TAG, "InitialData is NULL");
            return;
        }


        List<Project> projects = data.getProjectList();
        String[] a = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            a[i] = projects.get(i).getProjectName();

        }
        ReimburseModel model = new ReimburseModel();

        model.setProjectList(a);
        model.setOrderNo(Utils.getProjectId());

        Cache<InitialData> initialDataCache = new Cache<>(this, Cache.INITIAL_DATA);
        InitialData initialData = initialDataCache.getInitialData();

        userId = initialData.getUserid();
        List<UserInfo> userInfos = initialData.getUserInfoList();
        if (userId != null && userInfos != null) {
            for (UserInfo userInfo : userInfos) {
                if (userId.equals(userInfo.getId())) {
                    orgId = userInfo.getOrgid();
                    model.setUserName(userInfo.getName());
                    model.setUserDepartment(userInfo.getOrgName());
                    break;
                }
            }
        }
        binding.setModel(model);


    }


}
