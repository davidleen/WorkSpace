package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.StockInAndSubmitTableModel;
import com.giants.hd.desktop.mvp.presenter.StockXiaokuDetailIPresenter;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * 销库单详情
 * Created by davidleen29 on 2016/12/12.
 */
public class Panel_StockXiaokuDetail  extends BasePanel{
    private final StockXiaokuDetailIPresenter presenter;
    private JPanel rootpanel;
    private JHdTable tb;
    private JButton export;
    private JLabel guihao;
    private JLabel fapiao;
    private JLabel guixing;
    private JLabel fengqian;
    private JLabel tcgs;
    private JLabel ztj;

    StockInAndSubmitTableModel tableModel;

    public Panel_StockXiaokuDetail(final StockXiaokuDetailIPresenter presenter) {
        this.presenter = presenter;
        tableModel=new StockInAndSubmitTableModel();
        tb.setModel(tableModel);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                presenter.exportExcel();
            }
        });


    }
    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return rootpanel;
    }


    public void setItemData(RemoteData<StockSubmit> remoteData)
    {

        tableModel.setDatas(remoteData.datas);


        //计算出总体积
        float ztjValue=0;
        for(StockSubmit submit:remoteData.datas
                )
        {
            ztjValue+=submit.zxgtj;
        }

        ztj.setText(String.valueOf(ztjValue));
    }

    public void bindData(StockXiaoku xiaoku)
    {

        tcgs.setText(xiaoku.tcgs);
        fengqian.setText(xiaoku.xhfq);
        guihao.setText(xiaoku.xhgh);
        guixing.setText(xiaoku.xhgx);
        fapiao.setText(xiaoku.xhph);
    }
}
