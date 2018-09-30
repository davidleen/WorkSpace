package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;

import java.io.File;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface QuotationPictureExportPresenter extends IPresenter {


    void searchProduct(String key,boolean includeCopy);

    void exportPicture(File directory);

    void removeRows(int[] rows);
}
