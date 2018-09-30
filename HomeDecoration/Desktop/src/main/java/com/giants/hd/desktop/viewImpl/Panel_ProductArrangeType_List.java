package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.ProductArrangeTypeModel;
import com.giants.hd.desktop.mvp.presenter.ProductArrangeTypeIPresenter;
import com.giants.hd.desktop.mvp.viewer.ProductArrangeTypeViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.TableMouseAdapter;
import com.giants.hd.desktop.widget.TablePopMenu;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.WorkFlowSubType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品 排厂类型
 * 二级流程 铁件 木件  PU，其他
 * <p/>
 * Created by davidleen29 on 2017/1/15.
 */
public class Panel_ProductArrangeType_List extends BasePanel implements ProductArrangeTypeViewer {
    private JPanel root;
    private JHdTable jt;
    private JButton save;

    private ProductArrangeTypeIPresenter presenter;
    ProductArrangeTypeModel model;

    public Panel_ProductArrangeType_List(final ProductArrangeTypeIPresenter presenter) {
        this.presenter = presenter;


        model = new ProductArrangeTypeModel();
        jt.setModel(model);


        TableMouseAdapter adapter = new TableMouseAdapter(new TablePopMenu.TableMenuLister() {
            @Override
            public void onTableMenuClick(int index, BaseTableModel tableModel, int[] rowIndex) {

                switch (index) {


                    case TablePopMenu.ITEM_INSERT:

                        tableModel.addNewRow(rowIndex[0]);

                        break;
                    case TablePopMenu.ITEM_DELETE:


                        tableModel.deleteRows(rowIndex);
                        break;
                }


            }
        });

        jt.addMouseListener(adapter);


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                List<WorkFlowSubType> newData = model.getDatas();
                List<WorkFlowSubType> empty = new ArrayList<WorkFlowSubType>();
                for (WorkFlowSubType temp : newData) {
                    if (StringUtils.isEmpty(temp.typeName)) {
                        empty.add(temp);
                    }
                }

                newData.removeAll(empty);

                presenter.saveData(newData);


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
        return root;
    }

    @Override
    public void setData(List<WorkFlowSubType> datas) {

        model.setDatas(datas);
    }
}
