package com.xll.xinsheng.ui;

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

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ActivityLoanPayBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.adapter.AttachmentItemAdapter;
import com.xll.xinsheng.bean.BankType;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.bean.LoanPayResponse;
import com.xll.xinsheng.bean.Project;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.LoanPayModel;
import com.xll.xinsheng.myview.CompoEditView;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.OkHttpUtils;
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
    private static HashMap<String, String> map = new HashMap<>();

    final LoanPayModel model = new LoanPayModel();
    private String createUser;
    private ActivityLoanPayBinding binding;
    private static final String TAG = "LoanPayRequestActivity";

    private String contractId;
    private int processPos = 0;
   private AttachmentItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.applyPermission(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loan_pay);

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
            if (data.getProjectList() != null) {
                projectList.addAll(data.getProjectList());
                String[] projectArray = new String[projectList.size()];
                int i = 0;
                for (Project project : projectList) {
                    projectArray[i++] = project.getProjectName();
                }
                model.setProjectEntries(projectArray);
            }

            //申请类型
            final List<ItemType> itemTypeList = new ArrayList<>();
            if (data.getItemTypeList() != null) {
                itemTypeList.addAll(data.getItemTypeList());
                String[] itemArray = new String[itemTypeList.size() + 1];
                int i = 0;
                itemArray[i++] = getString(R.string.select);
                for (ItemType itemType : itemTypeList) {
                    itemArray[i++] = itemType.getTypeName();
                }
                model.setRequestEntries(itemArray);
            }

            //流程类型
            if (!map.isEmpty()) {
                final Set<Map.Entry<String, String>> entries = map.entrySet();
                String[] processType = new String[map.size() + 1];
                int i = 0;
                processType[i] = getString(R.string.select);
                Iterator<Map.Entry<String, String>> iterator = entries.iterator();
                while (iterator.hasNext()) {
                    processType[++i] = iterator.next().getValue();
                }
                model.setProcessEntries(processType);
            }


            //银行类型
            final List<BankType> bankTypeList = data.getBankList();
            if (bankTypeList != null && bankTypeList.size() > 0) {
                String[] itemArray = new String[bankTypeList.size() + 1];
                int i = 0;
                itemArray[i++] = getString(R.string.select);
                for (BankType bankType : bankTypeList) {
                    itemArray[i++] = bankType.getBank_name();
                }
                model.setBankEntries(itemArray);
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
                    HashMap<String, String> param = new HashMap<>();

                    //申请项目
                    int projectPosition = binding.requestProject.getCurrentPosition();
                    if (projectPosition < projectList.size() && projectPosition >= 0) {
                        Project project = projectList.get(projectPosition);
                        param.put("projectList", project.getProjectId());
                    } else {
                        Toast.makeText(LoanPayRequestActivity.this,
                                getString(R.string.select_hint, getString(R.string.request_project)), Toast.LENGTH_LONG).show();
                        return;
                    }

                    //申请类型
                    final int requestPosition = binding.requestType.getCurrentPosition() - 1;
                    if (requestPosition >= 0) {
                        ItemType itemType = itemTypeList.get(requestPosition);
                        param.put("feeType", itemType.getTypeId());
                    } else {
                        Toast.makeText(LoanPayRequestActivity.this,
                                getString(R.string.select_hint, getString(R.string.request_type)), Toast.LENGTH_LONG).show();
                        return;
                    }

                    //流程类别
                    if (textIsEmpty(contractId, getString(R.string.process_type))) {
                        return;
                    }
                    param.put("contractId", contractId);

                    //开户行
                    int bankType;
                    if ((bankType = binding.bankType.getCurrentPosition() -1) < 0) {
                        Toast.makeText(LoanPayRequestActivity.this,
                                getString(R.string.select_hint, getString(R.string.bank_type)), Toast.LENGTH_LONG).show();
                        return;
                    }
                    param.put("bankName", bankTypeList.get(bankType).getBank_id());

                    param.put("processId", "");
                    param.put("saveType", "1");
                    param.put("createUserName", model.getUserName());
                    param.put("createUser", createUser);
                    if (textIsEmpty(binding.requestMoney.getEditValue(), getString(R.string.request_money))) {
                        return;
                    }
                    param.put("fee", binding.requestMoney.getEditValue());

                    param.put("feeBig", model.getRequestMoneyBig());

                    if (processPos <0) {
                        Toast.makeText(LoanPayRequestActivity.this,
                                getString(R.string.select_hint, getString(R.string.process_type)), Toast.LENGTH_LONG).show();
                        return;
                    }
                    param.put("processType", processPos + "");




                    if (textIsEmpty( binding.bankName.getEditValue(), getString(R.string.bank_name))) {
                        return;
                    }
                    param.put("bankUserId", binding.bankName.getEditValue());


                    if (textIsEmpty( binding.bankAccount.getEditValue(), getString(R.string.bank_account))) {
                        return;
                    }
                    param.put("bankUserName", binding.bankAccount.getEditValue());



                    param.put("bankAttr", binding.bankNetwork.getEditValue());
                    if (textIsEmpty(binding.requestReason.getEditValue(), getString(R.string.request_reason))) {
                        return;
                    }
                    param.put("contractReason", binding.requestReason.getEditValue());
                    HttpUtils.post(HttpUtils.PAY_LOAN_REQUEST, param, new HttpUtils.XinResponseListener() {
                        @Override
                        public void onResponse(String response) {
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
                    });


                }
            });

        }
        binding.processType.setOnSelectItemListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contractId = getContractId(position);
                model.setTicketId(contractId);
                processPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.setOpenFileListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });


        adapter = new AttachmentItemAdapter(LoanPayRequestActivity.this, new ArrayList<String>());
        binding.setAttachmentAdapter(adapter);


        binding.setSubmitFileListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
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

    private boolean textIsEmpty(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, getString(R.string.edit_hint,hint), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }



    private String getContractId(int position) {
        String value = map.get(String.valueOf(position));
        String contractId;
        if (getString(R.string.pay_process).equals(value)) {
            contractId = Utils.generateId("zf-");
        } else if (getString(R.string.loan_process).equals(value)) {
            contractId = Utils.generateId("jk-");
        } else {
            contractId = "";
        }

        return contractId;
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
                    adapter.addAttachmentItem(filePath);
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
