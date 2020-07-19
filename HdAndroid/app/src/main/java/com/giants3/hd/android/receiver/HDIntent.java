package com.giants3.hd.android.receiver;

import com.giants3.hd.android.HdApplication;

public class HDIntent {



    public static final class Message
    {
        public static final String ACTION=HdApplication.baseContext.getPackageCodePath()+".newmessagecount";
        public static final String KEY_WORK_FLOW_MESSAGE_COUNT ="work_flow_message_count";
        public static final String KEY_WORK_FLOW_MONITOR_COUNT="work_flow_monitor_count";
    }
}
