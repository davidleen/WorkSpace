package com.giants3.hd.noEntity;

/**
 * 产品 类型
 * Created by davidleen29 on 2017/10/27.
 */
public class ProductType {



    public  static final int TYPE_NONE=0;

    public static  final String TYPE_NONE_NAME="无";

    public static final int  TYPE_TIE=1;
    public static  final String TYPE_TIE_NAME="铁";


    public  static final int TYPE_MU=2;
    public static  final String TYPE_MU_NAME="木";

    public  static final int TYPE_BOTH=TYPE_MU|TYPE_TIE;
    public static  final String TYPE_BOTH_NAME=TYPE_TIE_NAME+"|"+TYPE_MU_NAME;




    public int type;
    public String name;

    public static ProductType TIE;
    public static ProductType MU;
    public static ProductType NONE;

    public static ProductType BOTH;


    static {

        TIE=new ProductType();
        TIE.name=TYPE_TIE_NAME;
        TIE.type=TYPE_TIE;


       MU=new ProductType();
       MU.name=TYPE_MU_NAME;
       MU.type=TYPE_MU;


       NONE=new ProductType();
       NONE.name=TYPE_NONE_NAME;
       NONE.type=TYPE_NONE;



        BOTH=new ProductType();
        BOTH.name=TYPE_BOTH_NAME;
        BOTH.type=TYPE_BOTH;
    }


}
