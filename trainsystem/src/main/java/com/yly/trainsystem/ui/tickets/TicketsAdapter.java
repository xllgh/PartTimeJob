package com.yly.trainsystem.ui.tickets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yly.trainsystem.R;
import com.yly.trainsystem.databinding.TicketItemBinding;

import java.util.ArrayList;
import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder> {

    private Context context;

    List<TicketInfo> ticketInfos = new ArrayList<>();

    private String oldOrderId;


    public TicketsAdapter(Context context, List<TicketInfo> ticketInfos, String oldOrderId) {
        this.context = context;
        this.ticketInfos.addAll(ticketInfos);
        this.oldOrderId = oldOrderId;

    }

    @NonNull
    @Override
    public TicketsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TicketItemBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.ticket_item, parent, false);
        return new TicketsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsViewHolder holder, int position) {
        final TicketInfo ticketInfo = ticketInfos.get(position);
       final TrainInfo trainInfo = ticketInfo.getTrainInfo();
       if (trainInfo != null) {
           TicketViewModel model = new TicketViewModel();
           model.departure.set(trainInfo.getDeparture());
           model.destination.set(trainInfo.getDestination());
           model.trainName.set(trainInfo.getTrainName());
           model.departureTime.set( String.format(context.getString(R.string.depart_time_fomart),
                   trainInfo.getDepartureTime()));

           holder.setViewModel(model);
           holder.setListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, TicketDetailActivity.class);
                   intent.putExtra("ticketInfo", ticketInfo);
                   intent.putExtra("oldOrderId", oldOrderId);
                   context.startActivity(intent);
               }
           });
       }

    }

    @Override
    public int getItemCount() {
        return ticketInfos.size();
    }

    static class TicketsViewHolder extends RecyclerView.ViewHolder {
        private TicketItemBinding binding;

        public TicketsViewHolder(@NonNull TicketItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setViewModel(TicketViewModel viewModel) {
            binding.setTicket(viewModel);
        }

        public void setListener(View.OnClickListener listener) {
            binding.setListener(listener);
        }
    }
}
