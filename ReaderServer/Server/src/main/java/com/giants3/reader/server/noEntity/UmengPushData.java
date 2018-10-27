package com.giants3.reader.server.noEntity;

import com.giants3.reader.server.service.PushService;

/**
 * Created by davidleen29 on 2017/7/21.
 */
public class UmengPushData {


    /**
     *  // 必填 应用唯一标识
     */
    public String appkey= PushService.UMENG_APP_KEY;
    /**
     * // 必填 时间戳，10位或者13位均可，时间戳有效期为10分钟
     */
    public String timestamp= Integer.toString((int) (System.currentTimeMillis() / 1000));
    /**
     * // 必填 消息发送类型,其值可以为:
     unicast-单播
     listcast-列播(要求不超过500个device_token)
     filecast-文件播
     (多个device_token可通过文件形式批量发送）
     broadcast-广播
     groupcast-组播
     (按照filter条件筛选特定用户群, 具体请参照filter参数)
     customizedcast(通过开发者自有的alias进行推送),
     包括以下两种case:
     - alias: 对单个或者多个alias进行推送
     - file_id: 将alias存放到文件后，根据file_id来推送
     */
    public String type;


    /**
     * // 可选 设备唯一表示
     当type=unicast时,必填, 表示指定的单个设备
     当type=listcast时,必填,要求不超过500个,
     多个device_token以英文逗号间隔
     */
    public String device_tokens;

    /**
     * // 可选 当type=customizedcast时，必填，alias的类型,
     alias_type可由开发者自定义,开发者在SDK中
     调用setAlias(alias, alias_type)时所设置的alias_type
     */
    public String alias_type;

    /**
     * // 可选 当type=customizedcast时, 开发者填写自己的alias。
     要求不超过50个alias,多个alias以英文逗号间隔。
     在SDK中调用setAlias(alias, alias_type)时所设置的alias
     */
    public String alias;



    public String file_id;


    public String filter;


    public PushData payload;
    /**
     *  // 可选 发送消息描述，建议填写
     */
    public String description;

    public static class PushData {



        public static final String TYPE_MESSAGE="message";
        public static final String TYPE_NOTIFICATION="notification";
        /**
         *  // 必填 消息类型，值可以为:
         notification-通知，message-消息
         */
        public  String  display_type;

        /**
         *   // 必填 消息体。
         display_type=message时,body的内容只需填写custom字段。
         display_type=notification时, body包含如下参数:
         */
        public Data body;


    }


    /**
     *   // 通知展现内容:
     */
    public static  class Data {

        /**
         * // 必填 通知栏提示文字
         */
        public String ticker;
        /**
         * // 必填 通知标题
         */
        public String title;
        /**
         * // 必填 通知文字描述
         */
        public String text;
        /**
         * / 可选 状态栏图标ID, R.drawable.[smallIcon],
         如果没有, 默认使用应用图标。
         图片要求为24*24dp的图标,或24*24px放在drawable-mdpi下。
         注意四周各留1个dp的空白像素
         */
        public String icon;
        /**
         * // 可选
         */
        public String largeIcon;
        /**
         * // 可选 通知栏大图标的URL链接。该字段的优先级大于largeIcon。
         该字段要求以http或者https开头。
         */
        public String img;
        /**
         * 自定义内容
         */
        public String custom;


        /**
         *  // 点击"通知"的后续行为，默认为打开app。
         "after_open": "xx" // 必填 值可以为:
         "go_app": 打开应用
         "go_url": 跳转到URL
         "go_activity": 打开特定的activity
         "go_custom": 用户自定义内容。
         */
        public String after_open;
    }
}
