package com.giants.hd.desktop.mvp.productprocess;

import com.giants.hd.desktop.mvp.IPresenter;

public interface ProductProcessUpdatePresenter extends IPresenter {
    void save();

    void delete();

    void updateName(String name);

    void updateMemo(String memo);

    void updateCode(String code);
}
