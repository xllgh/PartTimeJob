package com.xll.xinsheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinsheng.R;
import com.example.xinsheng.databinding.ItemLoadMoreBinding;
import com.xll.xinsheng.tools.MyApplication;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadMoreRecyclerAdapter<T> extends RecyclerView.Adapter {

    protected static final int TYPE_DATA = 1;
    protected static final int TYPE_HINT = 0;
    private Context context;
    private int total;

    private List<T> list = new  ArrayList();

    public LoadMoreRecyclerAdapter(@NonNull List<T> data, @NonNull Context context, int total) {
        list.addAll(data);
        this.context = context;
        this.total = total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(TYPE_HINT == viewType) {
            ItemLoadMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_load_more, parent, false);
            return new HintViewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HintViewHolder) {
            HintViewHolder h = (HintViewHolder) holder;
            if (position == total - 1) {
                h.setText(MyApplication.getMyApplication().getString(R.string.no_more_data));
            } else {
                h.setText(MyApplication.getMyApplication().getString(R.string.load_more));
            }
            return;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() - 1) {
            return TYPE_HINT;
        } else  {
            return TYPE_DATA;
        }
    }

    static class HintViewHolder extends RecyclerView.ViewHolder{
        private ItemLoadMoreBinding binding;

        public HintViewHolder(@NonNull ItemLoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setText(String text) {
            binding.setHint(text);
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

        public EndlessRecyclerOnScrollListener() {
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
