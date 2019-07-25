package com.rnmap_wb.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.giants3.android.ToastHelper;
import com.giants3.android.frame.util.FileUtils;
import com.giants3.android.frame.util.StringUtil;
import com.rnmap_wb.R;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.service.SynchronizeCenter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedBackDialog {


    public static void start(Activity activity, final Task task) {

        boolean b = SynchronizeCenter.waitForFeedBack(task);

        String previousMemo = "";
        if (b) {
            previousMemo = FileUtils.readStringFromFile(SynchronizeCenter.getTaskFeedbackFilePath(task));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_feed_back, null);
        final ViewHolder holder = new ViewHolder();
        try {
            ButterKnife.bind(holder, view);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (!StringUtil.isEmpty(previousMemo))
            holder.memo.setText(previousMemo);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();


        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String memeo = holder.memo.getText().toString();

                try {
                    FileUtils.writeStringToFile(memeo, SynchronizeCenter.getTaskFeedbackFilePath(task));
                    FileUtils.writeStringToFile("", SynchronizeCenter.getTaskUpdateStateFilePath(task));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SynchronizeCenter.synchronize();

                alertDialog.dismiss();
                ToastHelper.show("反馈已经提交");

            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });
        alertDialog.show();

    }


    public static class ViewHolder {

        @Bind(R.id.confirm)
        public View confirm;

        @Bind(R.id.cancel)
        public View cancel;

        @Bind(R.id.memo)
        public EditText memo;


    }

}
