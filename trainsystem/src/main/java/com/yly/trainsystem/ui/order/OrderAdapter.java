package com.yly.trainsystem.ui.order;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends FragmentStateAdapter {

    private List<OrderListFragment> fragments = new ArrayList<>();

    public OrderAdapter(@NonNull FragmentActivity fragmentActivity, List<OrderListFragment> fragments) {
        super(fragmentActivity);
        this.fragments.addAll(fragments);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
