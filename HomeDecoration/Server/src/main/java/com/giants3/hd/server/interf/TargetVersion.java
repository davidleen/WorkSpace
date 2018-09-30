package com.giants3.hd.server.interf;

/**
 * Created by davidleen29 on 2017/4/15.
 */
public @interface TargetVersion {



    int value() default 0;
    /**
     *
     *  version =1    从报价单恢复产品包装数据。
     *
     *
     */
    public static final int VERSION_RESTORE_PACK_FROM_QUOTATION=1;
}
