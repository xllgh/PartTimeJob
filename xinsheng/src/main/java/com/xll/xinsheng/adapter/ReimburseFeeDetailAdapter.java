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
import androidx.databinding.adapters.AdapterViewBindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemReimburseFeeBinding;
import com.xll.xinsheng.bean.InvoiceType;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.ItemReimburseFee;
import com.xll.xinsheng.myview.CompoEditView;
import com.xll.xinsheng.ui.ReimburseRequestActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReimburseFeeDetailAdapter extends RecyclerView.Adapter<ReimburseFeeDetailAdapter.ReimburseDetailHolder> {

    private final Context context;
    private final List<ItemReimburseFee> itemReimburseFees = new ArrayList<>();
    private String projectId;
    private final String TAG = "ReimburseFeeAdapter";


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
        Log.i(TAG, "onBindViewHolder:" + model);
        model.setPosition(position);
        binding.setModel(model);
        setListener(binding);
        setDeleteListener(binding);
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

    private void setDeleteListener(com.example.xinsheng.databinding.ItemReimburseFeeBinding binding) {
        final ItemReimburseFee model = binding.getModel();

        binding.setDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(model);
            }
        });
    }

    private void setListener(final ItemReimburseFeeBinding binding) {

        final ItemReimburseFee model = binding.getModel();
        Log.i(TAG, "setListener:model:" + model);

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

        binding.reimburseFeeType.setOnItemSelectListener(new AdapterViewBindingAdapter.OnItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "setOnItemSelectListener:" + " position:" + position + " projectId:"  + projectId);
                if (TextUtils.isEmpty(projectId)) {
                    Toast.makeText(context, R.string.project_select_hint, Toast.LENGTH_LONG).show();
                    return;
                }

                model.setItemTypePos(position);
                final String[] itemTypeName = model.getItemTypeName();
                final List<ItemType> feeTypeList = model.getFeeTypeList();

                if(itemTypeName != null && position < itemTypeName.length) {
                     String curTypeName = itemTypeName[position];
                    for(ItemType itemType : feeTypeList) {
                        if (!TextUtils.isEmpty(curTypeName) && curTypeName.equals(itemType.getTypeName())) {
                            final InvoiceType invoiceType = getCacheInvoiceType(projectId+itemType.getTypeId());
                            if (invoiceType != null) {
                                final List<InvoiceType.InvoiceItem> invoiceItems = invoiceType.getInvoiceItems();
                                if (invoiceItems != null) {
                                    model.setInvoiceItemList(invoiceItems);
                                    List<String> list = new ArrayList<>();

                                    //设置选中的InvoiceItem
                                    for (InvoiceType.InvoiceItem item : invoiceItems) {
                                        String itemId = item.getItemId();
                                        Log.i(TAG, itemId + "-" + model);
                                        if(!TextUtils.isEmpty(itemId) && itemId.equals(model.getInvoiceSubject())) {
                                            list.add(0, item.getItemName());
                                            model.setReimburseLimitFee(item.getFees());
                                        } else {
                                            list.add(item.getItemName());
                                        }
                                    }
                                    model.setInvoiceTypeName(list.toArray(new String[0]));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });
        binding.reimburseFeeType.setCurrentPosition(model.getItemTypePos());

        binding.invoiceSubject.setOnItemSelectListener(new AdapterViewBindingAdapter.OnItemSelected() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<InvoiceType.InvoiceItem> invoiceItemList = model.getInvoiceItemList();
                InvoiceType.InvoiceItem invoiceItem = invoiceItemList.get(position);
                //Log.i(TAG, invoiceItem.toString());
                model.setReimburseLimitFee(invoiceItem.getFees());
                model.setInvoiceTypePos(position);
            }
        });

    }

    private InvoiceType getCacheInvoiceType(String key) {
        final Cache<InvoiceType> cache = new Cache<>(context, Cache.KMXX_INFO);
        return cache.getInvoiceType(key);
    }


    public void deleteItem(@NonNull ItemReimburseFee fee) {
        itemReimburseFees.remove(fee);
        notifyItemRemoved(fee.getPosition());
    }

    public void appendItem(ItemReimburseFee itemReimburseFee) {
        itemReimburseFees.add(itemReimburseFee);
        notifyItemRangeInserted(itemReimburseFees.size() - 1, 1);
    }

    public void appendItems(@NonNull List<ItemReimburseFee> items) {
        itemReimburseFees.clear();
        itemReimburseFees.addAll(items);
        notifyDataSetChanged();
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
