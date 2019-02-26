package com.rnmap_wb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.rnmap_wb.R;
import com.rnmap_wb.android.data.Directory;
import com.rnmap_wb.android.data.Task;
import com.rnmap_wb.service.SynchronizeCenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeTaskAdapter extends BaseExpandableListAdapter {


    List<Directory> datas;

    public HomeTaskAdapter(Context context, ItemListener listener) {
        this(context, new ArrayList<Directory>(), listener);
    }

    public HomeTaskAdapter(Context context, List<Directory> datas, ItemListener listener) {
        super();
        this.context = context;
        this.datas = datas;
        this.listener = listener;
    }

    private final Context context;
    private ItemListener listener;


    public void setDatas(List<Directory> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).list.size();
    }

    @Override
    public Directory getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Task getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return datas.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return datas.get(groupPosition).list.get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        GroupViewHolder viewHolder;
        if (convertView != null) {

            viewHolder = (GroupViewHolder) convertView.getTag();

        } else {

            View inflate = LayoutInflater.from(context).inflate(R.layout.list_item_dir, null);
            viewHolder = new GroupViewHolder(inflate);
            inflate.setTag(viewHolder);
            convertView = inflate;

        }
        viewHolder.bindData(getGroup(groupPosition),isExpanded);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (convertView != null) {

            viewHolder = (ViewHolder) convertView.getTag();

        } else {

            View inflate = LayoutInflater.from(context).inflate(R.layout.list_item_task, null);
            viewHolder = new ViewHolder(inflate, listener);
            inflate.setTag(viewHolder);
            convertView = inflate;

        }
        viewHolder.bindData(getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public static class ViewHolder implements View.OnClickListener {
        private final ItemListener itemListener;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.root)
        View root;


        @Bind(R.id.createTime)
        TextView createTime;

        @Bind(R.id.download)
        TextView download;

        @Bind(R.id.view)
        TextView view;

        @Bind(R.id.feedback)
        TextView feedback;

//        @Bind(R.id.kml)
//        TextView kml;

        public ViewHolder(View v, ItemListener itemListener) {

            this.itemListener = itemListener;
            try {
                ButterKnife.bind(this, v);
            }catch (Throwable t)
            {
                t.printStackTrace();
            }

            root.setOnClickListener(this);
            download.setOnClickListener(this);
            view.setOnClickListener(this);
            feedback.setOnClickListener(this);

        }


        public void bindData(Task data) {



            name.setText(data.name);
            createTime.setText(String.valueOf(data.created));


            download.setTag(data);
            view.setTag(data);
            root.setTag(data);
            feedback.setTag(data);


            feedback.setText(!SynchronizeCenter.waitForFeedBack(data)?"反馈":"待同步");

        }

        @Override
        public void onClick(View v) {

            Task tag = (Task) v.getTag();
            if (tag == null) return;
            switch (v.getId()) {
                case R.id.download:
                    itemListener.download(tag);
                    break;
                case R.id.view:
                    itemListener.view(tag);
                    break;
                case R.id.feedback:
                    itemListener.onFeedBack(tag);

                    break;
                case R.id.root:
                    itemListener.onDetail(tag);

                    break;
            }
        }
    }


    public static class GroupViewHolder implements View.OnClickListener {

        @Bind(R.id.dir_name)
        TextView dir_name;


        public GroupViewHolder(View v ) {


            try {
                ButterKnife.bind(this, v);
            }catch (Throwable t)
            {
                t.printStackTrace();
            }



        }


        public void bindData(Directory data,boolean  isExpand) {



            dir_name.setText(data.dir_name);
            dir_name.setSelected(isExpand);


        }

        @Override
        public void onClick(View v) {


        }
    }

    public interface ItemListener {
        public void onFeedBack(Task task);

        void download(Task task);

        void view(Task task);

        void onDetail(Task task);
    }
}
