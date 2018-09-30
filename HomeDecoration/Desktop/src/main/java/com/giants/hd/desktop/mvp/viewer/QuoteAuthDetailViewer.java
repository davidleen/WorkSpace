package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.User;

import java.util.List;

/**
 *
 *   报价权限明细详情界面接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface QuoteAuthDetailViewer extends IViewer {


    /**
     * 显示关联的业务员
     * @param salesList
     */
    public void showAllSales(List<User> salesList);


    public void bindRelateSalesData(List<Integer> indexs);


    void showQuoteAuthList(List<QuoteAuth> quoteAuth);
}
