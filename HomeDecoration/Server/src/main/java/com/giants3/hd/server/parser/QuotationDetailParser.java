package com.giants3.hd.server.parser;

import com.giants3.hd.noEntity.app.QuotationDetail;
import org.springframework.stereotype.Component;

/**
 * 桌面端Product 转换成app 端AProduct数据
 * <p/>
 * Created by david on 2016/1/2.
 */

@Component
//默认的 qualifier  为首字母小写的类名
public class QuotationDetailParser implements DataParser<com.giants3.hd.noEntity.QuotationDetail, QuotationDetail> {


    @Override
    public QuotationDetail parse(com.giants3.hd.noEntity.QuotationDetail data) {
        QuotationDetail aQuotation = new QuotationDetail();


        return aQuotation;
    }


}
