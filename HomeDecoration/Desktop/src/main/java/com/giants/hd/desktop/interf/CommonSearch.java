package com.giants.hd.desktop.interf;

import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;

/**
 * 普通查询接口
 */
public interface CommonSearch<T> {


    void setAdditionalParam(java.util.Map<String, Object> param);

    RemoteData<T> search(String value, int pageIndex, int pageCount) throws HdException;

}
