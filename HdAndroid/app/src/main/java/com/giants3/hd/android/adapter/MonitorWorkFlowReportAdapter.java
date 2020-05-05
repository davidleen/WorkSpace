package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giants3.android.adapter.AbstractAdapter;
import com.giants3.hd.android.R;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.android.helper.ImageLoaderFactory;
import com.giants3.hd.android.helper.ImageViewerHelper;
import com.giants3.hd.data.net.HttpUrl;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpWorkFlowReport;

import butterknife.Bind;

/**
 * Created by david on 2016/2/14.
 */
public class MonitorWorkFlowReportAdapter
          extends ItemListAdapter<ErpWorkFlowReport> {






    public MonitorWorkFlowReportAdapter(Context context) {
        super(context);
        setTableData(TableData.resolveData(context, R.array.table_monitor_work_flow_report));

    }


    @Override
    protected boolean bindFieldData(View view, ErpWorkFlowReport data, String field) {


        if ("isOverDue".equals(field)&&view instanceof TextView)
        {

            ((TextView) view).setText(data.isOverDue?"是":"否");
            return true;
        }
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= super.getView(position, convertView, parent);


        ErpWorkFlowReport item=getItem(position);
        if(item!=null) {

            if (item.overDueDay > 5)

            {
                view.setBackgroundResource(R.drawable.icon_workflow_limit_exceed_5_selector);

            } else if (item.overDueDay > 0) {
                view.setBackgroundResource(R.drawable.icon_workflow_limit_selector);
            } else if (item.overDueDay != 0 && Math.abs(item.overDueDay) <= item.alertDay) {
                view.setBackgroundResource(R.drawable.icon_workflow_alert_selector);
            } else {
                view.setBackgroundResource(R.drawable.list_item_bg_selector);
            }


        }

        return view;

    }
}
