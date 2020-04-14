package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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

}
