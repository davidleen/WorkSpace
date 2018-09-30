package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.IPresenter;

import java.util.List;

/**
 * Created by davidleen29 on 2018/9/17.
 */
public interface AppQuotationCountReportPresenter  extends IPresenter{
    void searchReport(String startDate, String endDate);

    void exportExcel(List<TableField> fieldList,
                     String directory, String fileName);
}
