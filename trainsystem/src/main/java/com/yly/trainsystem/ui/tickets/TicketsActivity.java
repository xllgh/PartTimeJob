package com.yly.trainsystem.ui.tickets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yly.trainsystem.R;
import com.yly.trainsystem.databinding.ActivityTicketsBinding;
import com.yly.trainsystem.volley.HttpUtils;
import com.yly.trainsystem.volley.ServerTicketResponseArray;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TicketsActivity extends AppCompatActivity {


    private ActivityTicketsBinding binding;
    private String oldOrderId;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tickets);
        Intent intent = getIntent();
        boolean ticketFlag = true;
        if (intent != null) {
           Bundle bundle =  intent.getExtras();
           if (bundle != null) {
               HashMap<String, String> map = (HashMap<String, String>) bundle.getSerializable("data");

               oldOrderId = Objects.requireNonNull(map).get("oldOrderId");
               binding.setLoading(true);
               HttpUtils.post(HttpUtils.QUERY_TICKET, map, listener, errorListener);
               ticketFlag = true;
           } else {
               ticketFlag = false;
           }
        } else {
            ticketFlag = false;
        }
        binding.setHasTickckets(ticketFlag);

    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            binding.setLoading(false);
            Gson gson = new Gson();
            ServerTicketResponseArray ticketInfos = gson.fromJson(response, ServerTicketResponseArray.class);
            if (ticketInfos == null) {
                binding.setHasTickckets(false);
                return;
            }
            List<TicketInfo> list = ticketInfos.getData();

            if(list == null || list.size() ==0) {
                binding.setHasTickckets(false);
                return;
            }

            if (binding != null) {
                binding.setAdapter(new TicketsAdapter(TicketsActivity.this, list, oldOrderId));
            }
        }
    };

  Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
          binding.setLoading(false);
          Toast.makeText(TicketsActivity.this,  R.string.net_error , Toast.LENGTH_LONG).show();
      }
  };


}
