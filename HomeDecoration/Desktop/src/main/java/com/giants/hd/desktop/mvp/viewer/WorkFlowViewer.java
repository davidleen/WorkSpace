package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.User;

import java.util.List;

/**
 *
 *    生产流程 界面层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface WorkFlowViewer extends IViewer {


    void setData(List<ErpWorkFlow> datas);

    void setUserList(List<User> users);
}
