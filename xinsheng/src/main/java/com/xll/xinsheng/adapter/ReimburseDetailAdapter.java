package com.xll.xinsheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemOrderDetailBinding;
import com.xll.xinsheng.bean.OrderDetailItem;

import java.util.List;

public class ReimburseDetailAdapter extends RecyclerView.Adapter<ReimburseDetailAdapter.OrderDetailViewHolder> {

    private Context context;

    private List<OrderDetailItem> detailItems;

    public ReimburseDetailAdapter(Context context, List<OrderDetailItem> list) {
        this.context = context;
        this.detailItems = list;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailItem model = detailItems.get(position);
        holder.setMode(model);
    }

    @Override
    public int getItemCount() {
        return detailItems.size();
    }

    static class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        ItemOrderDetailBinding binding;

        public OrderDetailViewHolder(@NonNull ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setMode(OrderDetailItem mode) {
            binding.setMode(mode);
        }
    }
}
