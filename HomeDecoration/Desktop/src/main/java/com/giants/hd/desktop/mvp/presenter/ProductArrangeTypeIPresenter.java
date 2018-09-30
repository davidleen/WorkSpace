package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.WorkFlowSubType;

import java.util.List;

/**
 * 产品排厂类型
 * 二级流程 铁件 木件  PU，其他
 * Created by davidleen29 on 2017/2/19.
 */
public interface ProductArrangeTypeIPresenter extends IPresenter {
    void saveData(List<WorkFlowSubType> newData);
}
