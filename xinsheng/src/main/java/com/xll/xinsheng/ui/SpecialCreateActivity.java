package com.xll.xinsheng.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.AdapterViewBindingAdapter;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivitySpecialCreateBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.AttachmentItemAdapter;
import com.xll.xinsheng.bean.FileInfo;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.bean.Project;
import com.xll.xinsheng.bean.TPResponse;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.SpecialRequestModel;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.HttpFileUtils;
import com.xll.xinsheng.tools.Uri2PathUtil;
import com.xll.xinsheng.tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpecialCreateActivity extends AppCompatActivity {

    private static final String TAG = "SpecialCreateActivity";
    private SpecialRequestModel model = new SpecialRequestModel();
    private String defaultProject;
    private String defaultRequestType;
    private String projectListId;
    private String defaultFeeDept;
    private final List<FileInfo> fileList = new ArrayList<>();
    private AttachmentItemAdapter attachmentItemAdapter;
    private static final int OPEN_FILE_REQUEST_CODE_SPECIAL = 122;
    private  List<Project> projectList;
    private List<ItemType> itemTypeList;
    private String nowNode;
    private long lastSubmitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySpecialCreateBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_special_create);
        binding.setModel(model);
        InitialData data = new Cache(this, Cache.INITIAL_DATA).getInitialData();
        Log.i(TAG, "InitialData:" + data);
        attachmentItemAdapter = new AttachmentItemAdapter(this, fileList);
        binding.setAttachmentAdapter(attachmentItemAdapter);

        if(data != null) {
            projectList = data.getProjectList();
            itemTypeList = data.getItemTypeList();
        }

        getIntentInfo();
        initProjectInfo(projectList, defaultProject, binding);
        initRequestTypeInfo(itemTypeList, defaultRequestType, binding);
        setListener(projectList, binding, data);
    }


    private void setListener(final List<Project> projectList, final ActivitySpecialCreateBinding binding, final InitialData data) {

        binding.projectType.setOnItemSelectListener(new AdapterViewBindingAdapter.OnItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected:" + position);
                if (position < projectList.size()) {
                    projectListId = projectList.get(position).getProjectId();
                } else {
                    projectListId = null;
                }
            }
        });
        binding.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecialCreateActivity.this.finish();
            }
        });

        binding.setSubmitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpecialProcess(model, data, binding);
            }
        });

        binding.setOpenFileListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, OPEN_FILE_REQUEST_CODE_SPECIAL);
            }
        });
    }

    private void getIntentInfo() {
        Intent intent = getIntent();
        String PendingDetailInfo = intent.getStringExtra("PendingDetailInfo");
        if (!TextUtils.isEmpty(PendingDetailInfo)) {
            PendingDetailInfo info = new Gson().fromJson(PendingDetailInfo, PendingDetailInfo.class);
            //设置订单号
            model.setTicketId(info.getOrderId());
            List<PaymentItem> paymentList = info.getPaymentList();
            for (PaymentItem item : paymentList) {
                Log.i(TAG, "PaymentItem:" + item.toString());
                //设置报销原因
                model.setReimburseReason(item.getSeal_desc());
                model.setSpecialFee(item.getFee() + "");
                defaultProject = item.getProject_id();
                defaultFeeDept = item.getFee_dept();
                defaultRequestType = item.getFee_type();
                nowNode = item.getNow_node();
                projectListId = item.getProject_id();
                break;
            }

            //附件文件
            List<FileInfo> fileList = info.getFileList();
            if (fileList != null) {
                for (FileInfo fileInfo : fileList) {
                    attachmentItemAdapter.addAttachmentItem(fileInfo);
                }
            }
        } else {
            model.setTicketId(Utils.generateId("tp-"));
        }
    }


    private void initRequestTypeInfo(List<ItemType> itemTypeList, String defaultRequestType, ActivitySpecialCreateBinding binding ) {
        //申请类型
        if (itemTypeList != null) {
            String[] itemArray = new String[itemTypeList.size() + 1];
            int i = 0;
            itemArray[i++] = getString(R.string.select);
            int itemSelect = 0;
            for (ItemType itemType : itemTypeList) {
                String name = itemType.getTypeName();
                String typeId = itemType.getTypeId();
                if(!TextUtils.isEmpty(typeId)) {
                    if(typeId.equals(defaultRequestType)) {
                        itemSelect = i;
                    }
                }
                itemArray[i++] = name;
            }
            Log.i(TAG, "itemSelect:" + itemSelect);
            binding.requestType.setCurrentPosition(itemSelect);//TODO
            model.setRequestEntries(itemArray);
        }
    }

    private void initProjectInfo(List<Project> projectList, String defaultProject, ActivitySpecialCreateBinding binding) {
        if (projectList != null && projectList.size() > 0) {
            String[] projectArray = new String[projectList.size()];
            int i = 0;
            for (Project project : projectList) {
                String projectName = project.getProjectName();
                String projectTypeId = project.getProjectId();

                //设置初始值
                if (!TextUtils.isEmpty(projectTypeId) && projectTypeId.equals(defaultProject)) {
                    binding.projectType.setCurrentPosition(i);
                    projectListId = project.getProjectId();
                }
                projectArray[i++] = projectName;

            }
            model.setProjectEntries(projectArray);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE_REQUEST_CODE_SPECIAL && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (Uri2PathUtil.getRealPathFromUri(this, uri) != null) {
                //从uri得到绝对路径，并获取到file文件
                String filePath = Uri2PathUtil.getRealPathFromUri(this, uri);
                if (!TextUtils.isEmpty(filePath)) {
                    model.setUploadFileName(filePath.substring(filePath.lastIndexOf("/")));
                } else {
                    Toast.makeText(SpecialCreateActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
                }
                Log.e(TAG, filePath);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(filePath);
                attachmentItemAdapter.addAttachmentItem(fileInfo);
                uploadFile(filePath);
            } else {
                Toast.makeText(SpecialCreateActivity.this, R.string.obtain_fail_hint, Toast.LENGTH_LONG).show();
                Log.e(TAG, "获取文件失败");
            }
        }
    }



    private void uploadFile(String filePath) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            Toast.makeText(SpecialCreateActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
            return;
        }
        final HashMap<String, Object> param = new HashMap<>();
        param.put("businessId", model.getTicketId());
        param.put("myfile", new File(filePath));
        //param.put("businessId", projectId);
        final ProgressDialog dialog = new ProgressDialog(SpecialCreateActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.uploading);
        dialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HttpFileUtils.upLoadFile(HttpFileUtils.UPLOAD_FILE_URL, param, new HttpFileUtils.OnProcessListener() {
                    @Override
                    public void onSuccess(File file, String str) {
                        Log.e(TAG, "onSuccess");
                        dialog.dismiss();
                        HttpFileUtils.toastUpFileSuccess(SpecialCreateActivity.this);

                    }

                    @Override
                    public void onProcessing(int progress) {

                    }

                    @Override
                    public void onFailed(Exception e) {
                        dialog.dismiss();
                       HttpFileUtils.toastUpFileFailed(SpecialCreateActivity.this);

                    }
                });
            }
        });

    }
//    processId:
//    saveType: 1
//    contractId: tp-20220405-201347
//    createUserName: 王涛
//    createUser: 13627679767
//    projectList: sjxm-20200602-120615
//    feeDept:
//    fee: 0.01
//    feeType: sc
//    contractReason: 特批流程测试
//    remark:
//{"total":0,"code":"","data":null,"success":true,"rows":null,"message":"操作成功"}

    private void createSpecialProcess(SpecialRequestModel model,InitialData data, ActivitySpecialCreateBinding binding) {
        long currentTime = System.currentTimeMillis();
        if(Utils.isFastClick(lastSubmitTime, currentTime, 1000)) {
            return;
        }


        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("processId", "");
        requestParam.put("saveType", "1");
        requestParam.put("contractId", model.getTicketId());//TODO
        requestParam.put("createUserName", data.getUsercaption());//TODO
        requestParam.put("createUser", data.getUsername());//TODO
        requestParam.put("projectList", projectListId);
        requestParam.put("feeDept", defaultFeeDept);
        //requestParam.put("feeType", "rc");
        requestParam.put("fee", binding.specialFee.getEditValue());
        requestParam.put("contractReason", binding.specialReason.getEditValue());


        //申请类型
        final int requestPosition = binding.requestType.getCurrentPosition() - 1;
        if (requestPosition >= 0) {
            ItemType itemType = itemTypeList.get(requestPosition);
            requestParam.put("feeType", itemType.getTypeId());
        } else {
            Toast.makeText(SpecialCreateActivity.this,
                    getString(R.string.select_hint, getString(R.string.request_type)), Toast.LENGTH_LONG).show();
            return;
        }
        String url;
        if (Utils.Initiator.equals(nowNode)) {
            url = HttpUtils.SPECIAL_PROCESS_EDIT;
            requestParam.put("nowNode", nowNode);
        } else {
            url = HttpUtils.SPECIAL_PROCESS_CREATE;
        }

        final AlertDialog dialog = Utils.getDialog(this, R.string.dealing);
        dialog.show();
        HttpUtils.post(url, requestParam, new HttpUtils.XinResponseListener(){
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Gson gson = new Gson();
                TPResponse tpResponse = gson.fromJson(response, TPResponse.class);
                Log.i(TAG, "response：" + response);
                boolean success = tpResponse.isSuccess();
                if (success) {
                    Toast.makeText(SpecialCreateActivity.this, R.string.tp_request_success, Toast.LENGTH_LONG).show();
                    SpecialCreateActivity.this.finish();
                } else {
                    Toast.makeText(SpecialCreateActivity.this, R.string.tp_request_fail, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String response) {
                dialog.dismiss();
            }
        });
    }
}