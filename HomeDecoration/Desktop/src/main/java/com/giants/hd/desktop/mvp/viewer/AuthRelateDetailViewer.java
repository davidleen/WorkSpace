package com.giants.hd.desktop.mvp.viewer;

import com.giants3.hd.entity.app.AppQuoteAuth;

import java.util.List;

/**
 *
 *   权限明细类界面展示曾
 * Created by davidleen29 on 2016/8/1.
 */
public interface AuthRelateDetailViewer extends  OrderAuthDetailViewer,StockOutAuthDetailViewer,QuoteAuthDetailViewer {
    void showPaneAndRow(int selectedPane, int showRow);

    void showQuoteRow(int showRow);

    void showOrderRow(int showRow);

    void showStockOutRow(int showRow);

    void showAppQuoteAuthList(List<AppQuoteAuth> quoteAuth);
}
