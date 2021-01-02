package com.yly.trainsystem.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;



import com.yly.trainsystem.R;
import com.yly.trainsystem.databinding.FragmentOrderListBinding;

import java.util.List;

public class OrderListFragment extends Fragment {

    private static OrderListAdapter adapter;

    List<OrderInfo> orderItems;

    private boolean isExpired = false;



    public OrderListFragment(@NonNull List<OrderInfo> orderItems ) {
        this.orderItems = orderItems;
    }

    public OrderListFragment(@NonNull List<OrderInfo> orderItems, boolean isExpired ) {
        this.orderItems = orderItems;
        this.isExpired = isExpired;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("OrderListFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("OrderListFragment", "onCreateView");
        FragmentOrderListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false);
        adapter = new OrderListAdapter(orderItems, inflater.getContext(), isExpired);
        binding.setLayoutManage(new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false));
        binding.setOrderListAdapter(adapter);
        return binding.getRoot();
    }
}
