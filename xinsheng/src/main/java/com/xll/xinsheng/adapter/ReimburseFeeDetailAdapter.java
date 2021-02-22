package com.xll.xinsheng.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemReimburseFeeBinding;
import com.google.gson.Gson;
import com.xll.xinsheng.bean.InvoiceType;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.model.ItemReimburseFee;
import com.xll.xinsheng.myview.CompoEditView;
import com.xll.xinsheng.tools.HttpUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReimburseFeeDetailAdapter extends RecyclerView.Adapter<ReimburseFeeDetailAdapter.ReimburseDetailHolder> {

    private final Context context;
    private final List<ItemReimburseFee> itemReimburseFees = new ArrayList<>();
    private String projectId;
    private InvoiceType invoiceType;
    private static final String TAG = "ReimburseFeeDetail";

    public ReimburseFeeDetailAdapter(Context context, List<ItemReimburseFee> itemReimburseFees) {
        this.context = context;
        this.itemReimburseFees.addAll(itemReimburseFees);
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @NonNull
    @Override
    public ReimburseDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReimburseFeeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_reimburse_fee, parent, false);
        return new ReimburseDetailHolder(binding);
    }

    public List<ItemReimburseFee> getData() {
        return itemReimburseFees;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReimburseDetailHolder holder, final int position) {
        final ItemReimburseFeeBinding binding = holder.getBinding();
        final ItemReimburseFee model = itemReimburseFees.get(position);
        binding.setModel(model);
        setSelectListener(position, binding, model);
        setDeleteListener(binding, model);
        setDateListener(holder, binding);
    }

    private void setDateListener(@NonNull final ReimburseDetailHolder holder, com.example.xinsheng.databinding.ItemReimburseFeeBinding binding) {
        binding.happenDate.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i, i1, i2);
                        ItemReimburseFee model = holder.getModel();
                        model.setReimburseDate(i + "-" + (i1 + 1) + "-" + i2);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
    }

    private void setDeleteListener(com.example.xinsheng.databinding.ItemReimburseFeeBinding binding, final ItemReimburseFee model) {
        binding.setDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(model);
            }
        });
    }

    private void setSelectListener(final int position, final ItemReimburseFeeBinding binding, final ItemReimburseFee model) {
        Log.i(TAG, "setSelectListener");
        binding.requestFee.setOnEditChangeListener(new CompoEditView.OnEditChangeListener() {
            @Override
            public void onEditChange(String str) {
                model.setReimburseFee(str);
            }
        });

        binding.invoiceAmount.setOnEditChangeListener(new CompoEditView.OnEditChangeListener() {
            @Override
            public void onEditChange(String str) {
                model.setFpCount(str);
            }
        });

        binding.reimburseRemarks.setOnEditChangeListener(new CompoEditView.OnEditChangeListener() {
            @Override
            public void onEditChange(String str) {
                model.setRemarks(str);
            }
        });



        binding.reimburseFeeType.setOnSelectItemListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "setSelectListener:" + " position:" + position + " i:" + i);
                if (TextUtils.isEmpty(projectId)) {
                    Toast.makeText(context, R.string.project_select_hint, Toast.LENGTH_LONG).show();
                    return;
                }
                ItemReimburseFee fee = itemReimburseFees.get(position);
                int itemTypePos = i - 1;
                if (fee != null && fee.getFeeTypeList().size() > itemTypePos && itemTypePos >= 0) {
                    fee.setItemTypePos(i);
                    //或者当前选择的费用类型
                    ItemType itemType = fee.getFeeTypeList().get(itemTypePos);
                    model.setItemTypePos(itemTypePos);

                    //获取当前支持的发票类型 TODO
                    HashMap<String, List<InvoiceType.InvoiceItem>> invoiceTypeMap = fee.getInvoiceTypeMap();
                    Log.e(TAG, "feeType:" + itemType.toString());
                    Log.e(TAG, "invoiceMap:" + invoiceTypeMap.toString());
                    List<InvoiceType.InvoiceItem> invoiceItemList = fee.getInvoiceTypeMap().get(itemType.getTypeId());

                    //设置发票科目列表
                    if( invoiceItemList != null ) {
                        int j = 0;
                        model.setInvoiceItemList(invoiceItemList);
                        String[] invoiceNames = new String[invoiceItemList.size()];
                        for (InvoiceType.InvoiceItem item : invoiceItemList) {
                            invoiceNames[j++] = item.getItemName();
                            model.setReimburseLimitFee(item.getFees());
                        }
                        model.setInvoiceTypeName(invoiceNames);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i(TAG, "onNothingSelected");

            }
        });

        binding.invoiceSubject.setOnSelectItemListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<InvoiceType.InvoiceItem> invoiceItemList = model.getInvoiceItemList();
                InvoiceType.InvoiceItem invoiceItem = invoiceItemList.get(position);
                Log.i(TAG, invoiceItem.toString());
                model.setReimburseLimitFee(invoiceItem.getFees());
                model.setInvoiceTypePos(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void deleteItem(ItemReimburseFee fee) {
        itemReimburseFees.remove(fee);
        Log.e(TAG, "deleteItem:" + fee.toString());
        notifyDataSetChanged();
    }

    public void appendItem(ItemReimburseFee itemReimburseFee, HashMap<String, List<InvoiceType.InvoiceItem>> invoiceTypeMap) {
        itemReimburseFees.add(itemReimburseFee);
        notifyItemRangeInserted(itemReimburseFees.size(), 1);
    }

    public void updateInvoiceType(HashMap<String, List<InvoiceType.InvoiceItem>> invoiceTypeMap) {
        for (ItemReimburseFee fee : itemReimburseFees) {
            fee.setInvoiceTypeMap(invoiceTypeMap);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return itemReimburseFees.size();
    }

    static class ReimburseDetailHolder extends RecyclerView.ViewHolder {
        private ItemReimburseFeeBinding binding;

        public ReimburseDetailHolder(@NonNull ItemReimburseFeeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemReimburseFee getModel() {
            return binding.getModel();
        }


        public ItemReimburseFeeBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemReimburseFeeBinding binding) {
            this.binding = binding;
        }
    }
}
