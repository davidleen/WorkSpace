package com.rnmap_wb.message;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.RemoteViews;

import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.domain.GsonUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.home.HomeActivity;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.helper.ImageLoaderFactory;

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


    public void handleMessage(Task task) {


        if (task == null) return;
        Intent intent = null;


        intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(HomeActivity.PARAM_TASK, GsonUtils.toJson(task));


        if (intent != null)
            showWorkFlowMessageNotification(task.hashCode(), context, "new task", "您有新任务", task.name, "", intent);


    }


    // - 收到私信 在通知栏显示-
    private void showWorkFlowMessageNotification(final int notifyId, final Context context, final String tickerText, final String contentTitle,
                                                 final String contentText, String thumbnail, final Intent intent) {


        if (StringUtil.isEmpty(thumbnail)) {


            showNotification(contentTitle,
                    contentText, null, notifyId, tickerText, intent);
            return;
        }
        //   String url = HttpUrl.completeUrl(thumbnail);

        String url = thumbnail;

        // Bitmap loadedImage=ImageLoaderFactory.getInstance().loadImageSync(url);

        ImageLoaderFactory.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                showNotification(contentTitle,
                        contentText, null, notifyId, tickerText, intent);


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


                showNotification(contentTitle,
                        contentText, loadedImage, notifyId, tickerText, intent);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {


                showNotification(contentTitle,
                        contentText, null, notifyId, tickerText, intent);

            }
        });


    }

    private void showNotification(String contentTitle, String contentText, Bitmap bitmap, int notifyId, String tickerText, Intent intent) {
        final RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification);
        mRemoteViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);

        mRemoteViews.setTextViewText(R.id.title, contentTitle);
        mRemoteViews.setTextViewText(R.id.content, contentText);
        if (bitmap != null)

            mRemoteViews.setImageViewBitmap(R.id.icon, bitmap);
        else
            mRemoteViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);

        showNotification(notifyId, context, tickerText, contentTitle,
                contentText, mRemoteViews, intent);
    }


    /**
     * @param notifyId
     * @param context
     * @param tickerText
     * @param contentTitle
     * @param contentText
     * @param remoteView
     * @param intent
     */
    private void showNotification(int notifyId, Context context, String tickerText, String contentTitle,
                                  String contentText, RemoteViews remoteView, Intent intent) {

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
                .setContent(remoteView)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND;
        // 唤醒
        notificationManager.notify(notifyId, notification);
    }


}
