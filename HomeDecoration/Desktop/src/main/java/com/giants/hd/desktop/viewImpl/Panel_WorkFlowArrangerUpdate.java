package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.presenter.WorkFlowArrangerUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowArrangerUpdateViewer;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlow;
import com.giants3.hd.entity.WorkFlowArranger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 材料分类修改添加 删除
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_WorkFlowArrangerUpdate extends BasePanel implements WorkFlowArrangerUpdateViewer {

    private final WorkFlowArrangerUpdateIPresenter presenter;
    private JPanel root;
    private JButton btn_save;
    private JButton btn_delete;
    private JComboBox cb_workFlow;
    private JCheckBox selfMade;
    private JCheckBox purchase;
    private JComboBox cb_user;
    private List<User> users;
    private List<WorkFlow> workFlows;


    public Panel_WorkFlowArrangerUpdate(final WorkFlowArrangerUpdateIPresenter presenter) {
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
    public void bindData(WorkFlowArranger workFlowArranger) {


        btn_delete.setVisible(workFlowArranger != null && workFlowArranger.id > 0);
        if (workFlowArranger == null) return;

        User selected=null;
        for(User user:users)
        {
            if(user.id==workFlowArranger.userId)
            {
                selected=user;
                break;
            }
        }
        if(selected!=null)
            cb_user.setSelectedItem(selected);


        selfMade.setSelected(workFlowArranger.selfMade);
        purchase.setSelected(workFlowArranger.purchase);






    }



    @Override
    public void bindUsers(List<User> users)
    {
        this.users = users;

        cb_user.setModel(new DefaultComboBoxModel(ArrayUtils.changeListToVector(users)));


    }

    @Override
    public void getData(WorkFlowArranger workFlowWorker) {


        User user= (User) cb_user.getSelectedItem();

        workFlowWorker.userId=user.id;
        workFlowWorker.userName=user.code+user.name+user.chineseName;
        workFlowWorker.selfMade=selfMade.isSelected();
        workFlowWorker.purchase=purchase.isSelected();

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
