package com.rnmap_wb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giants3.android.ToastHelper;
import com.giants3.android.reader.domain.DefaultUseCaseHandler;
import com.giants3.android.reader.domain.UseCaseFactory;
import com.rnmap_wb.R;
import com.rnmap_wb.activity.mapwork.SimpleMvpActivity;
import com.rnmap_wb.adapter.MessageAdapter;
import com.rnmap_wb.android.dao.DaoManager;
import com.rnmap_wb.android.data.RemoteData;
import com.rnmap_wb.android.data.TaskMessage;
import com.rnmap_wb.android.entity.MessageState;
import com.rnmap_wb.url.HttpUrl;

import java.util.List;

import butterknife.Bind;

public class MessageActivity extends SimpleMvpActivity {

    MessageAdapter adapter;


    @Bind(R.id.listview)
    ListView listView;

    public static void start(Activity activity) {


        Intent intent = new Intent(activity, MessageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationController().setTitle("消息列表");
        getNavigationController().setLeftView(R.drawable.icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new MessageAdapter(this);
        adapter.setOnViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TaskMessage taskMessage= (TaskMessage) v.getTag();
                viewTask(taskMessage);
            }
        });

        listView.setAdapter(adapter);

        String url = HttpUrl.getMessageList();

        UseCaseFactory.getInstance().createPostUseCase(url, TaskMessage.class).execute(new DefaultUseCaseHandler<RemoteData<TaskMessage>>() {
            @Override
            public void onError(Throwable e) {

                ToastHelper.show(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<TaskMessage> remoteData) {

                if (remoteData.isSuccess()) {

                    List<MessageState> states=DaoManager.getInstance().getMessageStateDao().findAll();

                    for (TaskMessage taskMessage:remoteData.data)
                    {

                        for(MessageState messageState:states)
                        {
                            if(taskMessage.id.equalsIgnoreCase(messageState.messageId))
                            {
                                taskMessage.read=true;
                                break;
                            }
                        }
                    }




                    adapter.setDataArray(remoteData.data);
                } else {
                    ToastHelper.show(remoteData.errmsg);
                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Object itemAtPosition = parent.getItemAtPosition(position);
                if (itemAtPosition instanceof TaskMessage) {
                    TaskMessage message = (TaskMessage) itemAtPosition;


                    viewTask(message);

                }


            }
        });
    }

    private void viewTask(TaskMessage message) {
        ProjectTaskDetailActivity.start(MessageActivity.this, message.task, 0);

        if (!message.read) {

            MessageState messageState = DaoManager.getInstance().getMessageStateDao().findByMessageId(message.id);
            if(messageState==null) {
                messageState=new MessageState();
                messageState.messageId = message.id;

            }
            messageState.read=true;
            DaoManager.getInstance().getMessageStateDao().save(messageState);
            message.read = true;
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}
