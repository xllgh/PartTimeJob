package com.yly.trainsystem.ui.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yly.trainsystem.MainActivity;
import com.yly.trainsystem.R;
import com.yly.trainsystem.SharedUtils;
import com.yly.trainsystem.databinding.OrderItemBinding;
import com.yly.trainsystem.volley.HttpUtils;
import com.yly.trainsystem.volley.ServerOrderResponseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderItemViewHolder> {

    private List<OrderInfo> orderInfos = new ArrayList<>();
    private Context context;
    private boolean isExpired = false;

    public OrderListAdapter(List<OrderInfo> orderInfos, Context context) {
        this.orderInfos.addAll(orderInfos);
        this.context = context;
    }

    public OrderListAdapter(List<OrderInfo> orderInfos, Context context, boolean isExpired) {
        this.orderInfos.addAll(orderInfos);
        this.context = context;
        this.isExpired = isExpired;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_item, parent, false);
        return new OrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        final OrderItem orderItem = new OrderItem();
        final OrderInfo orderInfo = orderInfos.get(position);
        orderItem.setDeparture(orderInfo.getDeparture());
        orderItem.setDestination(orderInfo.getDestination());
        orderItem.setDepartureTime(String.format(context.getString(R.string.depart_time_fomart),
                orderInfo.getDepartureTime()));
        orderItem.setTrainName(orderInfo.getTrainName());

        holder.binding.setOrderItem(orderItem);
        holder.binding.setIsExpired(isExpired);
        setListener(holder, orderInfo);

    }

    private void setListener(@NonNull OrderItemViewHolder holder, final OrderInfo orderInfo) {
        holder.binding.setRefundListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> param = new HashMap<>();
                param.put("userId", SharedUtils.getUserId(context));
                param.put("orderId", String.valueOf(orderInfo.getOrderId()));
                HttpUtils.post(HttpUtils.REFUND_TICKET, param, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ServerOrderResponseArray orderResponse = new Gson().fromJson(response, ServerOrderResponseArray.class);
                        if (orderResponse != null) {
                            List<OrderInfo> infos = orderResponse.getData();
                            orderInfos.clear();
                            for (OrderInfo o : infos) {
                                int result = orderInfo.getDepartureTime().compareTo(new Date());
                                if (isExpired) {
                                    if (result <= 0) {
                                        orderInfos.add(orderInfo);
                                    }
                                } else {
                                    if (result > 0) {
                                        orderInfos.add(orderInfo);
                                    }
                                }
                            }
                            Toast.makeText(context, R.string.refund_success, Toast.LENGTH_LONG).show();
                            notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, R.string.refund_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.binding.setChangeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("orderInfo", orderInfo);
                intent.putExtra("oldOrderId", orderInfo.getOrderId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderInfos.size();
    }


    static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        OrderItemBinding binding;

        public OrderItemBinding getBinding() {
            return binding;
        }

        OrderItemViewHolder(@NonNull OrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void setOrderItem(OrderItem orderItem) {
            binding.setOrderItem(orderItem);
        }
    }
}
