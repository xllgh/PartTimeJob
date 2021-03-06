package com.xll.xinsheng.ui;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

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
import com.xll.xinsheng.adapter.ReimburseDetailAdapter;
import com.xll.xinsheng.adapter.ReimburseFeeDetailAdapter;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.InvoiceType;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.bean.Project;
import com.xll.xinsheng.bean.ReimburseResponse;
import com.xll.xinsheng.bean.UserInfo;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.ItemReimburseFee;
import com.xll.xinsheng.model.ReimburseRequestModel;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.OkHttpUtils;
import com.xll.xinsheng.tools.Uri2PathUtil;
import com.xll.xinsheng.tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReimburseRequestActivity extends XinActivity {

    List<ItemReimburseFee> itemReimburseFeeList = new ArrayList<>();
    private String projectListId;
    private String projectId;
    private static String TAG = "ReimburseRequestActivity";
    private static final int OPEN_FILE_REQUEST_CODE_REIMBURSE = 121;
    private final ReimburseRequestModel model = new ReimburseRequestModel();

    private final List<String> fileList = new ArrayList<>();
    private AttachmentItemAdapter attachmentItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReimburseRequestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_reimburse_request);
        attachmentItemAdapter = new AttachmentItemAdapter(this, fileList);
        binding.setAttachmentAdapter(attachmentItemAdapter);
        selectFile(binding);
        Utils.applyPermission(ReimburseRequestActivity.this);


        Cache cache = new Cache(this, Cache.INITIAL_DATA);
        final InitialData data = cache.getInitialData();
        projectId = Utils.getProjectId();
        final List<Project> projectList = data.getProjectList();
        if (projectList != null && projectList.size()>0) {
            String[] projectArray = new String[projectList.size()];
            int i = 0;
            for (Project project : projectList) {
                projectArray[i++] = project.getProjectName();
            }
            model.setProjectEntries(projectArray);
        }
        itemReimburseFeeList.add(createReimburseFee(data));

        final ReimburseFeeDetailAdapter adapter = new ReimburseFeeDetailAdapter(this, itemReimburseFeeList);
        binding.setAdapter(adapter);
        binding.setAddListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.appendItem(createReimburseFee(data), invoiceTypeMap);
            }
        });

        binding.setModel(model);
        binding.projectType.setOnSelectItemListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < projectList.size()) {
                    projectListId = projectList.get(i).getProjectId();
                    initInvoiceType(data, projectListId, adapter);
                } else {
                    projectListId = null;
                    projectId = null;
                }
                adapter.setProjectId(projectListId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                projectListId = null;
                projectId = null;
                adapter.setProjectId(null);
            }
        });
        submitRequest(binding, data, adapter);
        binding.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReimburseRequestActivity.this.finish();
            }
        });
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
                Log.e(TAG, filePath);
                attachmentItemAdapter.addAttachmentItem(filePath);
                uploadFile(filePath);
            } else {
                Toast.makeText(ReimburseRequestActivity.this, R.string.obtain_fail_hint, Toast.LENGTH_LONG).show();
                Log.e(TAG, "获取文件失败");
            }
        }
    }

    private void selectFile(ActivityReimburseRequestBinding binding) {
        binding.setOpenFileListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, OPEN_FILE_REQUEST_CODE_REIMBURSE);
            }
        });
    }

    private void uploadFile(String filePath) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            Toast.makeText(ReimburseRequestActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
            return;
        }
        final HashMap<String, Object> param = new HashMap<>();
        param.put("businessId", projectListId);
        param.put("myfile", new File(filePath));
        param.put("businessId", projectId);
        final ProgressDialog dialog = new ProgressDialog(ReimburseRequestActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle(R.string.uploading);
        dialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().upLoadFile(OkHttpUtils.UPLOAD_FILE_URL, param, new OkHttpUtils.OnProcessListener() {
                    @Override
                    public void onSuccess(File file, String str) {
                        Log.e(TAG, "onSuccess");
                        dialog.dismiss();
                    }

                    @Override
                    public void onProcessing(int progress) {

                    }

                    @Override
                    public void onFailed(Exception e) {
                        dialog.dismiss();
                        // Toast.makeText(LoanPayRequestActivity.this, R.string.upload_file_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private ItemReimburseFee createReimburseFee(InitialData data) {
        ItemReimburseFee fee0 = new ItemReimburseFee();
        if (data.getItemTypeList() != null) {
            final List<ItemType> itemTypeList = new ArrayList<>(data.getItemTypeList());
            String[] itemArray = new String[itemTypeList.size() + 1];
            int i = 0;
            itemArray[i++] = getString(R.string.select);
            for (ItemType itemType : itemTypeList) {
                itemArray[i++] = itemType.getTypeName();
            }
            fee0.setItemTypeName(itemArray);
            fee0.setFeeTypeList(data.getItemTypeList());
            fee0.setInvoiceTypeMap(invoiceTypeMap);
        }
        return fee0;
    }

    private boolean textIsNum(String str, String hint) {
        if (str.matches("^[0-9]+(.[0-9])?$")) {
            return true;
        } else {
            Toast.makeText(this, getString(R.string.edit_num, hint), Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private boolean textIsEmpty(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, getString(R.string.edit_hint, hint), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private HashMap<String, List<InvoiceType.InvoiceItem>> invoiceTypeMap = new HashMap<>();

    private void initInvoiceType(InitialData data, String projectId, final ReimburseFeeDetailAdapter adapter) {
        List<ItemType> typeList = data.getItemTypeList();
        for (ItemType item : typeList) {
            final HashMap<String, String> requestMap = new HashMap<>();
            requestMap.put("id", item.getTypeId());
            requestMap.put("projectId", projectId);
            HttpUtils.post(HttpUtils.INVOICE_TYPE, requestMap, new HttpUtils.XinResponseListener() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    InvoiceType invoiceType = gson.fromJson(response, InvoiceType.class);
                    invoiceTypeMap.put(requestMap.get("id"), invoiceType.getInvoiceItems());
                    Log.e(TAG, "invoiceType:" + invoiceType + "---requestMap:" + requestMap);
                    if (adapter != null) {
                        adapter.updateInvoiceType(invoiceTypeMap);
                    }
                }
            });
        }
    }


    private void submitRequest(final com.example.xinsheng.databinding.ActivityReimburseRequestBinding binding, final InitialData data, final ReimburseFeeDetailAdapter adapter) {
        binding.setSubmitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Log.e(TAG, "submit:" + itemReimburseFeeList.toString());

                for (int i = 0; i < itemReimburseFeeList.size(); i++) {
                    index = i + 1;
                    ItemReimburseFee itemReimburseFee = itemReimburseFeeList.get(i);
                    int itemPos = itemReimburseFee.getItemTypePos();
                    int invoicePos = itemReimburseFee.getInvoiceTypePos();
                    final List<ItemType> itemTypeList = data.getItemTypeList();
                    Log.e(TAG, "itemPos:" + itemPos + ":" + itemTypeList.toString());

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
                    Log.e(TAG, "invoicePos:" + invoicePos + ":" + (invoiceItemList != null ? invoiceItemList.toString() : null));


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

                HttpUtils.post(HttpUtils.REIMBURSE, requestParam, new HttpUtils.XinResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "REIMBURSE:" + response);
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
                });

            }
        });
    }
}