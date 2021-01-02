package com.yly.trainsystem.ui.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;

import com.yly.trainsystem.SharedUtils;
import com.yly.trainsystem.ui.tickets.TicketsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HomeEventHandler {

    public void onQueryClick(HomeViewModel homeViewModel, Context context) {

        SharedUtils.getUserId(context);

        Log.e("HomeFragment", homeViewModel.departure.get()+ "");
        Log.e("HomeFragment", homeViewModel.departureTime.get()  + "");
        Log.e("HomeFragment", homeViewModel.destination.get() + "");

        HashMap<String, String> map = new HashMap<>();
        map.put("departure", homeViewModel.departure.get());
        map.put("destination", homeViewModel.destination.get());
        map.put("departureTime", homeViewModel.departureTime.get());
        String orderId = homeViewModel.oldOrderId.get();
        if (orderId != null && !TextUtils.isEmpty(orderId)) {
            map.put("oldOrderId", orderId);
        }
        Intent intent = new Intent(context, TicketsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", map);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void onExchangeAddress(HomeViewModel homeViewModel) {
        if (TextUtils.isEmpty(homeViewModel.departure.get())&& TextUtils.isEmpty(homeViewModel.destination.get()))
            return;
        String temp = homeViewModel.departure.get();
        homeViewModel.departure.set(homeViewModel.destination.get());
        homeViewModel.destination.set(temp);
    }

    public void onPopUpTimePicker(final HomeViewModel viewModel, Context context) {
        DatePickerDialog dpd =  new DatePickerDialog(context);
        dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                viewModel.departureTime.set(sdf.format(calendar.getTime()));
            }
        });
        dpd.show();

    }
}
