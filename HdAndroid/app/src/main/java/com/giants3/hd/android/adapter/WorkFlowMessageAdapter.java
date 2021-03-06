package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.android.adapter.AbstractViewHolder;
import com.giants3.hd.android.R;
import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.hd.android.helper.ImageViewerHelper;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.utils.StringUtils;

import butterknife.BindView;


/**
 * 流程处理
 * Created by david on 20160919
 */
public class WorkFlowMessageAdapter
        extends AbstractAdapter<WorkFlowMessage> implements View.OnClickListener {


    public WorkFlowMessageAdapter(Context context) {
        super(context);

    }

    @Override
    protected Bindable<WorkFlowMessage> createViewHolder(int itemViewType) {
        return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_work_flow_message, null), this);
    }

    @Override
    public void onClick(View v) {

        WorkFlowMessage message = (WorkFlowMessage) v.getTag();
        switch (v.getId()) {
            case R.id.picture:

                ImageViewerHelper.view(v.getContext(), message.url);

                break;
            case R.id.check:


                break;
            case R.id.receive:


                break;
        }

    }


    public static class ViewHolder extends AbstractViewHolder<WorkFlowMessage> {
        public WorkFlowMessage mItem;
         @BindView(R.id.picture)
        public ImageView picture;

         @BindView(R.id.fromFlow)
        public TextView fromFlow;

         @BindView(R.id.toFlow)
        public TextView toFlow;
         @BindView(R.id.tranQty)
        public TextView tranQty;

         @BindView(R.id.name)
        public TextView name;
         @BindView(R.id.orderName)
        public TextView orderName;
         @BindView(R.id.productName)
        public TextView productName;
         @BindView(R.id.pVersion)
        public TextView pVersion;
         @BindView(R.id.mrpNo)
        public TextView mrpNo;
         @BindView(R.id.qty)
        public TextView qty;
         @BindView(R.id.batNo)
        public TextView batNo;
         @BindView(R.id.cus_no)
        public TextView cus_no;
         @BindView(R.id.unitName)
        public TextView unitName;


         @BindView(R.id.createTime)
        public TextView createTime;
         @BindView(R.id.state)
        public TextView state;
         @BindView(R.id.panel_reason)
        public View panel_reason;
         @BindView(R.id.reason)
        public TextView reason;


         @BindView(R.id.panel_receiver)
        public View panel_receiver;
         @BindView(R.id.receiver)
        public TextView receiver;

         @BindView(R.id.panel_sender)
        public View panel_sender;
         @BindView(R.id.sender)
        public TextView sender;


         @BindView(R.id.panel_factory)
        public View panel_factory;
         @BindView(R.id.factory)
        public TextView factory;

        public ViewHolder(View view, View.OnClickListener listener) {
            super(view);
            picture.setOnClickListener(listener);


        }


        @Override
        public void bindData(AbstractAdapter<WorkFlowMessage> adapter, WorkFlowMessage data, int position) {

            mItem = data;
            name.setText(data.name);
            orderName.setText(data.orderName);
            qty.setText(String.valueOf(data.orderItemQty));
            productName.setText(data.productName);
            pVersion.setText(data.pVersion);
            toFlow.setText(data.toFlowName);
            fromFlow.setText(data.fromFlowName);
            tranQty.setText(String.valueOf(data.transportQty));
            factory.setText(String.valueOf(data.factoryName));
            mrpNo.setText(data.mrpNo == null ? "" : data.mrpNo);
            batNo.setText(data.bat_no);
            cus_no.setText(data.cus_no);

            panel_receiver.setVisibility(data.receiverId>0?View.VISIBLE:View.GONE);
            panel_sender.setVisibility(data.senderId>0?View.VISIBLE:View.GONE);

            receiver.setText(data.receiverName+ "   "+data.receiveTimeString);
            sender.setText(data.senderName+ "   "+data.createTimeString);



            panel_factory.setVisibility(StringUtils.isEmpty(data.factoryName) ? View.GONE : View.VISIBLE);


            ImageLoaderFactory.getInstance().displayImage(HttpUrl.completeUrl(data.url), picture);
            picture.setTag(data);
            unitName.setText("");


            String stateText = "";
            switch (data.state) {
                case WorkFlowMessage.STATE_SEND:
                    stateText = "待确认";
                    break;
                case WorkFlowMessage.STATE_RECEIVE:
                    stateText = "待审核";
                    break;
                case WorkFlowMessage.STATE_REWORK:
                    stateText = "返工";
                    break;
                case WorkFlowMessage.STATE_REJECT:
                    stateText = "审核未通过";
                    break;
                case WorkFlowMessage.STATE_PASS:
                    stateText = "已通过";
                    break;

                case WorkFlowMessage.STATE_ROLL_BACK:
                    stateText = "已撤销";
                    break;

            }
            boolean showReason = !StringUtils.isEmpty(data.sendMemo);
            panel_reason.setVisibility(showReason ? View.VISIBLE : View.GONE);
            if (showReason) {
                reason.setText(data.sendMemo);
            }
            state.setText(stateText);
            createTime.setText(data.createTimeString.substring(0, 10));


        }

        @Override
        public View getContentView() {
            return v;
        }


    }


}
