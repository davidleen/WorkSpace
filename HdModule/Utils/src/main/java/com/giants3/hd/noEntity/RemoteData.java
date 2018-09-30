package com.giants3.hd.noEntity;

import com.giants3.hd.noEntity.ConstantData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络数据交换格式。
 */
public class RemoteData<T> implements Serializable{


    /**
     * 请求状态
     */
    public int code;


    /**
     * 状态的消息说明
     */
    public String message;

    public int pageIndex=0;
    public int pageCount=1;
    public int pageSize=1;
    public List<T> datas;
    public int totalCount;



    public boolean isTest= ConstantData.FOR_TEST;

    /**
     * 后台登陆效验码
     */
    public String token;


    /**
     * 最新版本号。 不同客户端返回不一样。
     */
    public int newVersionCode;
    public String newVersionName;


    public RemoteData()
    {

        code=CODE_SUCCESS;
        datas=new ArrayList<>();

    }

    /**
     * 请求成功状态码
     */
    public  static final int CODE_SUCCESS=0;

    /**
     * 请求失败状态码
     */
    public  static final int CODE_FAIL=-1;

    /**
     * 请求失败状态码
     */
    public  static final int CODE_UNLOGIN=2;
    /**
     * 是否成功的调用
     * @return
     */
    public boolean isSuccess() {
        return code==CODE_SUCCESS;
    }
}
