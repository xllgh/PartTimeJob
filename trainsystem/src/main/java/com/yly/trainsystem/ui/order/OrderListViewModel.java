package com.yly.trainsystem.ui.order;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class OrderListViewModel extends ViewModel {
    List<OrderItem> orderItems;

    public OrderListViewModel(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
