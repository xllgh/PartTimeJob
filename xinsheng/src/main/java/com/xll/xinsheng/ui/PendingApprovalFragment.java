package com.xll.xinsheng.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.FragmentApprovalBinding;
import com.xll.xinsheng.adapter.ProcessApprovalAdapter;
import com.xll.xinsheng.bean.OrderLog;

import java.util.ArrayList;
import java.util.List;

public class PendingApprovalFragment extends Fragment {

    List<OrderLog> logList = new ArrayList<>();

    PendingApprovalFragment(List<OrderLog> orderLogs) {
        logList.clear();
        logList.addAll(orderLogs);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       FragmentApprovalBinding approvalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_approval, container, false);
       if (logList.size() > 0) {
           approvalBinding.setAdapter(new ProcessApprovalAdapter(getActivity(), logList));
       }
       return approvalBinding.getRoot();
    }
}
