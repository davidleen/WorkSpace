package com.giants.hd.desktop.mvp;


/**
 * Interface representing a IPresenter in a model viewImpl presenter (MVP) pattern.
 */
public interface IPresenter {


//    void create();
    void close();

    /**
     * 判断数据是否修改接口
     * @return
     */
    boolean  hasModifyData();

}
