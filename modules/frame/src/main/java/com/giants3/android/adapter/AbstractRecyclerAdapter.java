package com.giants3.android.adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.giants3.android.frame.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * a abstract class do some job to reduce code about bind view to holder and
 * easy on use baseAdapter
 *
 * @param <D>
 * @author davidleen29
 * @创建时间 2013年11月14日
 */
public abstract class AbstractRecyclerAdapter<V extends AbsRecycleViewHolder<D>, D> extends RecyclerView.Adapter<V> {

    public static final  int UNLIMITED_SIZE_MULTI= 1000;
    protected Context context;
    protected String TAG;
    protected LayoutInflater inflater;
    protected List<D> datas;
    private List<D> selectItems = new ArrayList<>();
    private boolean unlimited;
    public AbstractRecyclerAdapter(Context context) {
        this(context, null);
    }
    /**
     * set the datas Arrays and notify update
     *
     * @param arrayData
     */
    public void setDataArray(D[] arrayData) {

        setDataArray(Arrays.asList(arrayData));

    }
    /**
     * set the datas Arrays and notify update
     *
     * @param arrayData
     */
    public void setDataArray(List<D> arrayData) {
        datas.clear();
        if (arrayData != null) {
            datas.addAll(arrayData);
        }

        notifyDataSetChanged();
    }


    private View.OnClickListener itemClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            D data= (D) v.getTag(R.id.item_click_wrap_data);
            int position= (int) v.getTag(R.id.item_click_position);
            if(listener!=null)
            {
                listener.onItemClick(data,position);
            }

        }
    };
    @Override
    public void onBindViewHolder(V holder, int position) {

        D item = getItem(position);
        if(unlimited)
            position=position%getRealCount();
        if(listener!=null)
        {
            holder.itemView.setTag(R.id.item_click_position, position);
            holder.itemView.setTag(R.id.item_click_wrap_data, item);
        }
        holder.itemView.setOnClickListener(listener!=null?itemClickListener:null);
        holder.bindData(item,position);

    }



    /**
     * constructor
     *
     * @param context
     * @param initDatas
     */
    public AbstractRecyclerAdapter(Context context, List<D> initDatas) {

        this.context = context;
        TAG = this.getClass().getName();
        if (initDatas == null) {
            this.datas = new ArrayList<D>();
        } else {
            this.datas = initDatas;
        }

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {

        int size = datas.size();
        if (unlimited) {

            size *= UNLIMITED_SIZE_MULTI;
        }

        return size;

    }



    public boolean isSelected(D item)
    {
        return selectItems.contains(item);
    }



    public void  addSelectedItem(D item)
    {
        selectItems.add(item);
    }

    public void  removeSelectedItem(D item)
    {
        selectItems.remove(item);
    }



    public void selectAll()
    {


        selectItems.clear();
        selectItems.addAll(datas);
    }



    public void clearSelectItems()
    {
        selectItems.clear();
    }

    public boolean hasSelectedItem()
    {
        return selectItems.size()>0;
    }


    public List<D> getSelectItems()
    {
        return selectItems;
    }

    public void setSelectItems(List<D> selectItems)
    {
        this.selectItems.clear();
        this.selectItems.addAll(selectItems);
    }

    public void setSelectItem(D selectItem)
    {
        this.selectItems.clear();
        if(selectItem!=null)
            this.selectItems.add(selectItem);
    }
    public void setSelectPosition(int position)
    {
        this.selectItems.clear();
        if(position>=0&&position<getItemCount())
            this.selectItems.add(getItem(position));
    }



    public List<D> getItems()
    {
        return datas;
    }

    public void moveItem(int fromIndex, int toIndex) {


        if(fromIndex==toIndex) return;

        D remove = datas.remove(fromIndex);

        datas.add(toIndex,remove);

    }

    public void addItem(int index, D item) {


        datas.add(index,item);

    }

    public void removeItem(D item) {


        selectItems.remove(item);
        datas.remove(item);
    }





    private int getRealCount()
    {
        return datas.size();
    }


    public D getItem(int position)
    {


        if(unlimited)
        {
            position=position% getRealCount();
        }


        return datas.get(position);
    }




    public void addItem(D data) {

        datas.add(data);
    }





    public List<D> getDatas() {
        return datas;
    }

    public void addDataArray(List<D> arrayData) {

        if (arrayData != null) {
            datas.addAll(arrayData);
        }

        notifyDataSetChanged();

    }


    protected Context getContext() {
        return context;
    }


    private OnItemClickListener<D> listener;

    public void setOnItemClickListener(OnItemClickListener<D> listener) {
        this.listener = listener;
    }


    public static interface OnItemClickListener<D>
    {
        void onItemClick(D item,int position);
    }
}
