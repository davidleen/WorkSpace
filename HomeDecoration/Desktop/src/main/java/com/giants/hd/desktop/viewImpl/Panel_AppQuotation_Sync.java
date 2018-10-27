package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.local.HdDateComponentFormatter;
import com.giants.hd.desktop.mvp.presenter.AppQuotationSyncPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationSyncViewer;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 广交会报价同步面板  presenter
 * Created by david on 2018/3/29.
 */
public class Panel_AppQuotation_Sync extends BasePanel implements AppQuotationSyncViewer {
    private final AppQuotationSyncPresenter presenter;
    private JPanel panel1;
    private JTextField tf_url;
    private JButton btn_sync;
    private JDatePickerImpl startDate;
    private JDatePickerImpl endDate;
    private JButton syncPicture;
    private JTextField remoteResourceText;
    private JTextField tf_filter;
    private JButton btn_init;
    private JButton syncProduct;
    private JCheckBox cb_override;


    public Panel_AppQuotation_Sync(final AppQuotationSyncPresenter presenter) {

        this.presenter = presenter;

        btn_sync.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String ip= tf_url.getText().trim();

                presenter.beginAsync(ip, startDate.getJFormattedTextField().getText(), endDate.getJFormattedTextField().getText());
            }
        }); btn_init.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                presenter.initData( );
            }
        });

        syncPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {






                String remoteResource=remoteResourceText.getText().trim();
                String filterKey=tf_filter.getText().trim();
                boolean shouldOverride=cb_override.isSelected();
                presenter.beginAsyncPicture(remoteResource,filterKey,shouldOverride);



            }
        });

        syncProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {






                String remoteResource=remoteResourceText.getText().trim();
                String filterKey=tf_filter.getText().trim();
                boolean shouldOverride=cb_override.isSelected();


                presenter.beginAsyncProduct(remoteResource,filterKey,shouldOverride);



            }
        });



    }




    @Override
    public JComponent getRoot() {
        return panel1;
    }

    private void createUIComponents() {


        JDatePanelImpl picker = new JDatePanelImpl(null);
        startDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());
        picker = new JDatePanelImpl(null);
        endDate = new JDatePickerImpl(picker, new HdDateComponentFormatter());}

}
