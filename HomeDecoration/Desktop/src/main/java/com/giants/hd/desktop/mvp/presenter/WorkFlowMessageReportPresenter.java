package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.IPresenter;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface WorkFlowMessageReportPresenter extends IPresenter {


    void exportExcel(List<TableField> fieldList);

    void search(String dateStart, String dateEnd, boolean unhandle, boolean overdue);
}
