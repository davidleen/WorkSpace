package com.giants.hd.desktop.mvp.presenter;

/**
 *
 *   权限明细详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface AuthRelateDetailIPresenter extends OrderAuthDetailIPresenter,StockOutAuthDetailIPresenter,QuoteAuthDetailIPresenter {


    void setSelectedPane(int selectedIndex);

    void save();
}
