package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.OutFactoryModel;
import com.giants.hd.desktop.mvp.presenter.OutFactoryIPresenter;
import com.giants.hd.desktop.mvp.viewer.OutFactoryViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.OutFactory;

import javax.swing.*;
import java.util.List;

/**
 * 外厂家列表
 * Created by davidleen29 on 2017/1/15.
 */
public class Panel_OutFactory_LIst extends  BasePanel implements OutFactoryViewer {
    private JPanel root;
    private JHdTable jt;
    private JButton save;

    private OutFactoryIPresenter presenter;
    OutFactoryModel model;

    public Panel_OutFactory_LIst(final OutFactoryIPresenter presenter) {
        this.presenter = presenter;


          model=new OutFactoryModel();
        jt.setModel(model);

        save.setVisible(false);

//        TableMouseAdapter adapter=new TableMouseAdapter(new TablePopMenu.TableMenuLister() {
//            @Override
//            public void onTableMenuClick(int index, BaseTableModel tableModel, int[] rowIndex) {
//
//                switch (index) {
//
//
//                    case TablePopMenu.ITEM_INSERT:
//
//                        tableModel.addNewRow(rowIndex[0]);
//
//                        break;
//                    case TablePopMenu.ITEM_DELETE:
//
//
//
//
//                        tableModel.deleteRows(rowIndex);
//                        break;
//                }
//
//
//
//            }
//        });
//
//        jt.addMouseListener(adapter);


//        save.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                List<OutFactory> newFactories=model.getDatas();
//                List<OutFactory> empty=new ArrayList<OutFactory>();
//                for(OutFactory  temp:newFactories)
//                {
//                    if(StringUtils.isEmpty(temp.name))
//                    {
//                        empty.add(temp);
//                    }
//                }
//
//                newFactories.removeAll(empty);
//
//                presenter.saveData(newFactories);
//
//
//
//
//
//            }
//        });

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
    public void setData(List<OutFactory> datas)
    {

        model.setDatas(datas);
    }
}
