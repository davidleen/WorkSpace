package com.giants3.reader.server.noEntity;

/**
 * Created by davidleen29 on 2017/7/23.
 */
public class UmengPushResult {


    public static  final String RET_SUCCESS="SUCCESS";
    public static  final String RET_FAIL="FAIL";

    public String ret;
    public PushResult data;

    private class PushResult {

        public String msg_id;
        public String task_id;
        public String error_code;
        public int status;
        public int accept_count;
        public int open_count;
        public int dismiss_count;


    }
}
