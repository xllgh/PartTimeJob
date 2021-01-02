package com.xll.xinsheng.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemDoneProcessNewBinding;
import com.xll.xinsheng.model.DoneProcess;
import com.xll.xinsheng.ui.ProcessDetailActivity;

import java.util.List;
import java.util.Random;

public class ProcessDonePageAdapter extends LoadMoreRecyclerAdapter<DoneProcess> {

    private Context context;

    private List<DoneProcess> processList;

    private static final int TYPE_DATA = 1;
    private static final int TYPE_HINT = 0;

    private int[] backgroundList = new int[]{R.drawable.ic_round1, R.drawable.ic_round2, R.drawable.ic_round3,
            R.drawable.ic_round4, R.drawable.ic_round5, R.drawable.ic_round6};

    private int random = new Random().nextInt(backgroundList.length - 1);
    private int total;

    public ProcessDonePageAdapter(Context context, List<DoneProcess> list, int total) {
        super(list, context, total);
        this.context = context;
        this.processList = list;
        this.total = total;
    }

    /**
     * 加载更多
     * 1.两个布局，两个ViewHolder
     **/

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDoneProcessNewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_done_process_new, parent, false);
        return new ProcessListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ProcessListViewHolder) {
            ProcessListViewHolder h = (ProcessListViewHolder) holder;

            final DoneProcess model = processList.get(position);
            h.setMode(model);

            final int index = (random + position) % backgroundList.length;
            model.setImgRes(backgroundList[index]);
            h.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderId = model.getOrderId();
                    Intent intent = new Intent(context, ProcessDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("orderType", model.getProcessType());
                    intent.putExtra("isDone", true);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void update(List<DoneProcess> data) {
        int currentIndex = processList.size() - 2;
        processList.addAll(data);
        notifyItemRangeChanged(currentIndex, processList.size());
    }

    public void resetUpdate(List<DoneProcess> data) {
        processList.clear();
        processList.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return processList.size();
    }


    static class ProcessListViewHolder extends RecyclerView.ViewHolder {

        ItemDoneProcessNewBinding binding;

        public ProcessListViewHolder(@NonNull ItemDoneProcessNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setListener(View.OnClickListener listener) {
            binding.setListener(listener);
        }

        private void setMode(DoneProcess mode) {
            binding.setProcessMode(mode);
        }
    }
}
