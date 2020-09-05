package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giants3.hd.android.R;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.entity.ErpWorkFlowReport;

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
                view.setBackgroundResource(R.drawable.bg_red_selector);

            } else if (item.overDueDay > 0) {
                view.setBackgroundResource(R.drawable.bg_orange_selector);
            } else if (item.overDueDay != 0 && Math.abs(item.overDueDay) <= item.alertDay) {
                view.setBackgroundResource(R.drawable.bg_yellow_selector);
            } else {
                view.setBackgroundResource(R.drawable.list_item_bg_selector);
            }


        }

        return view;

    }
}
