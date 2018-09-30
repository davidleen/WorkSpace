package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.presenter.AppQuotationReportPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationReportViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 广交会报价同步面板  presenter
 * Created by david on 2018/3/29.
 */
public class Panel_AppQuotation_Report extends BasePanel implements AppQuotationReportViewer {
    private final AppQuotationReportPresenter presenter;
    private JPanel panel1;
    private JButton btn_count_report;


    public Panel_AppQuotation_Report(final AppQuotationReportPresenter presenter) {


        this.presenter = presenter;
        btn_count_report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.showQuoteCountReport();
            }
        });
    }


    @Override
    public JComponent getRoot() {
        return panel1;
    }


}
