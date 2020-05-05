package com.giants3.hd.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giants3.android.frame.util.StringUtil;
import com.giants3.hd.android.R;
import com.giants3.hd.android.adapter.WorkFlowMessageAdapter;
import com.giants3.hd.android.mvp.NewPresenter;
import com.giants3.hd.android.mvp.NewViewer;
import com.giants3.hd.data.utils.GsonUtils;
import com.giants3.hd.entity.WorkFlowMessage;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;


public class WorkFlowMessageListActivity extends BaseHeadViewerActivity<NewPresenter> {

    private static final int REQUEST_MESSAGE_OPERATE = 999;
    public static final String KEY_MESSAGE_LIST = "KEY_MESSAGE_LIST";
    public static String KEY_TITLE="KEY_TITLE";
    @Bind(R.id.list)
    ListView listView;
    private WorkFlowMessageAdapter adapter;


    @Override
    protected NewPresenter onLoadPresenter() {
        return new NewPresenter() {
            @Override
            public void attachView(NewViewer view) {

            }

            @Override
            public void start() {

            }

            @Override
            public void detachView() {

            }
        };
    }

    @Override
    protected void initEventAndData(Bundle savedInstance) {



        setTitle("任务列表");
        String title = getIntent().getStringExtra(KEY_TITLE);
        if(!StringUtil.isEmpty(title))
        {
            setTitle(title);
        }

        adapter = new WorkFlowMessageAdapter(this);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showMessageDetail((WorkFlowMessage) parent.getItemAtPosition(position));


            }
        });


        List<WorkFlowMessage> messages = GsonUtils.fromJson(getIntent().getStringExtra(KEY_MESSAGE_LIST), new TypeToken<List<WorkFlowMessage>>() {
        }.getType());


        adapter.setDataArray(messages);

    }


    private void showMessageDetail(WorkFlowMessage workFlowMessage) {
        Intent intent = new Intent(this, WorkFlowMessageReceiveActivity.class);
        intent.putExtra(WorkFlowMessageReceiveActivity.KEY_MESSAGE, GsonUtils.toJson(workFlowMessage));
        startActivityForResult(intent, REQUEST_MESSAGE_OPERATE);

    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_work_flow_message_list, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_MESSAGE_OPERATE) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
