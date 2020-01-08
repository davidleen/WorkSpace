package com.giants3.hd.noEntity.app;

import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by david on 2016/2/16.
 */
public class QuotationDetail implements Serializable {



   public  Quotation quotation;
    public    List<QuotationItem> items;

}
