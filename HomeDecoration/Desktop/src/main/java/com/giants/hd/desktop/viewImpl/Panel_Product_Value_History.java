package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.BaseListTableModel;
import com.giants.hd.desktop.mvp.presenter.ProductValueHistoryPresenter;
import com.giants.hd.desktop.mvp.viewer.ProductValueHistoryViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ProductValueHistory;

import javax.swing.*;
import java.awt.event.*;

/**
 * 广交会报价同步面板  presenter
 * Created by david on 2018/3/29.
 */
public class Panel_Product_Value_History extends BasePanel<ProductValueHistoryPresenter> implements ProductValueHistoryViewer {

    private JPanel panel1;

 ;
    private JHdTable table;
    private JTextField tf_key;
    private JButton search;
    private JPanel searchPanel;
    private Panel_Page pagePanel;
    private JCheckBox cb_accurate;
    BaseListTableModel<ProductValueHistory> tableModel;

    public Panel_Product_Value_History(final ProductValueHistoryPresenter presenter) {
        super(presenter);


        tableModel=new BaseListTableModel<>(ProductValueHistory.class,TableStructureUtils.getProductValueHistoryTable());
//        tableModel.setRowHeight(ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT);
        table.setModel(tableModel);

        table.addMouseListener(new MouseAdapter() {

        @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int[] selectedRowSOnModel = JTableUtils.getSelectedRowSOnModel(table);
                    if(selectedRowSOnModel!=null&&selectedRowSOnModel.length>0)
                    getPresenter().findHistoryData(tableModel.getItem(selectedRowSOnModel[0]));


                }

            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPresenter().search(tf_key.getText(), cb_accurate.isSelected() ,0, pagePanel.getPageSize());
            }
        });

        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                getPresenter().search(tf_key.getText(), cb_accurate.isSelected() ,pageIndex, pageSize);


            }
        });

        cb_accurate.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                tf_key.setToolTipText(cb_accurate.isSelected()?"输入产品完整名称，版本号以'-'连接,（08B3331-1111）":"根据名称，版本号模糊查询");
            }
        });

    }


    @Override
    public void setSearchable(boolean showSearch)
    {

        searchPanel.setVisible(showSearch);
    }


    @Override
    public JComponent getRoot() {
        return panel1;
    }



    @Override
    public void bindData(RemoteData<ProductValueHistory> remoteData) {
        tableModel.setDatas(remoteData.datas);
        pagePanel.bindRemoteData(remoteData);
    }
}
