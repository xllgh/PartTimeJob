package com.xll.xinsheng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemAttachmentBinding;
import com.example.xinsheng.databinding.ItemAttachmentOrderBinding;
import com.xll.xinsheng.bean.FileInfo;
import com.xll.xinsheng.tools.HttpFileUtils;

import java.util.ArrayList;
import java.util.List;

public class AttachmentItemAdapter extends RecyclerView.Adapter<AttachmentItemAdapter.AttachmentItemHolder> {


    private final Context context;
    private static final String TAG = "AttachmentItemAdapter";

    final List<FileInfo> fileList = new ArrayList<>();

    public AttachmentItemAdapter(Context context, List<FileInfo> fileList) {
        this.context = context;
        this.fileList.addAll(fileList);
    }

    @NonNull
    @Override
    public AttachmentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ItemAttachmentOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_attachment_order,  parent, false);
       return new AttachmentItemHolder(binding);
    }

    public void addAttachmentItem(@NonNull FileInfo fileInfo) {
        Log.i(TAG, "addAttachmentItem");
        fileList.add(fileInfo);
        notifyDataSetChanged();
    }

    public void removeAttachmentItem(@NonNull FileInfo fileInfo) {
        Log.i(TAG, "removeAttachmentItem");
        fileList.remove(fileInfo);
        notifyDataSetChanged();
    }

    public void removeAllAttachment() {
        fileList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentItemHolder holder, int position) {
        final FileInfo fileInfo = fileList.get(position);
        final ItemAttachmentOrderBinding binding = holder.getBinding();
        binding.setFileName(fileInfo.getFileName());
        binding.setDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "delete:" + binding.getFileName());
                HttpFileUtils.deleteFile(fileInfo.getFileId(), context);
                removeAttachmentItem(fileInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class AttachmentItemHolder extends RecyclerView.ViewHolder{

        ItemAttachmentOrderBinding binding;
        public AttachmentItemHolder(@NonNull ItemAttachmentOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemAttachmentOrderBinding getBinding() {
            return binding;
        }
    }
}
