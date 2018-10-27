package com.giants.hd.desktop.model;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.utils.Config;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import org.apache.commons.collections.map.LRUMap;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public  abstract  class AbsTableModel<T> extends AbstractTableModel implements  CellConfig{






    private static final ImageIcon EMPTY_IMAGE_VALUE = new ImageIcon(new byte[0]);
    private static final ImageIcon LOADING_IMAGE_VALUE = new ImageIcon(AbsTableModel.class.getClassLoader().getResource("icons/loading.png"));
    protected List<T> datas;
    protected Class<T> itemClass;

    public AbsTableModel(Class<T> itemClass) {
        this(new ArrayList<T>(),itemClass);

    }

    public AbsTableModel(List<T> list,Class<T> itemClass) {
        this.itemClass = itemClass;
        datas=list;

    }

    //    private static StringBuilder sb = new StringBuilder();
//    ExecutorService service= Executors.newSingleThreadExecutor();
    protected Object loadImage(final int row, final int column, String url) {


//        Runtime runtime = Runtime.getRuntime();
//        sb.setLength(0);
//        sb.append("totalMemory:" + runtime.totalMemory() / 1024 / 1024).append("\n");
//        sb.append("freeMemory:" + runtime.freeMemory() / 1024 / 1024).append("\n");
//        sb.append("maxMemory:" + runtime.maxMemory() / 1024 / 1024).append("\n");
//
//
//     final   String message=sb.toString();
//        sb.setLength(0);
//        service .submit(new Runnable() {
//            @Override
//            public void run() {
//                LocalFileHelper.writeString("runtime.txt",message );
//            }
//        });


        String[] fileNames = url.split(";");

        if (fileNames == null || fileNames.length == 0 || StringUtils.isEmpty(fileNames[0])) return EMPTY_IMAGE_VALUE;
        final String destUrl = HttpUrl.loadPicture(fileNames[0]);
        ImageIcon data = (ImageIcon) pictureMaps.get(destUrl);
        if (data != null)

            if(Config.DEBUG)
            {

                Config.log("row:"+row+",url:"+destUrl+" ,hit  "+pictureMaps.size());
            }


        if (data == null) {

            ImageLoader.getInstance().displayImage(new Iconable() {
                @Override
                public void setIcon(ImageIcon icon, String url) {

                    pictureMaps.put(url, icon);
                    if(Config.DEBUG)
                    {

                        Config.log("row:"+row+",url:"+destUrl+",url2:"+url);
                        Config.log("url equals ? ="+url.equals(destUrl));
                    }
                   // fireTableCellUpdated(row, column);
                    fireTableDataChanged( );
                }

                @Override
                public void onError(String message) {
                    pictureMaps.put(destUrl, EMPTY_IMAGE_VALUE);

                }
            }, destUrl,    getColumnWidth( column), getRowHeight());

            return LOADING_IMAGE_VALUE;

        } else {
            return data;
        }


    }



    /**
     * 表格中使用的图片缓存
     * 异步加载的图片缓存
     * 最多50
     */
    private   LRUMap pictureMaps = new LRUMap(100, 0.75f, false);


    /**
     * 获取列宽
     * @param column
     * @return
     */

    public int getColumnWidth(int column)
    {

        return 50;
    }


    @Override
    public int[] getColumnWidth() {
        return null;
    }

    public void setDatas(List<T> newDatas) {


        this.datas.clear();

        if (newDatas != null)
            this.datas.addAll(newDatas);
        adjustRowCount();

        fireTableDataChanged();

    }

    protected   void adjustRowCount()
    {}

    /**
     * 返回 设置的行高
     *
     * @return
     */
    @Override
    public   int getRowHeight()
    {    return 0;}


    /**
     * 是否有.连接的字段
     * @param column
     * @return
     */
    protected boolean isCombineField(int column)
    {
        return false;
    }



    public T getItem(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= datas.size()) return null;
        return datas.get(rowIndex);
    }


    protected abstract  String getFieldName(int column) ;
    protected abstract Field getField(int column) ;
    public List<T> getDatas() {


        return datas;
    }
    @Override
    public int getRowCount() {
        return datas.size();
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        final String currentFieldName = getFieldName(columnIndex);
        if (currentFieldName.equalsIgnoreCase(ConstantData.COLUMN_INDEX)) {
            return rowIndex + 1;
        }



        T data =getItem(rowIndex);
        Object obj = null;
        //复合字段数据
        if(isCombineField(columnIndex))
        {
            String[] fields= currentFieldName.split(StringUtils.STRING_SPLIT_DOT);
            obj=data;
            for(String field:fields)
            {
                obj=getFieldData(obj,field);
            }

        }else {

            //单字段处理
            final Field field = getField(columnIndex);
            if (field != null) {
                try {
                    obj = field.get(data);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }else
            {
                obj=getFieldData(data,currentFieldName);

            }
        }
        if(obj  ==null) return null;
        if (currentFieldName.equalsIgnoreCase(ConstantData.COLUMN_ITM)) {

            try{
               return Integer.valueOf(obj.toString())+1;
            }catch (Throwable t){}

        }

        //数字为0  显示空字符串
        if (obj instanceof Number) {
            if (Math.abs(((Number) obj).floatValue()) <= 0.00001)
                return "";
            else if (obj instanceof Float) {
                //    make the float value 3.0  to show in 3 ,without the fraction.
                return FloatHelper.scale((Float) obj, 5);
            }
        } else
            //显示图片
            if (getColumnClass(columnIndex).equals(ImageIcon.class)) {
                if (obj instanceof byte[])
                    return new ImageIcon((byte[]) obj);
                //图片url
                if (obj instanceof String) {
                    return loadImage(rowIndex, columnIndex, (String) obj);
                }
            }

        return obj
                ;


    }


    /**
     * 从数据中读取指定字段的值
     * @param data
     * @param fieldName
     * @return
     */
    protected Object getFieldData(Object data, String fieldName )
    {
        Object result = null;
        try {
            result=   data.getClass().getField(fieldName).get(data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return result;
    }



}
