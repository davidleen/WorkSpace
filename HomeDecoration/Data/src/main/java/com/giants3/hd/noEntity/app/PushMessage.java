package com.giants3.hd.noEntity.app;

/** 推送消息
 * Created by davidleen29 on 2018/6/25.
 */
public class PushMessage {

    /**
     * 消息类型
     */
    public int messageType;
    public String title;
    public String content;
    public String icon;
    public String ticker;
    /**
     * 消息自定义内容体
     */
    public String custom;

    /**
     * 消息处理通知。
     */
    public static  final int TYPE_MESSAGE_RECEIVE=2;


    /**
     * 消息处理 超时警告
     */
    public static  final int TYPE_MESSAGE_RECEIVE_ALERT=1;
}
