package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.ProductProcessModel;
import com.giants.hd.desktop.mvp.productprocess.ProductProcessPresenter;
import com.giants.hd.desktop.mvp.productprocess.ProductProcessViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants3.hd.entity.ProductProcess;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 工序列表
 */
public class Panel_ProductProcess extends BasePanel<ProductProcessPresenter> implements ProductProcessViewer {
    private JPanel root;
    private JTable jt_process;
    private JButton btn_add;

    @Inject
    public ProductProcessModel productProcessModel;

    public Panel_ProductProcess(ProductProcessPresenter productProcessPresenter) {
        super(productProcessPresenter);
        jt_process.setModel(productProcessModel);
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jt_process.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() >= 2) {

                    int selectedRow = JTableUtils.getFirstSelectedRowSOnModel(jt_process);
                    if(selectedRow<0) return;
                    getPresenter().updateItem(productProcessModel.getItem(selectedRow));


                }
            }
        });
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                getPresenter().addOne();


            }
        });

    }

    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void setData(List<ProductProcess> datas) {

        productProcessModel.setDatas(datas);


    }
}
