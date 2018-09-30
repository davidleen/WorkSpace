package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.presenter.WorkFlowAreaUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowAreaUpdateViewer;
import com.giants3.hd.entity.WorkFlowArea;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 流程节点人员改添加 删除
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_WorkFlowAreaUpdate extends BasePanel implements WorkFlowAreaUpdateViewer {

    private final WorkFlowAreaUpdateIPresenter presenter;
    private JPanel root;
    private JButton btn_save;
    private JButton btn_delete;
    private JTextField tv_code;
    private JTextField tv_name;
    private JTextArea tv_discribe;



    public Panel_WorkFlowAreaUpdate(final WorkFlowAreaUpdateIPresenter presenter) {
        super();

        this.presenter = presenter;


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save();

            }
        });

        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.delete();

            }
        });

    }


    @Override
    public void bindData(WorkFlowArea workFlowArea) {


        btn_delete.setVisible(workFlowArea != null && workFlowArea.id > 0);
         if (workFlowArea == null) return;

        tv_code.setText(workFlowArea.code);
        tv_name.setText(workFlowArea.name);
        tv_discribe.setText(workFlowArea.description);



    }

    @Override
    public void getData(WorkFlowArea workFlowArea) {
        workFlowArea.code=tv_code.getText();
        workFlowArea.name=tv_name.getText();
        workFlowArea.description=tv_discribe.getText();
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
}
