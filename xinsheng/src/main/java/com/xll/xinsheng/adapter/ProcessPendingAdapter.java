package com.xll.xinsheng.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemPendingProcessBinding;
import com.xll.xinsheng.model.PendingProcess;
import com.xll.xinsheng.ui.ProcessDetailActivity;

import java.util.List;
import java.util.Random;

public class ProcessPendingAdapter extends RecyclerView.Adapter<ProcessPendingAdapter.ProcessListViewHolder> {

    private static final String TAG = "ProcessPendingAdapter" ;
    private Context context;

    private List<PendingProcess> processList;

    private int[] backgroundList = new int[] {R.drawable.ic_round1, R.drawable.ic_round2, R.drawable.ic_round3,
            R.drawable.ic_round4, R.drawable.ic_round5, R.drawable.ic_round6};

    private int random = new Random().nextInt(backgroundList.length - 1);

    public ProcessPendingAdapter(Activity context, @NonNull List<PendingProcess> list) {
        this.context = context;
        this.processList = list;
    }

    public void update(List<PendingProcess> list) {
        processList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProcessListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPendingProcessBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_pending_process, parent, false);
        return new ProcessListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessListViewHolder holder, final int position) {
        PendingProcess model = processList.get(position);
        holder.setMode(model);

        final PendingProcess process = processList.get(position);

        int index = (random + position) % backgroundList.length;
        process.setImgRes(backgroundList[index]);
        holder.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String orderId = process.getOrderId();
               Intent intent = new Intent(context, ProcessDetailActivity.class);
               intent.putExtra("orderId", orderId);
               intent.putExtra("orderType", process.getProcessType());
                intent.putExtra("isDone",false);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return processList.size();
    }

    static class ProcessListViewHolder extends RecyclerView.ViewHolder {

        ItemPendingProcessBinding binding;

        public ProcessListViewHolder(@NonNull ItemPendingProcessBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setListener(View.OnClickListener listener) {
            binding.setListener(listener);
        }

        private void setMode(PendingProcess mode) {
            binding.setProcessMode(mode);
        }
    }
}
