package com.giants3.hd.parser;

import com.giants3.hd.noEntity.app.QuotationDetail;


/**
 * 桌面端Product 转换成app 端AProduct数据
 * <p/>
 * Created by david on 2016/1/2.
 */


//默认的 qualifier  为首字母小写的类名
public class QuotationDetailParser implements DataParser<com.giants3.hd.noEntity.QuotationDetail, QuotationDetail> {


    @Override
    public QuotationDetail parse(com.giants3.hd.noEntity.QuotationDetail data) {
        QuotationDetail aQuotation = new QuotationDetail();


        return aQuotation;
    }


}
