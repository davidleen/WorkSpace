package com.rnmap_wb.message;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.giants3.android.reader.domain.GsonUtils;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.home.HomeActivity;
import com.rnmap_wb.android.data.Task;

/**
 * Created by davidleen29 on 2018/6/24.
 */

public class PushManager {

    static PushManager pushManager;
    private Context context;


    public static PushManager getInstance() {
        if (pushManager == null) pushManager = new PushManager();
        return pushManager;
    }

    public void init(Application application) {
        this.context = application;
    }


    public void  handleMessage(Task task) {


        if (task == null) return;






        Intent intent = null;


        intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(HomeActivity.PARAM_TASK, GsonUtils.toJson(task));



        if (intent != null)
            showMessage(task.hashCode(), context, "新任务", "您有新任务", task.name, "", intent);


    }


    // - 收到私信 在通知栏显示-
    private void showMessage(final int notifyId, final Context context, final String tickerText, final String contentTitle,
                             final String contentText, String thumbnail, final Intent intent) {





            showNotification(contentTitle,
                    contentText,  notifyId, tickerText, intent);



    }

    private void showNotification(String contentTitle, String contentText,   int notifyId, String tickerText, Intent intent) {


        showNotification(notifyId, context, tickerText, contentTitle,
                contentText,  intent);
    }


    /**
     * @param notifyId
     * @param context
     * @param tickerText
     * @param contentTitle
     * @param contentText
     * @param intent
     */
    private void showNotification(int notifyId, Context context, String tickerText, String contentTitle,
                                  String contentText,   Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = NotificationUtils.createBuilder(context);
        Notification notification = builder
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText).setTicker(tickerText)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND;
        // 唤醒
        notificationManager.notify(notifyId, notification);
    }


}
