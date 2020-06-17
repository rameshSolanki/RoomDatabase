package com.fridayapp.roomdatabase.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import com.fridayapp.roomdatabase.R;
import com.fridayapp.roomdatabase.activities.MainActivity;
import com.fridayapp.roomdatabase.model.Task;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "chID";
    public static final String channelNAME = "chNAME";
  Task task;
    NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    public void createChannel() {
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelID, channelNAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(R.color.colorAccent);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(notificationChannel);

        }


    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return notificationManager;

    }

    public NotificationCompat.Builder builderNot(String title, String desc) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                //.setContentText(desc)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.edit1);
    }
    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        return PendingIntent.getActivity(getApplicationContext(),
                MainActivity.REQUEST_CODE_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
