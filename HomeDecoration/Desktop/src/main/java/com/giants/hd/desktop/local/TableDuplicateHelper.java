package com.giants.hd.desktop.local;

import com.giants3.hd.utils.ArrayUtils;

import java.util.List;

/**
 *
 * 表格复制帮助类。
 * Created by davidleen29 on 2015/7/19.
 */
public class TableDuplicateHelper {


    private  static  List bufferData;


    public static boolean hasBufferData()
    {
        return !ArrayUtils.isEmpty(bufferData);
    }


    public static <T> void  saveBufferData(List<T> data)
    {
        bufferData=data;
    }




    public static <T> List<T>  getBufferData( )
    {

        return bufferData;
    }

    public static void clear()
    {
        bufferData=null;
    }
}
