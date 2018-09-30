package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

/**
 * presenter layer of MVP
 */
public interface QuotationDetailIPresenter extends IPresenter {


    public void save();


    void delete();

    void unVerify();

    void verify();
}
