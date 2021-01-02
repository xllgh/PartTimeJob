package com.xll.xinsheng.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemDoneProcessNewBinding;
import com.example.xinsheng.databinding.ItemLoadMoreBinding;
import com.xll.xinsheng.model.DoneProcess;
import com.xll.xinsheng.tools.MyApplication;
import com.xll.xinsheng.ui.ProcessDetailActivity;

import java.util.List;
import java.util.Random;

public class ProcessDoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<DoneProcess> processList;

    private static final int TYPE_DATA = 1;
    private static final int TYPE_HINT = 0;

    private int[] backgroundList = new int[] {R.drawable.ic_round1, R.drawable.ic_round2, R.drawable.ic_round3,
            R.drawable.ic_round4, R.drawable.ic_round5, R.drawable.ic_round6};

    private int random = new Random().nextInt(backgroundList.length - 1);
    private int total;

    public ProcessDoneAdapter(Context context, List<DoneProcess> list, int total) {
        this.context = context;
        this.processList = list;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 加载更多
     * 1.两个布局，两个ViewHolder
     *
     * **/

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HINT) {
          ItemLoadMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_load_more, parent, false);
          return new LoadMoreViewHolder(binding);
        } else {
            ItemDoneProcessNewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_done_process_new, parent, false);
            return new ProcessListViewHolder(binding);
        }

    }

    public void update(List<DoneProcess> list) {
        int currentIndex = processList.size() - 1;
        processList.addAll(list);
        notifyItemRangeChanged(currentIndex, list.size());
    }

    public void resetUpdate(List<DoneProcess> list) {
        processList.clear();
        processList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ProcessListViewHolder) {
            ProcessListViewHolder h = (ProcessListViewHolder) holder;

            final DoneProcess model = processList.get(position);
            h.setMode(model);

            final int index = (random + position) % backgroundList.length;
            model.setImgRes(backgroundList[index]);
            h.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderId = model.getOrderId();
                    Intent intent = new Intent(context, ProcessDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("orderType", model.getProcessType());
                    intent.putExtra("isDone",true);
                    context.startActivity(intent);
                }
            });
        }

        if (holder instanceof LoadMoreViewHolder) {
            LoadMoreViewHolder h = (LoadMoreViewHolder) holder;
            if (position == total - 1) {
                h.setText(MyApplication.getMyApplication().getString(R.string.no_more_data));
            } else {
                h.setText(MyApplication.getMyApplication().getString(R.string.load_more));
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == processList.size() - 1) {
            return TYPE_HINT;
        } else  {
            return TYPE_DATA;
        }
    }

    @Override
    public int getItemCount() {
        return processList.size();
    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder{
        private  ItemLoadMoreBinding binding;

        public LoadMoreViewHolder(@NonNull ItemLoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setText(String text) {
            binding.setHint(text);

        }
    }

    static class ProcessListViewHolder extends RecyclerView.ViewHolder {

        ItemDoneProcessNewBinding binding;

        public ProcessListViewHolder(@NonNull ItemDoneProcessNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setListener(View.OnClickListener listener) {
            binding.setListener(listener);
        }

        private void setMode(DoneProcess mode) {
            binding.setProcessMode(mode);
        }
    }


    public abstract static class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        //用来标记是否正在向上滑动
        private boolean isSlidingUpward = false;

        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public EndlessRecyclerOnScrollListener(int total) {
            this.total = total;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //获取最后一个完全显示的itemPosition
                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                int itemCount = manager.getItemCount();

                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition == (itemCount - 1) && isSlidingUpward && itemCount != total) {
                    //加载更多
                    onLoadMore();
                }
            }
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
            isSlidingUpward = dy > 0;
        }
        /**
         * 加载更多回调
         */
        public abstract void onLoadMore();
    }
}
