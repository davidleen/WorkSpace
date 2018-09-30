package com.giants.hd.desktop.mvp.viewer;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants.hd.desktop.mvp.IViewer;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.noEntity.ErpStockOutDetail;

import java.util.List;
import java.util.Set;

/**出库详情展示接口
 * Created by davidleen29 on 2016/7/18.
 */
public interface StockOutDetailViewer extends IViewer {



    public void setStockOutDetail(ErpStockOutDetail erpStockOutDetail);




    /**
     * 设置柜号信息
     * @param guiInfos
     */
    void  showGuihaoData(Set<StockOutDetailFrame.GuiInfo> guiInfos);

    /**
     * 显示出库单项目
     * @param itemList
     */
    void showItems(List<ErpStockOutItem> itemList);

    /**
     * 设置是否编辑权限
     * @param b
     */
    void setEditable(boolean b);

    void setExportable(boolean b);

    void setStockOutPriceVisible(boolean visible);

    /**
     * 显示拆分对话框
     * @param stockOutItem
     */
    void showSplitDialog(ErpStockOutItem stockOutItem);
}
