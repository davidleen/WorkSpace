package com.giants3.hd.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.giants3.hd.android.R;
import com.giants3.hd.android.entity.TableData;
import com.giants3.hd.noEntity.ProducerValueReport;

/**
 * Created by david on 2016/2/14.
 */
public class ProducerValueReportAdapter
        extends ItemListAdapter<ProducerValueReport> {


//    private View.OnClickListener itemClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            ViewHolder viewHolder = (ViewHolder) v.getTag();
//
//            Context context = v.getContext();
//            if (context instanceof ProductListFragment.OnFragmentInteractionListener) {
//                ((ProductListFragment.OnFragmentInteractionListener) context).onFragmentInteraction(viewHolder.mItem);
//            }
//
//
//        }
//    };


    public ProducerValueReportAdapter(Context context) {
        super(context);
        setTableData(TableData.resolveData(context, R.array.table_producer_value_report));

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ProducerValueReport item = getItem(position);
        if (item != null) {


            int color = R.drawable.list_item_bg_selector;
            if (item.workingValue > 0) {
                if (item.maxOutputValue > 0 && item.workingValue > item.maxOutputValue) {
                    color = R.drawable.bg_red_selector;
                } else if (item.maxOutputValue > 0 && item.workingValue > item.maxOutputValue * 0.8f) {
                    color = R.drawable.bg_yellow_selector;
                } else if (item.minOutputValue > 0 && item.workingValue < item.minOutputValue) {
                    color = R.drawable.bg_green_selector;
                }

            }
            view.setBackgroundResource(color);


        }


        return view;


    }

}
