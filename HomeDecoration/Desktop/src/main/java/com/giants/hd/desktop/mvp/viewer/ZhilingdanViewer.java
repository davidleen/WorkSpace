package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity_erp.Zhilingdan;

import java.util.List;

/**
 *
 *      界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface ZhilingdanViewer extends IViewer {


    void setData(List<Zhilingdan> datas);


}
