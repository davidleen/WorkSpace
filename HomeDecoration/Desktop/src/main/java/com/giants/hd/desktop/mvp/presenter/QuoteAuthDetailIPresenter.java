package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

import java.util.List;

/**
 *
 *   报价权限明细详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface QuoteAuthDetailIPresenter extends IPresenter {




    void onRelateUsesSelected(List<Integer> indexes);
    /**
     * 报价明细记录被选中
     * @param row
     */
    void onQuoteAuthRowSelected(int row);
}
