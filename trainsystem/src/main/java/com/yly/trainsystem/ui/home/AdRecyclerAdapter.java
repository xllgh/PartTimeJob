package com.yly.trainsystem.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yly.trainsystem.R;
import com.yly.trainsystem.databinding.ItemAddBinding;

import java.util.ArrayList;

public class AdRecyclerAdapter extends RecyclerView.Adapter<AdRecyclerAdapter.AdViewHolder> {

    private ArrayList<Integer> adList;
    private Context context;

    public AdRecyclerAdapter(@NonNull ArrayList<Integer> adList, @NonNull Context context) {
        this.adList = adList;
        this.context = context;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, @DrawableRes int resId) {
        Picasso.get().load(resId).fit().into(view);
    }

    @NonNull
    @Override
    public AdRecyclerAdapter.AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ItemAddBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_add, parent, false);
        return new AdViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdRecyclerAdapter.AdViewHolder holder, int position) {
        int resId = adList.get(position);
        AdViewModel adViewModel = new AdViewModel();
        adViewModel.imageUrl.set(resId);
       holder.binding.setAdViewModel(adViewModel);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        ItemAddBinding binding;

         AdViewHolder(@NonNull  ItemAddBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
