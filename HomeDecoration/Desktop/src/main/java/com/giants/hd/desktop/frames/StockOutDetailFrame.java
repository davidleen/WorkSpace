package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.presenter.StockOutDetailIPresenter;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockOut_Invoice;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockOut_List;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockOut_Qingguan_Invoice;
import com.giants.hd.desktop.reports.excels.Report_Excel_StockOut_XK_Invoice;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.mvp.viewer.StockOutDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_StockOutDetail;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import rx.Subscriber;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 出库单详情模块
 */
public class StockOutDetailFrame extends BaseFrame implements StockOutDetailIPresenter {


    StockOutDetailViewer stockOutDetailViewer;

    //柜号数组信息
    Set<GuiInfo> guiInfos = new HashSet<>();
    private String oldData;
    private ErpStockOutDetail erpStockOutDetail;

    public StockOutDetailFrame(final ErpStockOut stockOut) {

        super("出库单：" + stockOut.ck_no);
        stockOutDetailViewer = new Panel_StockOutDetail(this);
        init();
        readData(stockOut);
    }

    private void readData(ErpStockOut erpStockOut) {

        UseCaseFactory.getInstance().createStockOutDetailUseCase(erpStockOut.ck_no).execute(new Subscriber<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void onCompleted() {
                stockOutDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                stockOutDetailViewer.hideLoadingDialog();
                stockOutDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpStockOutDetail> erpOrderRemoteData) {
                if (erpOrderRemoteData.isSuccess()) {
                    setErpStockOutDetail(erpOrderRemoteData.datas.get(0));
                }


            }
        });

    }


    public void init() {
        setContentPane(stockOutDetailViewer.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();


        //设置权限相关
        stockOutDetailViewer.setEditable(AuthorityUtil.getInstance().editStockOut());
        //设置权限相关
        stockOutDetailViewer.setExportable(AuthorityUtil.getInstance().exportStockOut());

        stockOutDetailViewer.setStockOutPriceVisible(CacheManager.getInstance().isStockOutPriceVisible());
    }

    @Override
    public boolean hasModifyData() {
        return !GsonUtils.toJson(erpStockOutDetail).equals(oldData);
    }

    private void setErpStockOutDetail(ErpStockOutDetail newDetail) {

        oldData = GsonUtils.toJson(newDetail);
        erpStockOutDetail = newDetail;

        stockOutDetailViewer.setStockOutDetail(newDetail);

        guiInfos.clear();
        //整理出柜号信息数据
        for (ErpStockOutItem item : erpStockOutDetail.items) {
            if (StringUtils.isEmpty(item.guihao) && StringUtils.isEmpty(item.fengqianhao))
                continue;
            guiInfos.add(new GuiInfo(item.guihao, item.fengqianhao, item.guixing));
        }

        stockOutDetailViewer.showGuihaoData(guiInfos);


    }


    @Override
    public void save() {


        UseCaseFactory.getInstance().saveStockOutDetail(erpStockOutDetail).execute(new Subscriber<RemoteData<ErpStockOutDetail>>() {
            @Override
            public void onCompleted() {
                stockOutDetailViewer.hideLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {

                stockOutDetailViewer.hideLoadingDialog();
                stockOutDetailViewer.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(RemoteData<ErpStockOutDetail> remoteData) {
                stockOutDetailViewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    setErpStockOutDetail(remoteData.datas.get(0));

                    stockOutDetailViewer.showMesssage("保存成功!");
                } else {

                    stockOutDetailViewer.showMesssage("保存失败!" + remoteData.message);
                }


            }
        });

        stockOutDetailViewer.showLoadingDialog("正在保存。。。");
    }


    @Override
    public void delete() {


    }

    @Override
    public void verify() {

    }


    @Override
    public void onCemaiChange(String value) {

        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;
        erpStockOutDetail.erpStockOut.cemai = value;
    }

    @Override
    public void onNeihemaiChange(String value) {
        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;

        erpStockOutDetail.erpStockOut.neheimai = value;

    }

    @Override
    public void onZhengmaiChange(String value) {
        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;

        erpStockOutDetail.erpStockOut.zhengmai = value;

    }

    @Override
    public void onMemoChange(String value) {
        if (erpStockOutDetail == null || erpStockOutDetail.erpStockOut == null) return;

        erpStockOutDetail.erpStockOut.memo = value;


    }


    @Override
    public void addGuiInfo(String guihao, String fengqian, String guixing) {

        GuiInfo guiInfo = new GuiInfo(guihao, fengqian, guixing);
        guiInfos.add(guiInfo);
        stockOutDetailViewer.showGuihaoData(guiInfos);

    }

    @Override
    public void removeGuiInfo(GuiInfo guiInfo) {
        guiInfos.remove(guiInfo);
        stockOutDetailViewer.showGuihaoData(guiInfos);
    }

    @Override
    public void filterGuihao(GuiInfo guiInfo) {


        java.util.List<ErpStockOutItem> itemList = new ArrayList<>();
        if (guiInfo == null || (StringUtils.isEmpty(guiInfo.guihao))) {
            itemList.addAll(erpStockOutDetail.items);
        } else {
            for (ErpStockOutItem item : erpStockOutDetail.items) {
                if (guiInfo.guihao.equals(item.guihao)) {
                    itemList.add(item);
                }
            }
        }


        stockOutDetailViewer.showItems(itemList);

    }


    @Override
    public void showOrderDetail(String os_no) {
        OrderDetailFrame orderDetailFrame = new OrderDetailFrame(os_no);
        orderDetailFrame.setLocationRelativeTo(this);
        orderDetailFrame.setVisible(true);
    }

    @Override
    public void exportInvoice() {


        if (hasModifyData()) {

            stockOutDetailViewer.showMesssage("请先保存数据");
            return;
        }

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockOut_Invoice().report(erpStockOutDetail, file.getAbsolutePath());
            stockOutDetailViewer.showMesssage("导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        }


    }

    @Override
    public void exportPack() {
        if (hasModifyData()) {

            stockOutDetailViewer.showMesssage("请先保存数据");
            return;
        }
        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockOut_List().report(erpStockOutDetail, file.getAbsolutePath());
            stockOutDetailViewer.showMesssage("导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        }


    }

    @Override
    public void close() {
        setVisible(false);
        dispose();
    }


    /**
     * 柜号相关信息数据
     */

    public static class GuiInfo {

        public String guihao;

        public String fengqianhao;
        public String guixing;

        public GuiInfo() {
        }

        public GuiInfo(String guihao, String fengqianhao, String guixing) {

            this.guihao = guihao;
            this.fengqianhao = fengqianhao;
            this.guixing = guixing;
        }


        @Override
        public String toString() {
            return guihao + "        " + fengqianhao;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GuiInfo)) return false;

            GuiInfo guiInfo = (GuiInfo) o;

            if (guihao != null ? !guihao.equals(guiInfo.guihao) : guiInfo.guihao != null) return false;
            return fengqianhao != null ? fengqianhao.equals(guiInfo.fengqianhao) : guiInfo.fengqianhao == null;

        }

        @Override
        public int hashCode() {
            int result = guihao != null ? guihao.hashCode() : 0;
            result = 31 * result + (fengqianhao != null ? fengqianhao.hashCode() : 0);
            return result;
        }
    }


    /**
     * 出库单拆分
     *
     * @param erpStockOutItem
     */
    @Override
    public void splitItem(ErpStockOutItem erpStockOutItem, int newQty) {


        int index = erpStockOutDetail.items.indexOf(erpStockOutItem);

        ErpStockOutItem newStockOutItem = GsonUtils.fromJson(GsonUtils.toJson(erpStockOutItem), ErpStockOutItem.class);
        newStockOutItem.subRecord = true;
        newStockOutItem.id = 0;
        newStockOutItem.stockOutQty = newQty;
        erpStockOutItem.stockOutQty -= newQty;

        erpStockOutDetail.items.add(index + 1, newStockOutItem);
        stockOutDetailViewer.showItems(erpStockOutDetail.items);


    }


    /**
     * 删除拆分的出库单
     *
     * @param finalItem
     */
    @Override
    public void deleteErpStockOutItem(ErpStockOutItem finalItem) {

        if (!finalItem.subRecord) return;

        boolean returned = false;
        for (ErpStockOutItem item : erpStockOutDetail.items) {
            //找到主数据。
            if (item.itm == finalItem.itm && !item.subRecord) {
                item.stockOutQty += finalItem.stockOutQty;
                returned = true;
                break;
            }
        }

        if (returned) {
            erpStockOutDetail.items.remove(finalItem);
            stockOutDetailViewer.showItems(erpStockOutDetail.items);

        }


    }

    /**
     * 是否可编辑状态
     *
     * @return
     */
    @Override
    public boolean isEditable() {
        return AuthorityUtil.getInstance().editStockOut();
    }

    /**
     * 导出清关发票
     */
    @Override
    public void exportQingguan() {

        if (hasModifyData()) {

            stockOutDetailViewer.showMesssage("请先保存数据");
            return;
        }

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockOut_Qingguan_Invoice().report(erpStockOutDetail, file.getAbsolutePath());
            stockOutDetailViewer.showMesssage("导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        }


    } /**
     * 导出咸康清关发票
     */
    @Override
    public void exportQingguan_xk() {

        if (hasModifyData()) {

            stockOutDetailViewer.showMesssage("请先保存数据");
            return;
        }

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;


        try {
            new Report_Excel_StockOut_XK_Invoice().report(erpStockOutDetail, file.getAbsolutePath());
            stockOutDetailViewer.showMesssage("导出成功");

            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        } catch (HdException e1) {
            e1.printStackTrace();
            stockOutDetailViewer.showMesssage(e1.getMessage());

        }


    }
}
