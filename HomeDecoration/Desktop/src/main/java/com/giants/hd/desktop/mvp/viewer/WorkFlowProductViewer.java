package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.WorkFlowProduct;

/**
 * 产品关联的生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowProductViewer extends IViewer {


    void setData(WorkFlowProduct workFlowProduct);

    /**
     * 获取用户编辑结果
     * @param data
     */
   void getData(WorkFlowProduct  data);
}
