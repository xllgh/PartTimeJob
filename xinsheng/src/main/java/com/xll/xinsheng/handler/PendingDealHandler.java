package com.xll.xinsheng.handler;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.xinsheng.R;
import com.google.gson.Gson;
import com.xll.xinsheng.bean.BankInfo;
import com.xll.xinsheng.bean.DealResponse;
import com.xll.xinsheng.bean.OrderDetailItem;
import com.xll.xinsheng.bean.PaymentItem;
import com.xll.xinsheng.bean.PendingDetailInfo;
import com.xll.xinsheng.model.PendingDealModel;
import com.xll.xinsheng.tools.HttpUtils;
import com.xll.xinsheng.tools.Utils;
import com.xll.xinsheng.ui.PendingActivity;

import java.util.HashMap;
import java.util.List;

import static com.xll.xinsheng.tools.Utils.PAY_LOAN;
import static com.xll.xinsheng.tools.Utils.REIMBURSEMENT;
import static com.xll.xinsheng.tools.Utils.TP;
import static com.xll.xinsheng.tools.Utils.UNKNOWN;

public class PendingDealHandler {

    private PendingDetailInfo info;

    private static final String TAG = "PendingDealHandler";


    public PendingDealHandler(PendingDetailInfo info) {
        this.info = info;
    }

    //TODO 用注解 代替 枚举
    //通过
    private static final int PASS = 2;

    //驳回至发起人
    private static final int DISMISS_INITIATOR = -2;

    //驳回至上一级
    private static final int DISMISS_LAST_NODE = -1;

    private void action(String orderId, Context context, PendingDealModel model, int action) {
        @Utils.ProcessType int type = Utils.getProcessType(orderId);
        switch (type) {
            case REIMBURSEMENT:
                reimburse(context, action, info, model.getComment());
                break;
            case PAY_LOAN:
                payAndLoan(context, action, info, model.getComment());
                break;
            case TP:
                tp(context, action, info, model.getComment());
                break;
            case UNKNOWN:
                Toast.makeText(context, R.string.unknown_process_type, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void onPassClick(Context context, PendingDealModel model) {
        action(info.getOrderId(), context, model, PASS);
    }

    public void onDismissLastNode(Context context, PendingDealModel model) {
        action(info.getOrderId(), context, model, DISMISS_LAST_NODE);
    }

    public void onDismissInitiator(Context context, PendingDealModel model) {
        action(info.getOrderId(), context, model, DISMISS_INITIATOR);
    }


    //报销
    private void reimburse(@NonNull final Context context, int type, @NonNull PendingDetailInfo info, String coment) {
        List<PaymentItem> paymentItems = info.getPaymentList();
        if (paymentItems != null && paymentItems.size() > 0) {
            HashMap<String, String> map = new HashMap<>();
            PaymentItem item = paymentItems.get(0);
            map.put("projectId", item.getOrderId());
            map.put("createUser", item.getName());
            map.put("paymentReason", item.getBx_desc());
            map.put("remark", TextUtils.isEmpty(coment) ? item.getRemark() : coment);
            map.put("nowNode", item.getNow_node());
            map.put("saveType", String.valueOf(type));
            map.put("projectList", item.getProject_id());

            List<OrderDetailItem> orderDetailItems = info.getDetailList();

            if (orderDetailItems != null) {
                for(int i = 0; i< orderDetailItems.size(); i++) {
                    String key = "fee" + (i+1);
                    String value = orderDetailItems.get(i).getBxFee();
                    map.put(key, value);
                }
                map.put("rownum", String.valueOf(orderDetailItems.size()));
            }

            HttpUtils.post(HttpUtils.REIMBURSE_DEAL, map, new HttpUtils.XinResponseListener() {
                @Override
                public void onResponse(String response) {
                    handleResponse(response, context);

                }
            });

        }
    }

    //特批
    private void tp(@NonNull final Context context, int type, @NonNull PendingDetailInfo info, String coment) {
        HashMap<String, String> map = new HashMap<>();
        List<PaymentItem> paymentItems = info.getPaymentList();
        if (paymentItems != null && paymentItems.size() > 0) {
            PaymentItem item = paymentItems.get(0);
            map.put("saveType", String.valueOf(type));
            map.put("nowNode", String.valueOf(item.getNow_node()));
            map.put("contractId", item.getOrderId());
            map.put("createUserName", item.getName());
            map.put("createUser", item.getUsername());
            map.put("projectList", item.getProject_id());
            map.put("feeDept", item.getFee_dept());
            map.put("fee", String.valueOf(item.getFee()));
            map.put("feeType", item.getFee_type());
            map.put("contractReason", item.getSeal_desc());
            map.put("remark", TextUtils.isEmpty(coment) ? item.getRemark() : coment);
            Log.i(TAG, "comment:" + coment);
            HttpUtils.post(HttpUtils.TP_PROCESS, map, new HttpUtils.XinResponseListener() {
                @Override
                public void onResponse(String response) {
                    handleResponse(response, context);
                }
            });
        }
     }

    //支付和借款
    private void payAndLoan(@NonNull final Context context, int type, @NonNull PendingDetailInfo info, String coment) {
        HashMap<String, String> map = new HashMap<>();
        List<PaymentItem> paymentItems = info.getPaymentList();
        if (paymentItems != null && paymentItems.size() > 0) {
            PaymentItem item = paymentItems.get(0);
            map.put("fee", String.valueOf(item.getFee()));
            map.put("contractId", item.getOrderId());
            map.put("createUser", item.getUsername());
            map.put("projectList", item.getProject_id());
            map.put("feeType", item.getFee_type());
            map.put("contractReason", item.getSeal_desc());
            map.put("remark", TextUtils.isEmpty(coment) ? item.getRemark() : coment);
            map.put("nowNode", item.getNow_node());
            map.put("saveType", String.valueOf(type));
            map.put("processType", item.getProcess_type());
            map.put("bankAttr", item.getBank_attr());
            Log.i(TAG, "comment:" + coment);

            List<BankInfo> bankInfos = info.getBankList();
            if (bankInfos != null) {
                for(BankInfo bankInfo : bankInfos) {
                    if(bankInfo.getBank_id().equals(item.getBank_id())) {
                        map.put("bankName", bankInfo.getBank_name());
                    }
                }
            }

            HttpUtils.post(HttpUtils.PAY_LOAN, map, new HttpUtils.XinResponseListener() {
                @Override
                public void onResponse(String response) {
                    handleResponse(response, context);
                }
            });
        }
    }

    private void handleResponse(String response, @NonNull Context context) {
        Log.i(TAG, "onResponse：" + response);
        Gson gson = new Gson();
        DealResponse dealResponse = gson.fromJson(response, DealResponse.class);
        if (dealResponse.isSuccess()) {
            Toast.makeText(context,R.string.handle_success, Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, PendingActivity.class));
        } else {
            Toast.makeText(context, R.string.handle_fail, Toast.LENGTH_LONG).show();
        }
    }
}
