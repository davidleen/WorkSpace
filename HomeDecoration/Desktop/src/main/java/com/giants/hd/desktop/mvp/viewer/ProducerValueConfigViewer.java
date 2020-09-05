package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.ProducerValueConfig;
import com.giants3.hd.entity.WorkFlowMessage;

import java.util.List;

/**
 *  产能配置界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface ProducerValueConfigViewer extends IViewer {



    public void bindData(List<ProducerValueConfig> datas)  ;


    boolean hasModify(String originData);

    void completeEdit();

}
