package com.xll.xinsheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.WorkItemBinding;
import com.xll.xinsheng.model.WorkViewModel;

import java.util.List;

public class WorkstationAdapter extends RecyclerView.Adapter<WorkstationAdapter.WorkstationViewHolder> {

    private Context context;
    private List<WorkViewModel> list;

    public WorkstationAdapter(Context context, List<WorkViewModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public WorkstationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.work_item, parent, false);
        return new WorkstationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkstationViewHolder holder, int position) {
        WorkViewModel model = list.get(position);
        WorkItemBinding binding = holder.getBinding();
        if (model.getListener() != null) {
            binding.setListener(model.getListener());
        }

        binding.setWork(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class WorkstationViewHolder extends RecyclerView.ViewHolder {

        private WorkItemBinding binding;

        public WorkstationViewHolder(@NonNull WorkItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public WorkItemBinding getBinding() {
            return binding;
        }
    }
}
