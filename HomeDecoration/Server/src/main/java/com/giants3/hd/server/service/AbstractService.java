package com.giants3.hd.server.service;

import com.giants3.hd.noEntity.RemoteData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 2016/2/15.
 */
public  abstract  class AbstractService implements InitializingBean, DisposableBean {
    protected     Logger logger  ;
    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger=Logger.getLogger(getClass());

    }
    /**
     * 构造分页数据
     * @param pageIndex 从0开始
     * @param pageSize
     * @return
     */
    protected Pageable constructPageSpecification(int pageIndex, int pageSize) {
        return constructPageSpecification(pageIndex, pageSize,sortByParam(Sort.Direction.ASC,"id"));
    }

    /**
     * Returns a new object which specifies the the wanted result page.
     * @param pageIndex The flowStep of the wanted result page
     * @return
     */
    protected Pageable constructPageSpecification(int pageIndex, int pageSize,Sort sort) {
        Pageable pageSpecification = new PageRequest(pageIndex,  pageSize, sort);
        return pageSpecification;
    }

    /**
     * 构造排序方法
     * @param direction
     * @param column
     * @return
     */
    protected Sort sortByParam(Sort.Direction direction,String column) {
        return new Sort(direction, column);
    }


    /**
     * 封装正常的返回结果。
     * @param data
     * @param <T>
     * @return
     */
    public <T> RemoteData<T> wrapData(T data)
    {

        List<T> datas=new ArrayList<>();
        datas.add(data);

        return wrapData(0,datas.size(),1,datas.size(),datas);


    }


    /**
     * 封装正常的返回结果。
     * @param message   带消息参数
     * @param <T>
     * @return
     */
    public <T>        RemoteData<T> wrapMessageData(String message)
    {


        RemoteData<T> data=new RemoteData<>();
        data.message=message;

        return data;


    }

    /**
     * 封装正常的返回结果。
     * @param datas
     * @param <T>
     * @return
     */
    public <T>        RemoteData<T> wrapData(List<T> datas)
    {



        return wrapData(0,datas==null?0:datas.size(),1,datas==null?0:datas.size(),datas);


    }


    /**
     * 封装正常的返回结果。
     * @param <T>
     * @return
     */
    public <T>        RemoteData<T> wrapData( )
    {



        return wrapData(null);


    }


    /**
     *
     * @param pageIndex 当前页码
     * @param pageSize  一页大小
     * @param pageCount 总页数
     * @param totalCount 总记录数
     * @param datas
     * @param <T>
     * @return
     */

    public <T>        RemoteData<T> wrapData(int pageIndex,int pageSize,int pageCount,int totalCount,List<T> datas)
    {




        RemoteData<T> remoteData=new RemoteData<T>();
        remoteData.pageIndex=pageIndex;
        remoteData.pageSize=pageSize;
        remoteData.pageCount=pageCount;
        remoteData.totalCount=totalCount;

        int defaultSize=datas==null||datas.size()<10?10:datas.size();
        remoteData.datas  =new ArrayList<T>(defaultSize);
        if(datas!=null)
            remoteData.datas.addAll(datas);
        remoteData.code=RemoteData.CODE_SUCCESS;

        remoteData.message="operation sucessed";
        return remoteData;


    }



    /**
     * 封装正常的返回结果。
     * @param <T>
     * @return
     */

    public <T>        RemoteData<T> wrapData(int pageIndex,Page<T> page )
    {

        return   wrapData(pageIndex, page.getSize(), page.getTotalPages(), (int) page.getTotalElements(), page.getContent());


    }

    /**
     * 封装错误信息类。
     * @param message
     * @param <T>
     * @return
     */

    protected  <T> RemoteData<T> wrapError(String message) {


        RemoteData<T> remoteData=new RemoteData<T>();



        remoteData.code=RemoteData.CODE_FAIL;

        remoteData.message=message;
        return remoteData;

    }



}
