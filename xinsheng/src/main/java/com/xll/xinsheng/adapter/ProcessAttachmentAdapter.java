package com.xll.xinsheng.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemAttachmentBinding;
import com.xll.xinsheng.bean.FileInfo;
import com.xll.xinsheng.handler.AttachmentHandler;

import java.util.List;

public class ProcessAttachmentAdapter extends RecyclerView.Adapter<ProcessAttachmentAdapter.AttachmentViewHolder> {

    private Context context;

    private List<FileInfo> fileInfos;

    public ProcessAttachmentAdapter(Context context, List<FileInfo> list) {
        this.context = context;
        this.fileInfos = list;
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ItemAttachmentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_attachment, parent, false);
       binding.setHandler(new AttachmentHandler());
       return new AttachmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int position) {
        FileInfo model = fileInfos.get(position);
        holder.setMode(model);

    }

    @Override
    public int getItemCount() {
        return fileInfos.size();
    }

    static class AttachmentViewHolder extends RecyclerView.ViewHolder {

        ItemAttachmentBinding binding;

        public AttachmentViewHolder(@NonNull ItemAttachmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setMode(FileInfo mode) {
            binding.setFile(mode);
            Linkify.addLinks(binding.tv, Linkify.ALL);

        }
    }
}
