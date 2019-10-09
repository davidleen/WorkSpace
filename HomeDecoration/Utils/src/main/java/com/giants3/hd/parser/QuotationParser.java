package com.giants3.hd.parser;

import com.giants3.hd.entity.app.Quotation;


/**  桌面端Quotation 转换成app Quotation
 *
 * Created by david on 2016/1/2.
 */


//默认的 qualifier  为首字母小写的类名  productParser
public class QuotationParser implements DataParser<com.giants3.hd.entity.Quotation,Quotation> {
    @Override
    public Quotation parse(com.giants3.hd.entity.Quotation data) {
        Quotation aQuotation=new Quotation();

        aQuotation.qNumber=data.qNumber;
        aQuotation.id=data.id;
        aQuotation.salesman=data.salesman;
        aQuotation.vDate=data.vDate;
        aQuotation.qDate=data.qDate;


        return aQuotation;
    }
}
