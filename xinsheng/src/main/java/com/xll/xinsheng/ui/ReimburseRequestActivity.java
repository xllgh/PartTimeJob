package com.xll.xinsheng.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.AdapterViewBindingAdapter;

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

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityReimburseRequestBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.AttachmentItemAdapter;
import com.xll.xinsheng.adapter.ReimburseFeeDetailAdapter;
import com.xll.xinsheng.bean.FileInfo;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.InvoiceType;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.bean.OrderDetailItem;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.bean.Project;
import com.xll.xinsheng.bean.ReimburseResponse;
import com.xll.xinsheng.bean.UserInfo;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.ItemReimburseFee;
import com.xll.xinsheng.model.ReimburseRequestModel;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.HttpFileUtils;
import com.xll.xinsheng.tools.Uri2PathUtil;
import com.xll.xinsheng.tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReimburseRequestActivity extends XinActivity {

    private String projectListId;
    private String projectId;
    private static String TAG = "ReimburseRequestActivity";
    private static final int OPEN_FILE_REQUEST_CODE_REIMBURSE = 121;
    private String nowNode = "-1";
    private static long lastSubmitTime;

    private final ReimburseRequestModel model = new ReimburseRequestModel();

    private final List<FileInfo> fileList = new ArrayList<>();
    private AttachmentItemAdapter attachmentItemAdapter;
    List<ItemReimburseFee> itemReimburseFeeList = new ArrayList<>();
    private InitialData data;
    private ReimburseFeeDetailAdapter adapter ;
    private ActivityReimburseRequestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        Utils.applyPermission(ReimburseRequestActivity.this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reimburse_request);
        binding.setModel(model);

        attachmentItemAdapter = new AttachmentItemAdapter(this, fileList);
        binding.setAttachmentAdapter(attachmentItemAdapter);

        adapter = new ReimburseFeeDetailAdapter(this, itemReimburseFeeList);
        binding.setAdapter(adapter);

        data = new Cache(this, Cache.INITIAL_DATA).getInitialData();
        final List<Project> projectList = data.getProjectList();

        initData(binding, projectList);
        setListener(binding, projectList);
        setSubmitListener(binding, data, adapter);

    }

    private void setListener(ActivityReimburseRequestBinding binding, final List<Project> projectList) {

        binding.setOpenFileListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, OPEN_FILE_REQUEST_CODE_REIMBURSE);
            }
        });

        binding.setAddListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.appendItem(createReimburseFee(data, null));
            }
        });

        binding.projectType.setOnItemSelectListener(new AdapterViewBindingAdapter.OnItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "projectType onItemSelected:" + position);
                if (position < projectList.size()) {
                    projectListId = projectList.get(position).getProjectId();
                    initInvoiceType(data, projectListId, adapter);
                } else {
                    projectListId = null;
                    projectId = null;
                }
                adapter.setProjectId(projectListId);
            }
        });
        binding.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReimburseRequestActivity.this.finish();
            }
        });
    }

    private void initProjectInfo(List<Project> projectList) {
        if (projectList != null && projectList.size() > 0) {
            String[] projectArray = new String[projectList.size()];
            int i = 0;
            for (Project project : projectList) {
                projectArray[i++] = project.getProjectName();
            }
            model.setProjectEntries(projectArray);
        }
    }

    private void initData(ActivityReimburseRequestBinding binding, List<Project> projectList) {
        Log.i(TAG, "initData");
        initProjectInfo(projectList);

        Intent intent = getIntent();
        String PendingDetailInfo = intent.getStringExtra("PendingDetailInfo");

        if (!TextUtils.isEmpty(PendingDetailInfo)) {
            PendingDetailInfo info = new Gson().fromJson(PendingDetailInfo, PendingDetailInfo.class);

            //设置订单号
            model.setTicketId(info.getOrderId());
            projectId = info.getOrderId();

            List<PaymentItem> paymentList = info.getPaymentList();
            for (PaymentItem item : paymentList) {
                //设置报销原因
                model.setReimburseReason(item.getBx_desc());
                nowNode = item.getNow_node();
                String projectName = item.getProject_name();
                if (!TextUtils.isEmpty(projectName)) {
                    for (int i = 0; i < projectList.size(); i++) {
                        Project project = projectList.get(i);
                        //设置项目初始位置
                        if (projectName.equals(project.getProjectName())) {
                            binding.projectType.setCurrentPosition(i);
                            break;
                        }
                    }
                }
                break;
            }

            //附件文件
            List<FileInfo> fileList = info.getFileList();
            Log.i(TAG, "fileList:" + fileList);

            if (fileList != null) {
                for (FileInfo fileInfo : fileList) {
                    attachmentItemAdapter.addAttachmentItem(fileInfo);
                }
            }
            //费用明细
            List<OrderDetailItem> detailList = info.getDetailList();
            itemReimburseFeeList.clear();
            if (detailList != null) {
                for (OrderDetailItem item : detailList) {
                    itemReimburseFeeList.add(createReimburseFee(data, item));
                }
            }
        } else {
            projectId = Utils.getProjectId();
            model.setTicketId(projectId);
            itemReimburseFeeList.add(createReimburseFee(data, null));
        }
        //adapter.appendItems(itemReimburseFeeList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE_REQUEST_CODE_REIMBURSE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (Uri2PathUtil.getRealPathFromUri(this, uri) != null) {
                //从uri得到绝对路径，并获取到file文件
                String filePath = Uri2PathUtil.getRealPathFromUri(this, uri);
                if (!TextUtils.isEmpty(filePath)) {
                    model.setUploadFileName(filePath.substring(filePath.lastIndexOf("/")));
                } else {
                    Toast.makeText(ReimburseRequestActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
                }
                //Log.e(TAG, filePath);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(filePath);
                attachmentItemAdapter.addAttachmentItem(fileInfo);
                uploadFile(filePath);
            } else {
                Toast.makeText(ReimburseRequestActivity.this, R.string.obtain_fail_hint, Toast.LENGTH_LONG).show();
                Log.e(TAG, "获取文件失败");
            }
        }
    }

    private void uploadFile(String filePath) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            Toast.makeText(ReimburseRequestActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
            return;
        }
        final HashMap<String, Object> param = new HashMap<>();
        //param.put("businessId", projectListId);
        param.put("myfile", new File(filePath));
        param.put("businessId", projectId);
        final ProgressDialog dialog = new ProgressDialog(ReimburseRequestActivity.this);
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
                        HttpFileUtils.toastUpFileSuccess(ReimburseRequestActivity.this);
                    }

                    @Override
                    public void onProcessing(int progress) {

                    }

                    @Override
                    public void onFailed(Exception e) {
                        dialog.dismiss();
                        HttpFileUtils.toastUpFileFailed(ReimburseRequestActivity.this);
                    }
                });
            }
        });

    }


    private ItemReimburseFee createReimburseFee(@NonNull InitialData data, OrderDetailItem item) {
        ItemReimburseFee fee0 = new ItemReimburseFee();
        String dx = null;
        if (item != null) {
            dx = item.getBxDx();
            Log.i(TAG, "费用类型：" + dx);
            fee0.setFeeTypeList(data.getItemTypeList());
            fee0.setReimburseLimitFee(item.getFee());
            fee0.setReimburseFee(item.getBxFee());
            fee0.setFpCount(item.getFpCount() + "");
            fee0.setRemarks(item.getRemark());
            fee0.setInvoiceType(getCacheInvoiceType(item.getBxProject()+item.getBxDx()));
            fee0.setReimburseDate(item.getBxDate());
            //发票科目
            fee0.setInvoiceSubject(item.getBxXx());
        }

        if (data.getItemTypeList() != null) {
            final List<ItemType> itemTypeList = new ArrayList<>();
            List<String> itemTypeNames = new ArrayList<>();
            Log.i(TAG, "ItemTypeList:" + data.getItemTypeList() + " dx:" + dx);
            for (ItemType itemType : data.getItemTypeList()) {
                String typeId = itemType.getTypeId();
                if (!TextUtils.isEmpty(typeId) && typeId.equals(dx)) {
                    itemTypeNames.add(0, itemType.getTypeName());
                    itemTypeList.add(0, itemType);
                } else {
                    itemTypeNames.add(itemType.getTypeName());
                    itemTypeList.add(itemType);
                }
            }
            fee0.setItemTypeName(itemTypeNames.toArray(new String[0]));
            fee0.setFeeTypeList(itemTypeList);
        }
        Log.i(TAG, "createReimburseFee" + fee0.toString());
        return fee0;
    }


    private boolean textIsEmpty(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, getString(R.string.edit_hint, hint), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private HashMap<String, List<InvoiceType.InvoiceItem>> invoiceTypeMap = new HashMap<>();

    private InvoiceType getCacheInvoiceType(String key) {
        final Cache<InvoiceType> cache = new Cache<>(ReimburseRequestActivity.this, Cache.KMXX_INFO);
        final InvoiceType info = cache.getInvoiceType(key);
        return info;
    }

    private void initInvoiceType(InitialData data,@NonNull final String projectListId, final ReimburseFeeDetailAdapter adapter) {
        Log.i(TAG, "initInvoiceType：" + itemReimburseFeeList);
        List<ItemType> typeList = data.getItemTypeList();
        for (final ItemType item : typeList) {
            final InvoiceType info = getCacheInvoiceType(projectListId+item.getTypeId()) ;
            if(!TextUtils.isEmpty(projectListId) && info != null && projectListId.equals(info.getProjectListId())) {
                for(ItemReimburseFee fee : itemReimburseFeeList) {
                    fee.setInvoiceType(info);
                }
                adapter.appendItems(itemReimburseFeeList);
                Log.i(TAG, "load cache InvoiceType");
                return;
            }

            final HashMap<String, String> requestMap = new HashMap<>();
            requestMap.put("id", item.getTypeId());
            requestMap.put("projectId", projectListId);
            HttpUtils.post(HttpUtils.INVOICE_TYPE, requestMap, new HttpUtils.XinResponseListener() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    InvoiceType invoiceType = gson.fromJson(response, InvoiceType.class);
                    invoiceType.setProjectListId(projectListId);

                    new Cache<>(ReimburseRequestActivity.this, Cache.KMXX_INFO).saveInvoiceType(projectListId+item.getTypeId(),new Gson().toJson(invoiceType));
                    for(ItemReimburseFee fee : itemReimburseFeeList) {
                        fee.setInvoiceType(invoiceType);
                    }
                    adapter.appendItems(itemReimburseFeeList);
                }

                @Override
                public void onError(String response) {

                }
            });
        }
    }


    private void setSubmitListener(final ActivityReimburseRequestBinding binding, final InitialData data, final ReimburseFeeDetailAdapter adapter) {
        binding.setSubmitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                if (Utils.isFastClick(lastSubmitTime, currentTime, 1000)) {
                    return;
                }
                lastSubmitTime = currentTime;

                HashMap<String, String> requestParam = new HashMap<>();
                requestParam.put("saveType", "1");
                requestParam.put("projectId", projectId);
                requestParam.put("projectCreateUser", data.getUsercaption());
                requestParam.put("createUser", data.getUsername());
                if (data.getUserInfoList().size() > 0) {
                    UserInfo userInfo = data.getUserInfoList().get(0);
                    requestParam.put("dept", userInfo.getOrgName());
                    requestParam.put("deptId", userInfo.getOrgid());
                    requestParam.put("feeDept", userInfo.getOrgName());
                }
                requestParam.put("projectList", projectListId);
                requestParam.put("bxFee", "0");//TODO
                requestParam.put("loanId", "root");//TODO
                if (textIsEmpty(binding.reimburseReason.getEditValue(), getString(R.string.reimburse_reason))) {
                    return;
                }
                requestParam.put("paymentReason", binding.reimburseReason.getEditValue());//TODO

                float totalFee = 0;
                int index = 0;
                List<ItemReimburseFee> itemReimburseFeeList = adapter.getData();
                if (itemReimburseFeeList == null || itemReimburseFeeList.size() == 0) {
                    Toast.makeText(ReimburseRequestActivity.this, R.string.hint_insert_fee_detail, Toast.LENGTH_LONG).show();
                    return;
                }


                for (int i = 0; i < itemReimburseFeeList.size(); i++) {
                    index = i + 1;
                    ItemReimburseFee itemReimburseFee = itemReimburseFeeList.get(i);
                    int itemPos = itemReimburseFee.getItemTypePos();
                    int invoicePos = itemReimburseFee.getInvoiceTypePos();
                    final List<ItemType> itemTypeList = data.getItemTypeList();
                    //Log.e(TAG, "itemPos:" + itemPos + ":" + itemTypeList.toString());

                    if (itemReimburseFee.getFeeTypeList() != null && itemTypeList.size() > itemPos && itemPos >= 0) {
                        ItemType itemType = itemTypeList.get(itemPos);
                        requestParam.put("kmdx" + index, itemType.getTypeId());
                    }
                    List<InvoiceType.InvoiceItem> invoiceItemList;
                    if ((invoiceItemList = itemReimburseFee.getInvoiceItemList()) != null && invoiceItemList.size() > invoicePos && invoicePos >= 0) {
                        InvoiceType.InvoiceItem invoiceItem = invoiceItemList.get(invoicePos);
                        requestParam.put("kmxx" + index, invoiceItem.getItemId());
                        requestParam.put("bxed" + index, invoiceItem.getFees());
                    }
                    //Log.e(TAG, "invoicePos:" + invoicePos + ":" + (invoiceItemList != null ? invoiceItemList.toString() : null));


                    totalFee += Utils.parseToFloat(itemReimburseFee.getReimburseFee());
                    if (textIsEmpty(requestParam.get("kmdx" + index), getString(R.string.fee_type))) {
                        return;
                    }
                    if (textIsEmpty(requestParam.get("kmxx" + index), getString(R.string.invoice_subject))) {
                        return;
                    }
                    if (textIsEmpty(itemReimburseFee.getReimburseFee(), getString(R.string.reimbursement_amount))) {
                        return;
                    }
               /*     if (!textIsNum(itemReimburseFee.getReimburseFee(), getString(R.string.reimbursement_amount))) {
                        return;
                    }*/

                    if (textIsEmpty(itemReimburseFee.getFpCount(), getString(R.string.invoice_amount))) {
                        return;
                    }
                    if (textIsEmpty(itemReimburseFee.getReimburseDate(), getString(R.string.happen_date))) {
                        return;
                    }

                    requestParam.put("fee" + index, itemReimburseFee.getReimburseFee());
                    requestParam.put("time" + index, itemReimburseFee.getReimburseDate());
                    requestParam.put("fp" + index, itemReimburseFee.getFpCount());
                    requestParam.put("remark" + index, itemReimburseFee.getRemarks());
                }
                requestParam.put("feeV3", String.valueOf(totalFee));
                requestParam.put("feeV1", String.valueOf(totalFee));
                requestParam.put("rownum", String.valueOf(index));
                requestParam.put("remark", "app remark");

                String url;
                if(Utils.Initiator.equals(nowNode)) {
                    url = HttpUtils.REIMBURSE_DEAL_EDIT;
                } else {
                    url = HttpUtils.REIMBURSE;
                }

                final AlertDialog dialog = Utils.getDialog(ReimburseRequestActivity.this, R.string.dealing);
                dialog.show();
                HttpUtils.post(url, requestParam, new HttpUtils.XinResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        //Log.i(TAG, "REIMBURSE:" + response);
                        Gson gson = new Gson();
                        ReimburseResponse reimburseResponse = gson.fromJson(response, ReimburseResponse.class);
                        boolean success = reimburseResponse.isSuccess();
                        if (success) {
                            Toast.makeText(ReimburseRequestActivity.this, R.string.reimburse_request_success, Toast.LENGTH_LONG).show();
                            ReimburseRequestActivity.this.finish();
                        } else {
                            Toast.makeText(ReimburseRequestActivity.this, R.string.reimburse_request_fail, Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onError(String response) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
}