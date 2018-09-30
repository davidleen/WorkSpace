package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.OutFactory;

import java.util.List;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface OutFactoryIPresenter extends IPresenter {


    void saveData(List<OutFactory> datas)  ;
}
