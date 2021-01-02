package com.xll.xinsheng.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.FragmentAttachmentBinding;
import com.xll.xinsheng.adapter.ProcessAttachmentAdapter;
import com.xll.xinsheng.bean.FileInfo;
import com.xll.xinsheng.tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class PendingAttachmentFragment extends Fragment {

    private List<FileInfo> fileInfoList = new ArrayList<>();

    public PendingAttachmentFragment(List<FileInfo> fileInfoList) {
        this.fileInfoList.addAll(fileInfoList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentAttachmentBinding binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_attachment, container, false);
        if (fileInfoList.size() > 0) {
            binding.setAdapter(new ProcessAttachmentAdapter(getActivity(), fileInfoList));

            binding.setShowNoData(false);
        } else {
            binding.setShowNoData(true);
        }

        Utils.applyPermission(getActivity());
        return binding.getRoot();
    }
}
