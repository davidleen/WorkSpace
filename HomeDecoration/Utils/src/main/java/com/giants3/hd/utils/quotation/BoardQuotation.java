package com.giants3.hd.utils.quotation;

import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.MaterialClass;
import com.giants3.hd.entity.ProductMaterial;

/**
 * 板材类材料配额 利用率计算
 * Created by davidleen29 on 2017/8/5.
 */
public class BoardQuotation {


    /**
     * 板材类 需要重新计算利用率， 开法，材料规格
     * @param productMaterial
     * @param material
     */

    public static final CutResult update(ProductMaterial productMaterial, Material material)
    {
        float[] wh=getWHOfMaterial(productMaterial,material);



        if(productMaterial.wLong>0&&productMaterial.wWidth>0)
        {


            int cutcount=0;
            int widthCount=0;
            int longCount=0;
            //进行裁剪  片数最多 利用率高。
            final int longCount1 = (int) (wh[0] / productMaterial.wLong);
            final int widthCount1 = (int) (wh[1] / productMaterial.wWidth);

            final int longCount2 = (int) (wh[0] / productMaterial.wWidth);
            final int widthCount2 = (int) (wh[1] / productMaterial.wLong);


            int cutCount1=longCount1*widthCount1;
            int cutCount2=longCount2*widthCount2;

            if(cutCount1>cutCount2)
            {
                cutcount=cutCount1;
                longCount=longCount1;
                widthCount=widthCount1;
            }else
            {
                cutcount=cutCount2;
                longCount=longCount2;
                widthCount=widthCount2;
            }



            float available= FloatHelper.scale(cutcount* productMaterial.wLong*productMaterial.wWidth/(wh[0]*wh[1]));

            CutResult cutResult=new CutResult();
            cutResult.available=available;
            cutResult.cutWay=longCount+"*"+widthCount;
            cutResult.spec=wh[0]+"*"+wh[1];

            return cutResult;
        }



        return null;








    }



    public static float[] getWHOfMaterial(ProductMaterial productMaterial, Material material)
    {

        float[] wh=new float[2];

        if(!StringUtils.isEmpty(productMaterial.spec))
        {

            try{
               String[] temp= productMaterial.spec.split("\\*");
                wh[1]=Float.valueOf(temp[1]);
                wh[0]=Float.valueOf(temp[0]);

            }catch ( Throwable t)
            {}




        }else
        {
            //木板 默认宽高
            if(isMuBAN(productMaterial))
            {
                wh[0]=122;
                wh[1]=244;
            }else
            if(isTIEBAN(productMaterial)) {
                //铁板默认宽高度
                wh[0] = 100;
                wh[1] = 200;
            }
        }

        return wh;

    }


    public static boolean work(ProductMaterial productMaterial) {

        if(StringUtils.isEmpty(productMaterial.classId)) return false;
        for (String code : MaterialClass.CLASS_BANGLEI) {
            if ( productMaterial.classId.equals(code)) {
                return true;
            }
        }


        return false;
    }

    public static boolean isMuBAN(ProductMaterial productMaterial)
    {
        if(StringUtils.isEmpty(productMaterial.classId)) return false;
        for (String code : MaterialClass.CLASS_BANGLEI_MU) {
            if (  productMaterial.classId.equals(code)) {
                return true;
            }
        }


        return false;
    }
    public static boolean isTIEBAN(ProductMaterial productMaterial)
    {
        if(StringUtils.isEmpty(productMaterial.classId)) return false;
        for (String code : MaterialClass.CLASS_BANGLEI_TIE) {
            if (  productMaterial.classId.equals(code)) {
                return true;
            }
        }


        return false;
    }


    public static class CutResult
    {

        public float available;
        public String cutWay;
        public String spec;
    }
}
