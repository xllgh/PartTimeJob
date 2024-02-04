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
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.AdapterViewBindingAdapter;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityLoanPayBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.AttachmentItemAdapter;
import com.xll.xinsheng.bean.BankType;
import com.xll.xinsheng.bean.FileInfo;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.bean.LoanPayResponse;
import com.xll.xinsheng.bean.OrderDetailItem;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.bean.Project;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.LoanPayModel;
import com.xll.xinsheng.myview.CompoEditView;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.HttpFileUtils;
import com.xll.xinsheng.tools.Uri2PathUtil;
import com.xll.xinsheng.tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoanPayRequestActivity extends XinActivity {

    public static final int OPEN_FILE_REQUEST_CODE = 120;
    private static final HashMap<String, String> map = new HashMap<>();

    final LoanPayModel model = new LoanPayModel();
    private String createUser;
    private ActivityLoanPayBinding binding;
    private static final String TAG = "LoanPayRequestActivity";
    private static long lastSubmitTime;

    private String contractId;
   private AttachmentItemAdapter adapter;
    private String project_name;
    private String fee_type;
    private String process_type;
    private String bank_id;
    private String bx_desc;
    private float bx_fee;
    private String itemTypeName;
    private String orderId;
    private String now_node = "-1";
    private String feeDept;
    private List<String> projectNames = new ArrayList<>();
    private List<String> itemTypeNames = new ArrayList<>();
    private List<String> processNames = new ArrayList<>();
    private List<String> bankNames = new ArrayList<>();
    private List<FileInfo> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.applyPermission(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loan_pay);
        adapter = new AttachmentItemAdapter(LoanPayRequestActivity.this, fileList);
        binding.setAttachmentAdapter(adapter);
        getIntentInfo();

        map.put("1", getString(R.string.loan_process));
        map.put("2", getString(R.string.pay_process));


        binding.requestMoney.setOnEditChangeListener(new CompoEditView.OnEditChangeListener() {
            @Override
            public void onEditChange(String str) {
                model.setRequestMoneyBig(Utils.digitUppercase(Double.parseDouble(str)));
            }
        });


        Cache cache = new Cache(this, Cache.INITIAL_DATA);
        final InitialData data = cache.getInitialData();
        if (data != null) {
            //项目类型
            model.setUserName(data.getUsercaption());
            createUser = data.getUsername();
            final List<Project> projectList = new ArrayList<>();
            //项目信息
            if (data.getProjectList() != null) {
                for (Project project : data.getProjectList()) {
                    String name =  project.getProjectName();
                    if (!TextUtils.isEmpty(name) && name.equals(project_name)) {
                        projectNames.add(0, name);
                        projectList.add(0, project);
                    } else {
                        projectNames.add(name);
                        projectList.add(project);
                    }
                }
                model.setProjectEntries(projectNames.toArray(new String[0]));
            }

            //申请类型
            final List<ItemType> itemTypeList = new ArrayList<>();
            if (data.getItemTypeList() != null) {
                for (ItemType itemType : data.getItemTypeList()) {
                    String name = itemType.getTypeName();
                    if(!TextUtils.isEmpty(name) && name.equals(itemTypeName)) {
                        //itemTypeNames.remove(0);
                        itemTypeNames.add(0, name);
                        itemTypeList.add(0, itemType);
                    } else {
                        itemTypeNames.add(name);
                        itemTypeList.add(itemType);
                    }
                }
                model.setRequestEntries(itemTypeNames.toArray(new String[0]));
            }

            //流程类型
            if (!map.isEmpty()) {
                final Set<Map.Entry<String, String>> entries = map.entrySet();
                Iterator<Map.Entry<String, String>> iterator = entries.iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<String, String> next = iterator.next();
                    String key = next.getKey();
                    if (!TextUtils.isEmpty(key) && key.equals(process_type)) {
                        processNames.add(0, next.getValue());
                    } else {
                        processNames.add(next.getValue());
                    }
                }
                if (Utils.Initiator.equals(now_node) ) {
                    binding.processType.setClickable(false);
                    binding.processType.setEnabled(false);
                }
                model.setProcessEntries(processNames.toArray(new String[0]));
            }

            //银行类型
            final List<BankType> bankTypeList = new ArrayList<>();
            if (data.getBankList() != null ) {
                for (BankType bankType : data.getBankList()) {
                    String bankId = bankType.getBank_id();
                    if (!TextUtils.isEmpty(bankId) && bankId.equals(bank_id)) {
                        bankNames.add(0, bankType.getBank_name());
                        bankTypeList.add(0, bankType);
                    } else {
                        bankNames.add(bankType.getBank_name());
                        bankTypeList.add(bankType);
                    }
                }
                model.setBankEntries(bankNames.toArray(new String[0]));
            }
            binding.setModel(model);
            binding.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoanPayRequestActivity.this.finish();
                }
            });
            binding.setSubmitListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long currentTime = System.currentTimeMillis();
                    if (Utils.isFastClick(lastSubmitTime, currentTime, 1000)) {
                        return;
                    }
                    lastSubmitTime = currentTime;


                    //流程类别
                    if (textIsEmpty(contractId, getString(R.string.process_type))) {
                        return;
                    }
                    if (textIsEmpty(binding.requestMoney.getEditValue(), getString(R.string.request_money))) {
                        return;
                    }
                    if (textIsEmpty( binding.bankName.getEditValue(), getString(R.string.bank_name))) {
                        return;
                    }
                    if (textIsEmpty(binding.bankAccount.getEditValue(), getString(R.string.bank_account))) {
                        return;
                    }
                    if (textIsEmpty(binding.requestReason.getEditValue(), getString(R.string.request_reason))) {
                        return;
                    }

                    HashMap<String, String> param = new HashMap<>();

                    //申请项目
                    Project project = projectList.get(binding.requestProject.getCurrentPosition());
                    param.put("projectList", project.getProjectId());

                    //申请类型
                    ItemType itemType = itemTypeList.get(binding.requestType.getCurrentPosition());
                    param.put("feeType", itemType.getTypeId());

                    //流程类别
                    param.put("contractId", model.getTicketId());

                    //开户行
                    param.put("bankName", bankTypeList.get(binding.bankType.getCurrentPosition()).getBank_id());
                    param.put("createUserName", model.getUserName());
                    param.put("createUser", createUser);

                    param.put("fee", binding.requestMoney.getEditValue());
                    param.put("feeBig", model.getRequestMoneyBig());

                    if(getString(R.string.loan_process).equals(processNames.get(binding.processType.getCurrentPosition()))) {
                        param.put("processType", "1");
                    } else {
                        param.put("processType", "2");
                    }
                    param.put("bankUserId", binding.bankName.getEditValue());
                    param.put("bankUserName", binding.bankAccount.getEditValue());
                    param.put("bankAttr", binding.bankNetwork.getEditValue());
                    param.put("contractReason", binding.requestReason.getEditValue());

                    String url;

                    if (Utils.Initiator.equals(now_node)) {
                        url = HttpUtils.PAY_LOAN_EDIT;
                        param.put("nowNode", "0");
                        param.put("feeDept",feeDept );
                    } else {
                        url = HttpUtils.PAY_LOAN_REQUEST;
                        param.put("processId", "");
                    }
                    param.put("saveType", "1");
                    param.put("remark", "App客户端发起");

                    final AlertDialog dialog = Utils.getDialog(LoanPayRequestActivity.this, R.string.dealing);
                    dialog.show();
                    HttpUtils.post(url, param, new HttpUtils.XinResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();
                            Log.i(TAG, response + "");
                            Gson gson = new Gson();
                            if (response != null) {
                                LoanPayResponse result = gson.fromJson(response, LoanPayResponse.class);
                                if (result != null) {
                                    String msg = result.getMessage();
                                    if (!TextUtils.isEmpty(msg)) {
                                        Toast.makeText(LoanPayRequestActivity.this, msg, Toast.LENGTH_LONG).show();
                                        LoanPayRequestActivity.this.finish();
                                        return;
                                    }
                                }
                            }
                            Toast.makeText(LoanPayRequestActivity.this, R.string.submit_process_error, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(String response) {
                            dialog.dismiss();
                        }
                    });


                }
            });

        }

        binding.processType.setOnItemSelectListener(new AdapterViewBindingAdapter.OnItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected:" + position);
                //编辑节点信息，不需要再次生成订单号
                if(Utils.Initiator.equals(now_node)){
                    return;
                }
                adapter.removeAllAttachment();
                binding.processType.setCurrentPosition(position);
                contractId = getContractId(position);
                model.setTicketId(contractId);
            }
        });

        binding.setOpenFileListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });


        binding.setSubmitFileListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
                    Toast.makeText(LoanPayRequestActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
                    return;
                }

                final HashMap<String , Object> param = new HashMap<>();
                if (textIsEmpty(model.getTicketId(), getString(R.string.process_type))) {
                    return;
                }
                param.put("businessId", model.getTicketId());
                param.put("myfile", new File(filePath));
                final ProgressDialog dialog = new ProgressDialog(LoanPayRequestActivity.this);
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
                                HttpFileUtils.toastUpFileSuccess(LoanPayRequestActivity.this);

                            }

                            @Override
                            public void onProcessing(int progress) {
                            }

                            @Override
                            public void onFailed(Exception e) {
                                dialog.dismiss();
                                HttpFileUtils.toastUpFileFailed(LoanPayRequestActivity.this);
                            }
                        });
                    }
                });
            }
        });
    }

    private void getIntentInfo() {
        Intent intent = getIntent();
        String PendingDetailInfo = intent.getStringExtra("PendingDetailInfo");
        if(!TextUtils.isEmpty(PendingDetailInfo)) {
           com.xll.xinsheng.bean.PendingDetailInfo info = new Gson().fromJson(PendingDetailInfo, PendingDetailInfo.class);

            orderId = info.getOrderId();
            model.setTicketId(orderId);
            contractId = orderId;
            final List<PaymentItem> paymentList = info.getPaymentList();
            for(PaymentItem item : paymentList) {
                Log.e(TAG, "PaymentItem" + item);
                project_name = item.getProject_name();
                itemTypeName = item.getItem_type_name();
                fee_type = item.getFee_type();
                process_type = item.getProcess_type();// TODO 开户行
                bank_id = item.getBank_id();
                bx_desc = item.getBx_desc();
                bx_fee = item.getBx_fee();
                feeDept = item.getFee_dept();

                now_node = item.getNow_node();

                model.setRequestMoney(item.getFee() + "");
                model.setBankName(item.getBank_userid());
                model.setBankAccount(item.getBank_username());
                model.setBankNetwork(item.getBank_attr());
                model.setRequestReason(item.getSeal_desc());

            }
            //附件信息
            final List<FileInfo> fileList = info.getFileList();
            if (fileList != null) {
                for (FileInfo fileInfo : fileList) {
                    adapter.addAttachmentItem(fileInfo);
                }
            }

            final List<OrderDetailItem> detailList = info.getDetailList();
            if(detailList!=null) {
                for(OrderDetailItem item : detailList) {
                    String bxFee = item.getBxFee();
                    String bxUser = item.getBxUser();
                   // item.getbx
                }
            }

        }
    }

    private void submitFile() {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            Toast.makeText(LoanPayRequestActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
            return;
        }

        final HashMap<String , Object> param = new HashMap<>();
        if (textIsEmpty(contractId, getString(R.string.process_type))) {
            return;
        }
        param.put("businessId", contractId);
        param.put("myfile", new File(filePath));
        final ProgressDialog dialog = new ProgressDialog(LoanPayRequestActivity.this);
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
                        HttpFileUtils.toastUpFileSuccess(LoanPayRequestActivity.this);
                    }

                    @Override
                    public void onProcessing(int progress) {
                    }

                    @Override
                    public void onFailed(Exception e) {
                        dialog.dismiss();
                        HttpFileUtils.toastUpFileFailed(LoanPayRequestActivity.this);
                        // Toast.makeText(LoanPayRequestActivity.this, R.string.upload_file_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private boolean textIsEmpty(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, getString(R.string.edit_hint,hint), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }



    private String getContractId(int position) {
        String[] processes = model.getProcessEntries();
        String value = null;
        if (position >= 0 && position < processes.length) {
            value= processes[position];
        }
        if (getString(R.string.pay_process).equals(value)) {
            return Utils.generateId("zf-");
        } else {
            return Utils.generateId("jk-");
        }
    }

    String filePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == OPEN_FILE_REQUEST_CODE && resultCode == RESULT_OK&& data !=  null) {
            Uri uri = data.getData();
            if (Uri2PathUtil.getRealPathFromUri(this, uri) != null) {
                //从uri得到绝对路径，并获取到file文件
                filePath = Uri2PathUtil.getRealPathFromUri(this, uri);
                if (!TextUtils.isEmpty(filePath)) {
                    model.setUploadFileName(filePath.substring(filePath.lastIndexOf("/")));
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(filePath);
                    adapter.addAttachmentItem(fileInfo);
                    adapter.notifyDataSetChanged();
                    submitFile();
                } else {
                    Toast.makeText(LoanPayRequestActivity.this, R.string.file_hint, Toast.LENGTH_LONG).show();
                }
                Log.e(TAG, filePath);
            } else {
                Toast.makeText(LoanPayRequestActivity.this, R.string.obtain_fail_hint, Toast.LENGTH_LONG).show();
                Log.e(TAG, "获取文件失败");
            }
        }
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, OPEN_FILE_REQUEST_CODE);
    }

}
