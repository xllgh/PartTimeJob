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
import com.example.xinsheng.databinding.ItemNoticeBinding;
import com.xll.xinsheng.bean.NoticeItem;
import com.xll.xinsheng.ui.NoticeDetailActivity;

import java.util.List;

public class NoticeAdapter extends LoadMoreRecyclerAdapter<NoticeItem> {

    private List<NoticeItem> list;
    private Context context;


    public NoticeAdapter(@NonNull List<NoticeItem> list, Context context, int total) {
        super(list, context, total);
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemNoticeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_notice, parent, false);
        return new NoticeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof NoticeViewHolder) {
            NoticeViewHolder noticeViewHolder = (NoticeViewHolder) holder;
            noticeViewHolder.bindHolder(list.get(position));
            noticeViewHolder.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NoticeDetailActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class NoticeViewHolder extends RecyclerView.ViewHolder {

        ItemNoticeBinding binding;

        public NoticeViewHolder(@NonNull ItemNoticeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindHolder(NoticeItem item) {
            binding.setNotice(item);
        }

        void setListener(View.OnClickListener listener) {
            binding.setListener(listener);
        }
    }
}
