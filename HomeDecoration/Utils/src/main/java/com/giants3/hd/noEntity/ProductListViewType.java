package com.giants3.hd.noEntity;

import java.util.Vector;

/**
 * Created by davidleen29 on 2017/10/15.
 */
public class ProductListViewType {





    public static  Vector<String> vector=new Vector<>();
    static {
        vector.add("    ");
        vector.add("白胚未处理");
        vector.add("组装未处理");
        vector.add("油漆未处理");
        vector.add("包装未处理");
    }


    public static final int  VIEWTYPE_NONE=0;
    public static final int  VIEWTYPE_PEITI_UNSET=1;
    public static final int  VIEWTYPE_ZUZHUANG_UNSET=2;
    public static  final int  VIEWTYPE_YOUQI_UNSETE=3;
    public static  final int  VIEWTYPE_BAOZHUANG_UNSETE=4;
}
