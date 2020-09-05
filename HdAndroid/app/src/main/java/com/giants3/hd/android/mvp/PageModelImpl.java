package com.giants3.hd.android.mvp;

import com.giants3.hd.noEntity.RemoteData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2017/11/25.
 */

public class PageModelImpl<T> implements PageModel<T> {


    private String key =            "";
    private RemoteData<T> remoteData;

    public void setKey(String key) {

        this.key = key;
    }


    public String getKey() {
        return key;
    }


    public void setRemoteData(RemoteData<T> remoteData) {



        if (remoteData.pageIndex == 0) {
            this.remoteData = remoteData;

        }else{
            this.remoteData.pageIndex=remoteData.pageIndex;
            this.remoteData.datas.addAll(remoteData.datas);
        }

    }

    public List<T> getDatas() {
        return remoteData.datas;
    }


    public RemoteData<T> getRemoteData() {


        return remoteData;
    }



    public boolean hasNextPage()
    {
        return  remoteData!=null&& remoteData.pageIndex+1<remoteData.pageCount;
    }


    @Override
    public int getPageIndex() {

        if(remoteData==null)return 0;
        return remoteData.pageIndex;

    }

    @Override
    public int getPageSize() {
        if(remoteData==null)return 20;
        return remoteData.pageSize;
    }
}
