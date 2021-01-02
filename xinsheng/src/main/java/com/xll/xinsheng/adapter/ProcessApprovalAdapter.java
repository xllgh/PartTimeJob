package com.xll.xinsheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemOrderLogBinding;
import com.xll.xinsheng.bean.OrderLog;

import java.util.List;

public class ProcessApprovalAdapter extends RecyclerView.Adapter<ProcessApprovalAdapter.OrderLogViewHolder> {

    private Context context;

    private List<OrderLog> processList;

    public ProcessApprovalAdapter(Context context, List<OrderLog> list) {
        this.context = context;
        this.processList = list;
    }

    @NonNull
    @Override
    public OrderLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ItemOrderLogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_log, parent, false);
        return new OrderLogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderLogViewHolder holder, int position) {
        OrderLog model = processList.get(position);
        if (position == processList.size() - 1) {
            model.setImageRes(R.drawable.ic_pending);
        } else {
            model.setImageRes(R.drawable.ic_done);
        }
        holder.setMode(model);
    }

    @Override
    public int getItemCount() {
        return processList.size();
    }

    static class OrderLogViewHolder extends RecyclerView.ViewHolder {

        ItemOrderLogBinding binding;

        public OrderLogViewHolder(@NonNull ItemOrderLogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setMode(OrderLog mode) {
            binding.setLogModel(mode);
        }
    }
}
