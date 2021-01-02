package com.yly.trainsystem.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Response;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.yly.trainsystem.R;
import com.yly.trainsystem.SharedUtils;
import com.yly.trainsystem.databinding.FragmentOrderBinding;
import com.yly.trainsystem.volley.HttpUtils;
import com.yly.trainsystem.volley.ServerOrderResponseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;

    private FragmentOrderBinding binding;

    private Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            if (response != null) {
                Gson gson = new Gson();
                ServerOrderResponseArray orderResponse = gson.fromJson(response, ServerOrderResponseArray.class);
                List<OrderInfo> orderInfos = orderResponse.getData();
                List<OrderInfo> futureOrder = new ArrayList<>();
                List<OrderInfo> historyOrder = new ArrayList<>();

                for (OrderInfo orderInfo: orderInfos) {
                   int result =  orderInfo.getDepartureTime().compareTo(new Date());
                   if (result <= 0) {
                       historyOrder.add(orderInfo);
                   } else {
                       futureOrder.add(orderInfo);
                   }
                }

                List<OrderListFragment> list = new ArrayList<>();
                list.add(new OrderListFragment(futureOrder));
                list.add(new OrderListFragment(historyOrder, true));
                final OrderAdapter orderAdapter = new OrderAdapter(Objects.requireNonNull(getActivity()), list);
                binding.setOrderAdapter(orderAdapter);
            }

        }
    };

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        orderViewModel =
                ViewModelProviders.of(this).get(OrderViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        binding.setOrderViewModel(orderViewModel);
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", SharedUtils.getUserId(getActivity()));
        HttpUtils.post(HttpUtils.QUERY_USER_TICKET, map, listener, null);
        TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

               if (tab.getText().equals(inflater.getContext().getString(R.string.future_order))) {
                    orderViewModel.currentPage.set(0);
                } else {
                   orderViewModel.currentPage.set(1);
               }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

        binding.setTabSelectListener(tabSelectedListener);

        return binding.getRoot();
    }
}
