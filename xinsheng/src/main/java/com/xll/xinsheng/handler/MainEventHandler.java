package com.xll.xinsheng.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.xinsheng.R;
import com.xll.xinsheng.bean.InitialData;
import com.xll.xinsheng.bean.ItemType;
import com.xll.xinsheng.bean.Session;
import com.xll.xinsheng.cache.Cache;
import com.xll.xinsheng.model.ReimburseModel;
import com.xll.xinsheng.tools.MyApplication;
import com.xll.xinsheng.ui.DoneActivity;
import com.xll.xinsheng.ui.LoginActivity;
import com.xll.xinsheng.ui.PendingActivity;

import java.util.List;

public class MainEventHandler {

    private static final String TAG = "MainEventHandler";

    public void onPendingClick(Context context) {
        context.startActivity(new Intent(context, PendingActivity.class));
    }

    public void onDoneClick(Context context) {
        context.startActivity(new Intent(context, DoneActivity.class));
    }

    public void onBackClick(Context context) {
        if (context instanceof  Activity) {
            ((Activity)context).finish();
        }
    }

    public void onPopupMenu(View view, Context context) {
        PopupMenu menu = new PopupMenu(context, view);
        MenuItem item = menu.getMenu().add(Menu.NONE, Menu.FIRST, Menu.FIRST, R.string.logout );
        item.setIcon(R.drawable.log_out);

       // menu.inflate(R.menu.main_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == Menu.FIRST) {
                    final Context context = MyApplication.getMyApplication();
                    new Cache<>(context, Cache.LOGIN_INFO).clearCache(Cache.LOGIN_INFO);
                    new Cache<InitialData>(context, Cache.INITIAL_DATA).clearCache(Cache.INITIAL_DATA);
                    new Cache<>(context,Cache.GENERAL_INFO).clearCache(Cache.GENERAL_INFO);
                    context.startActivity(new Intent(context, LoginActivity.class));

                }
                return false;
            }
        });
        menu.show();
    }



    public void showProjectTypeDialog(Context context, final ReimburseModel model) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_project_type);
        final CharSequence[] cs = model.getProjectList();
        builder.setItems(cs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.setSelectProject(String.valueOf(cs[which]));
                Log.i(TAG, "setSelectProject" + String.valueOf(cs[which]));
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showFeeTypeDialog(Context context, final ReimburseModel model) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.title_fee_type);
        Cache<InitialData> cache = new Cache<>(context, Cache.INITIAL_DATA);
        InitialData data = cache.getInitialData();
        if (data != null) {
           List<ItemType> itemTypes = data.getItemTypeList();
           if (itemTypes != null && itemTypes.size() > 0) {
               final CharSequence[] items = new CharSequence[itemTypes.size()];
               for (int i = 0; i < itemTypes.size(); i++) {
                   items[i] = itemTypes.get(i).getTypeName();
               }
               builder.setItems(items, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       model.setFeeType(String.valueOf(items[which]));
                       dialog.dismiss();
                   }
               });
               AlertDialog dialog = builder.create();
               dialog.show();
               return;
           }
        }
        builder.setMessage(R.string.no_info);
        builder.show();
    }
}
