package com.rnmap_wb.android.data;



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
    public int errno;


    /**
     * 状态的消息说明
     */
    public String errmsg;

    public int pageIndex=0;
    public int pageCount=1;
    public int pageSize=1;
    public List<T> data;
    public int totalCount;




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

        errno=CODE_SUCCESS;
        data =new ArrayList<>();

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
    public  static final int CODE_UNLOGIN=9997;
    /**
     * 是否成功的调用
     * @return
     */
    public boolean isSuccess() {
        return errno==CODE_SUCCESS;
    }
}
