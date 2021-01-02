package com.xll.xinsheng.handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.xinsheng.R;
import com.xll.xinsheng.bean.FileInfo;
import com.xll.xinsheng.tools.AttachmentOpenUtils;
import com.xll.xinsheng.tools.OkHttpUtils;
import com.xll.xinsheng.tools.HttpUtils;

import java.io.File;

public class AttachmentHandler {
    private static final String TAG = "AttachmentHandler";

    public void onFileClick(final Context context, FileInfo fileInfo) {
        String downloadUrl = HttpUtils.FILE_URL_HEADER + fileInfo.getRealFileName();
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.downloading);
        dialog.show();
        OkHttpUtils.get().download(downloadUrl, "/mnt/sdcard/", fileInfo.getRealFileName(),
                new OkHttpUtils.OnProcessListener() {
                    @Override
                    public void onSuccess(File file, String str) {
                        dialog.dismiss();
                        Log.d(TAG, "onDownloadSuccess");
                        AttachmentOpenUtils.openFile(context, file);
                    }

                    @Override
                    public void onProcessing(int progress) {
                        Log.d(TAG, "onDownloading" + progress);

                    }

                    @Override
                    public void onFailed(Exception e) {
                        dialog.dismiss();
                        Log.d(TAG, "onDownloadFailed" + e.getMessage());
                    }
                });

    }

}
