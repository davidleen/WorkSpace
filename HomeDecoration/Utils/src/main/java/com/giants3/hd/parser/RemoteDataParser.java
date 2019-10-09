package com.giants3.hd.parser;

import com.giants3.hd.noEntity.RemoteData;

import java.util.ArrayList;
import java.util.List;

/** 转换 工具类。
 * Created by david on 2016/1/2.
 */


public class RemoteDataParser {


    public static  <T,K> RemoteData<K>   parse(RemoteData<T> source, DataParser<T,K> parser)
    {

        RemoteData remoteData=source;
        if(source.datas!=null&&source.datas.size()>0)
        {

            List<T> sourceList=new ArrayList<>(source.datas.size());
            sourceList.addAll(source.datas);
            source.datas.clear();

            for(T t :sourceList)
            {
                K k=   parser.parse(t);
                remoteData.datas.add(k);

            }


        }
        return ( RemoteData<K>)remoteData;

    }
}
