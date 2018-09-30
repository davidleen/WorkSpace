package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.MaterialClass;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public interface MaterialClassUpdateViewer extends IViewer {
    void bindData(MaterialClass materialClass);

    void getData(MaterialClass materialClass);
}
