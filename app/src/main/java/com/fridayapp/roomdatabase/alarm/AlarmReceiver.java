package com.fridayapp.roomdatabase.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.fridayapp.roomdatabase.model.Task;

import static com.fridayapp.roomdatabase.model.Constants.EXTRA_ID;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_STATUS;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_TITLE;

public class AlarmReceiver extends BroadcastReceiver {
    int BIG_NUM_FOR_ALARM=100;
    int id;
    @Override
    public void onReceive(Context context, Intent intent) {
        id = intent.getIntExtra("id",0);
        int num=id-BIG_NUM_FOR_ALARM;
        Toast.makeText(context,"Time UP!", Toast.LENGTH_LONG).show();
        NotificationHelper notificationHelper = new NotificationHelper(context);

        String title = intent.getStringExtra(EXTRA_TITLE);
        String desc = intent.getStringExtra(EXTRA_STATUS);
        NotificationCompat.Builder builder = notificationHelper.builderNot(title, desc);
        notificationHelper.getManager().notify(num, builder.build());
    }
}
