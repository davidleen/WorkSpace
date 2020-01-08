package com.giants3.hd.logic;

import com.giants3.hd.entity.GlobalData;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.entity.QuotationXKItem;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;

/** 报价单的逻辑
 * Created by davidleen29 on 2018/5/20.
 */
public class QuotationAnalytics {


    public static void updateProduct(QuotationItem quotationItem, Product product, GlobalData globalData) {


        quotationItem.productId =product.id;
        quotationItem.thumbnail =product.thumbnail;
        quotationItem.photoUrl =product.url;
        quotationItem.productName =product.name;
        quotationItem.pVersion =product.pVersion;
        quotationItem.inBoxCount =product.insideBoxQuantity;
        quotationItem.packQuantity =product.packQuantity;
        quotationItem.packageSize = StringUtils.combineNumberValue(product.packLong, product.packWidth, product.packHeight);


        quotationItem.unit =product.pUnitName;
        quotationItem.cost = FloatHelper.scale(product.cost);
        if(quotationItem.cost_price_ratio <=0)
            quotationItem.cost_price_ratio =globalData.cost_price_ratio;
        quotationItem.price=  getPriceBaseOnCostRatio( product,quotationItem.cost_price_ratio,globalData);



        quotationItem.volumeSize =product.getPackVolume();
        quotationItem.weight =FloatHelper.scale(product.weight);
        quotationItem.spec =product.spec;
        quotationItem.constitute =product.constitute;
        quotationItem.mirrorSize =product.mirrorSize;
        quotationItem.memo =product.memo;


    }

    /**
     * 根据指定产品，设定指定成本利润比，获得fob值
     * @param product
     * @param newCost_price_ratio
     * @param globalData
     * @return
     */
    public static float getPriceBaseOnCostRatio( Product product, float newCost_price_ratio,GlobalData globalData)
    {



         return
                 ProductAnalytics.getFobFromCost(product,newCost_price_ratio,globalData.manageRatioForeign,globalData.addition,globalData.price_of_export,globalData.exportRate);
    }


    /**
     * 更新咸康报价产品
     * @param quotationXKItem
     * @param product
     */
    public static void updateProduct(QuotationXKItem quotationXKItem, Product product,GlobalData globalData) {





        quotationXKItem.productId =product.id;
       quotationXKItem.thumbnail =product.thumbnail;
        quotationXKItem.photoUrl =product.url;
        quotationXKItem.productName =product.name;
        quotationXKItem.pVersion =product.pVersion;
        quotationXKItem.inBoxCount =product.insideBoxQuantity;
        quotationXKItem.packQuantity =product.packQuantity;
        quotationXKItem.packageSize =StringUtils.combineNumberValue(product.packLong, product.packWidth, product.packHeight);


        quotationXKItem.unit =product.pUnitName;
        quotationXKItem.cost = FloatHelper.scale(product.cost);
        final float cost_price_ratio;
        if( quotationXKItem.cost_price_ratio<=0)
            cost_price_ratio = globalData.cost_price_ratio;
        else
            cost_price_ratio=quotationXKItem.cost_price_ratio;
        quotationXKItem.price=  getPriceBaseOnCostRatio( product, cost_price_ratio,globalData);
        //默认不带动fob
        // price=0;
        //price=FloatHelper.scale(product.fob);

        quotationXKItem.volumeSize =FloatHelper.scale(product.getPackVolume(),3);
        quotationXKItem.weight =FloatHelper.scale(product.weight);
        quotationXKItem.spec =product.spec;
        quotationXKItem.constitute =product.constitute;
        quotationXKItem.mirrorSize =product.mirrorSize;
        quotationXKItem.memo =product.memo;




    }

    /**
     * 更新咸康报价产品2
     * @param quotationXKItem
     * @param product
     */
    public static void updateProduct2(QuotationXKItem quotationXKItem, Product product,GlobalData globalData) {


        quotationXKItem.productId2 =product==null?0:product.id;
       quotationXKItem.thumbnail2 =product==null?null:product.thumbnail;
        quotationXKItem.photo2Url =product==null?"":product.url;

        quotationXKItem.productName2 =product==null?"":product.name;
        quotationXKItem.pVersion2 =product==null?"":product.pVersion;
        quotationXKItem.inBoxCount2 =product==null?0:product.insideBoxQuantity;
        quotationXKItem.packQuantity2 =product==null?0:product.packQuantity;
        quotationXKItem.packageSize2 =product==null?"":StringUtils.combineNumberValue(product.packLong, product.packWidth, product.packHeight);


        quotationXKItem.unit2 =product==null?"":product.pUnitName;
        quotationXKItem.cost2 =product==null?0:FloatHelper.scale(product.cost);
        final float cost_price_ratio;
        if( quotationXKItem.cost_price_ratio<=0)
            cost_price_ratio = globalData.cost_price_ratio;
        else
            cost_price_ratio=quotationXKItem.cost_price_ratio;
        quotationXKItem.price2=  getPriceBaseOnCostRatio( product,cost_price_ratio,globalData);


        quotationXKItem.volumeSize2 =product==null?0:FloatHelper.scale(product.getPackVolume());
        quotationXKItem.weight2 =product==null?0:FloatHelper.scale(product.weight);
        quotationXKItem.spec2 =product==null?"":product.spec;
        quotationXKItem.constitute2 =product==null?"":product.constitute;
        quotationXKItem.mirrorSize2 =product==null?"":product.mirrorSize;
        quotationXKItem.memo2 =product==null?"":product.memo;


    }
}
