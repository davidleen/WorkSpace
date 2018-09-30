package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.mvp.presenter.QuotationDetailIPresenter;
import com.giants.hd.desktop.viewImpl.BasePanel;
import com.giants.hd.desktop.viewImpl.Panel_QuotationDetail;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.QuotationDelete;
import com.giants3.hd.noEntity.QuotationDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;

/**
 * 报价单详情模块
 */
public class QuotationDetailFrame extends BaseFrame implements QuotationDetailIPresenter {


    @Inject
    ApiManager apiManager;


    Panel_QuotationDetail panel_QuotationDetail;


    QuotationDetailAdapter adapter = new QuotationDetailAdapter();

    private QuotationDetail oldData;
    private QuotationDetail quotationDetail;


    public QuotationDetailFrame(final QuotationDetail quotationDetail) {


        this(quotationDetail, null);
    }

    public QuotationDetailFrame(final QuotationDetail quotationDetail, QuotationDelete quotationDelete) {

        super();

        panel_QuotationDetail = new Panel_QuotationDetail(this);


        Quotation quotation = quotationDetail.quotation;

        String title = "";
        if (quotation.id <= 0) {
            switch ((int) quotation.quotationTypeId) {

                case Quotation.QUOTATION_TYPE_NORMAL:

                    title = "新增普通报价单";
                    break;
                case Quotation.QUOTATION_TYPE_XK:
                    title = "新增咸康报价单";
                    break;
            }


        } else {
            switch ((int) quotation.quotationTypeId) {

                case Quotation.QUOTATION_TYPE_NORMAL:

                    title = "普通报价单详情[" + quotationDetail.quotation.qNumber + "]";
                    break;
                case Quotation.QUOTATION_TYPE_XK:
                    title = "咸康报价单详情[" + quotationDetail.quotation.qNumber + "]";
                    break;
            }

            if (quotationDelete != null) {
                title += "      [已删除]";
            }

        }
        setTitle(title);

        init();
        setQuotationDetail(quotationDetail);
        panel_QuotationDetail.setQuotationDelete(quotationDelete);


    }


    public void init() {


        setContentPane(panel_QuotationDetail.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();


        panel_QuotationDetail.setListener(adapter);


    }


    @Override
    public boolean hasModifyData() {
        if (panel_QuotationDetail.data == null) {
            return false;
        }

        panel_QuotationDetail.getData(quotationDetail);

        return !quotationDetail.equals(oldData);
    }

    private void setQuotationDetail(QuotationDetail newDetail) {


        oldData = (QuotationDetail) ObjectUtils.deepCopy(newDetail);
        this.quotationDetail = newDetail;
        panel_QuotationDetail.setData(newDetail);

    }


    /**
     * 保存详情信息
     */
    private void saveQuotationDetail(final QuotationDetail quotationDetail) {


        new HdSwingWorker<QuotationDetail, Long>(this) {
            @Override
            protected RemoteData<QuotationDetail> doInBackground() throws Exception {
                return apiManager.saveQuotationDetail(quotationDetail);
            }

            @Override
            public void onResult(RemoteData<QuotationDetail> data) {

                if (data.isSuccess()) {

                    setQuotationDetail(data.datas.get(0));
                    JOptionPane.showMessageDialog(QuotationDetailFrame.this, "保存成功");

                } else {

                    JOptionPane.showMessageDialog(QuotationDetailFrame.this, data.message);


                }
            }
        }.go();


    }

    /**
     * 保存详情信息
     */
    private void saveAndVerifyQuotationDetail(final QuotationDetail quotationDetail) {


        new HdSwingWorker<QuotationDetail, Long>(this) {
            @Override
            protected RemoteData<QuotationDetail> doInBackground() throws Exception {
                return apiManager.saveAndVerifyQuotationDetail(quotationDetail);
            }

            @Override
            public void onResult(RemoteData<QuotationDetail> data) {

                if (data.isSuccess()) {

                    setQuotationDetail(data.datas.get(0));
                    JOptionPane.showMessageDialog(QuotationDetailFrame.this, "保存/审核成功");

                } else {

                    JOptionPane.showMessageDialog(QuotationDetailFrame.this, data.message);


                }
            }
        }.go();


    }

    @Override
    public void save() {


        try {
            panel_QuotationDetail.checkData(quotationDetail);
        } catch (HdUIException e) {
            JOptionPane.showMessageDialog(e.component, e.message);
            e.component.requestFocus();
            return;
        }


        panel_QuotationDetail.getData(quotationDetail);


        if (quotationDetail.equals(oldData)) {
            JOptionPane.showMessageDialog(QuotationDetailFrame.this, "数据无改动");
            return;
        }


        saveQuotationDetail(quotationDetail);


    }

    /**
     * 面板监听适配类
     */
    private class QuotationDetailAdapter extends BasePanel.PanelAdapter {


        @Override
        public void save() {


            try {
                panel_QuotationDetail.checkData(quotationDetail);
            } catch (HdUIException e) {
                JOptionPane.showMessageDialog(e.component, e.message);
                e.component.requestFocus();
                return;
            }


            panel_QuotationDetail.getData(quotationDetail);


            if (quotationDetail.equals(oldData)) {
                JOptionPane.showMessageDialog(QuotationDetailFrame.this, "数据无改动");
                return;
            }


            saveQuotationDetail(quotationDetail);


        }

        @Override
        public void delete() {


            final QuotationDetail detail = quotationDetail;
            if (detail == null) return;

            if (detail.quotation.id <= 0) {

                JOptionPane.showMessageDialog(QuotationDetailFrame.this, "产品数据未建立，请先保存");
                return;

            }


            int res = JOptionPane.showConfirmDialog(QuotationDetailFrame.this, "是否删除报价单？（导致数据无法恢复）", "删除", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                new HdSwingWorker<Void, Void>(QuotationDetailFrame.this) {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {

                        return apiManager.deleteQuotationLogic(detail.quotation.id);


                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        if (data.isSuccess()) {

                            JOptionPane.showMessageDialog(QuotationDetailFrame.this, "删除成功！");

                            QuotationDetailFrame.this.dispose();


                        } else {
                            JOptionPane.showMessageDialog(QuotationDetailFrame.this, data.message);
                        }

                    }
                }.go();


            }


        }

        @Override
        public void close() {
            setVisible(false);
            dispose();
        }


        @Override
        public void verify() {


            try {
                panel_QuotationDetail.checkData(quotationDetail);
            } catch (HdUIException e) {
                JOptionPane.showMessageDialog(e.component, e.message);
                e.component.requestFocus();
                return;
            }


            panel_QuotationDetail.getData(quotationDetail);


            saveAndVerifyQuotationDetail(quotationDetail);


        }

        @Override
        public void unVerify() {


            int res = JOptionPane.showConfirmDialog(QuotationDetailFrame.this, "是否撤销报价单的审核？（未审核的报价单可以修改，但不能导出excel）", "撤销审核", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                new HdSwingWorker<Void, Void>(QuotationDetailFrame.this) {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {

                        return apiManager.unVerifyQuotation(oldData.quotation.id);


                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        if (data.isSuccess()) {

                            JOptionPane.showMessageDialog(QuotationDetailFrame.this, "撤销成功！");

                            QuotationDetailFrame.this.dispose();


                        } else {
                            JOptionPane.showMessageDialog(QuotationDetailFrame.this, data.message);
                        }

                    }
                }.go();


            }


        }
    }


    @Override
    public void delete() {


        final QuotationDetail detail = quotationDetail;
        if (detail == null) return;

        if (detail.quotation.id <= 0) {

            JOptionPane.showMessageDialog(QuotationDetailFrame.this, "产品数据未建立，请先保存");
            return;

        }


        int res = JOptionPane.showConfirmDialog(QuotationDetailFrame.this, "是否删除报价单？（导致数据无法恢复）", "删除", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            new HdSwingWorker<Void, Void>(QuotationDetailFrame.this) {

                @Override
                protected RemoteData<Void> doInBackground() throws Exception {

                    return apiManager.deleteQuotationLogic(detail.quotation.id);


                }

                @Override
                public void onResult(RemoteData<Void> data) {

                    if (data.isSuccess()) {

                        JOptionPane.showMessageDialog(QuotationDetailFrame.this, "删除成功！");

                        QuotationDetailFrame.this.dispose();


                    } else {
                        JOptionPane.showMessageDialog(QuotationDetailFrame.this, data.message);
                    }

                }
            }.go();


        }


    }

    @Override
    public void close() {
        setVisible(false);
        dispose();
    }


    @Override
    public void verify() {


        try {
            panel_QuotationDetail.checkData(quotationDetail);
        } catch (HdUIException e) {
            JOptionPane.showMessageDialog(e.component, e.message);
            e.component.requestFocus();
            return;
        }


        panel_QuotationDetail.getData(quotationDetail);


        saveAndVerifyQuotationDetail(quotationDetail);


    }

    @Override
    public void unVerify() {


        int res = JOptionPane.showConfirmDialog(QuotationDetailFrame.this, "是否撤销报价单的审核？ ", "撤销审核", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            new HdSwingWorker<Void, Void>(QuotationDetailFrame.this) {

                @Override
                protected RemoteData<Void> doInBackground() throws Exception {

                    return apiManager.unVerifyQuotation(oldData.quotation.id);


                }

                @Override
                public void onResult(RemoteData<Void> data) {

                    if (data.isSuccess()) {

                        JOptionPane.showMessageDialog(QuotationDetailFrame.this, "撤销成功！");

                        QuotationDetailFrame.this.dispose();


                    } else {
                        JOptionPane.showMessageDialog(QuotationDetailFrame.this, data.message);
                    }

                }
            }.go();


        }


    }
}
