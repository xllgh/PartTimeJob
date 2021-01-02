package com.yly.trainsystem.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.yly.trainsystem.R;
import com.yly.trainsystem.databinding.FragmentHomeBinding;
import com.yly.trainsystem.ui.order.OrderInfo;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

       Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent != null) {
            OrderInfo orderInfo = (OrderInfo) intent.getSerializableExtra("orderInfo");
            if (orderInfo != null )
            homeViewModel.oldOrderId.set(String.valueOf(orderInfo.getOrderId()));
        }

        FragmentHomeBinding binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.ad1);
        list.add(R.drawable.ad2);
        list.add(R.drawable.ad3);
       binding.setBookTicket(homeViewModel);
        binding.setHandler(new HomeEventHandler());
        binding.setAdRecyclerAdapter(new AdRecyclerAdapter(list, Objects.requireNonNull(getActivity())));
        return binding.getRoot();
    }


}
