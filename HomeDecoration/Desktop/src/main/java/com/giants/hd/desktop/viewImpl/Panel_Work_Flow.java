package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.WorkFlowModel;
import com.giants.hd.desktop.mvp.presenter.WorkFlowIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.User;

import javax.swing.*;
import java.util.List;

/**
 * 生产流程 界面层实现
 * Created by davidleen29 on 2016/9/17.
 */
public class Panel_Work_Flow extends BasePanel implements WorkFlowViewer {
    private JComponent rootPanel;
    private JButton save;
    private JHdTable tb;



    WorkFlowModel workFlowModel;

    WorkFlowIPresenter presenter;

    public Panel_Work_Flow(final WorkFlowIPresenter presenter) {
        this.presenter = presenter;
        workFlowModel = new WorkFlowModel();
        workFlowModel.setRowAdjustable(false);
        tb.setModel(workFlowModel);

        save.setVisible(false);






//        save.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//
//                presenter.save( );
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
        return rootPanel;
    }

    @Override
    public void setData(List<ErpWorkFlow> datas) {

        workFlowModel.setDatas(datas);
    }

    @Override
    public void setUserList(List<User> users) {

//        JComboBox<User> comboBox = new JComboBox<>();
//        User empty =new User();
//        comboBox.addItem(empty);
//        for (User user : users)
//            comboBox.addItem(user);
//        DefaultCellEditor comboboxEditor = new DefaultCellEditor(comboBox);
//        tb.setDefaultEditor(User.class , comboboxEditor);

    }
}
