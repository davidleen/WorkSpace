package com.giants3.hd.android.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.giants3.hd.android.R;

/**
 * 通知的适配类
 * Created by davidleen29 on 2018/8/27.
 */

public class NotificationUtils {

    public static Notification.Builder createBuilder(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        //适配安卓8.0的消息渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setChannelId(context.getResources().getString(R.string.notify_channel_id));
        }
        return builder;
    }


    public static void createNotificationChanel(Context context) {

        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //适配安卓8.0的消息渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(context.getResources().getString(R.string.notify_channel_id), context.getResources().getString(R.string.notify_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

    }
}
