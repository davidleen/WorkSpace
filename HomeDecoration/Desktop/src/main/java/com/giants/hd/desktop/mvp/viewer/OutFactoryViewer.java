package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.OutFactory;

import java.util.List;

/**
 * Created by davidleen29 on 2017/1/15.
 */
public  interface OutFactoryViewer  extends IViewer {
    void setData(List<OutFactory> datas);
}
