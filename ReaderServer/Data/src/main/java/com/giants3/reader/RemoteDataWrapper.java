package com.giants3.reader;

import com.giants3.reader.noEntity.RemoteData;

import java.util.ArrayList;
import java.util.List;

public class RemoteDataWrapper {


    /**
     * 封装正常的返回结果。
     *
     * @param datas
     * @param <T>
     * @return
     */
    public static  <T> RemoteData<T> wrapData(List<T> datas) {


        return wrapData(0, datas == null ? 0 : datas.size(), 1, datas == null ? 0 : datas.size(), datas);


    }


    /**
     * 封装正常的返回结果。
     *
     * @param <T>
     * @return
     */
    public static <T> RemoteData<T> wrapData() {


        return wrapData(null);


    }
    /**
     * 封装正常的返回结果。
     *
     * @param datas
     * @param <T>
     * @return
     */

    public static  <T> RemoteData<T> wrapData(int pageIndex, int pageSize, int pageCount, int totalCount, List<T> datas)
    {
        return wrapData(pageIndex,pageSize,pageCount,totalCount,datas, RemoteData.CODE_SUCCESS,"");
    }
    /**
     * 封装正常的返回结果。
     *
     * @param datas
     * @param <T>
     * @return
     */

    public static  <T> RemoteData<T> wrapData(int pageIndex, int pageSize, int pageCount, int totalCount, List<T> datas,int code, String message) {

        RemoteData<T> remoteData = new RemoteData<T>();
        remoteData.pageIndex = pageIndex;
        remoteData.pageSize = pageSize;
        remoteData.pageCount = pageCount;
        remoteData.totalCount = totalCount;

        int defaultSize = datas == null || datas.size() < 10 ? 10 : datas.size();
        remoteData.datas = new ArrayList<T>(defaultSize);
        if (datas != null)
            remoteData.datas.addAll(datas);
        remoteData.code = code;

        remoteData.message =message;
        return remoteData;


    }
}
