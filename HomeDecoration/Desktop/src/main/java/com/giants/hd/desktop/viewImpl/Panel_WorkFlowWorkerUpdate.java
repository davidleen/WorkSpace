package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.presenter.WorkFlowWorkerUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowWorkerUpdateViewer;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * 流程节点人员改添加 删除
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_WorkFlowWorkerUpdate extends BasePanel implements WorkFlowWorkerUpdateViewer {

    private final WorkFlowWorkerUpdateIPresenter presenter;
    private JPanel root;
    private JButton btn_save;
    private JButton btn_delete;
    private JComboBox cb_workFlow;
    private JCheckBox receive;
    private JCheckBox send;
    private JCheckBox tie;
    private JCheckBox mu;
    private JCheckBox pu;
    private JComboBox cb_user;
    private JRadioButton rb_self;
    private JRadioButton rb_purchase;
    private JButton jb_jgh;
    private JLabel jgh;
    private List<User> users;
//    private List<ErpWorkFlow> workFlows;
    private WorkFlowWorker workFlowWorker;


    public Panel_WorkFlowWorkerUpdate(final WorkFlowWorkerUpdateIPresenter presenter) {
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



        jb_jgh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                presenter.pickjgh();
            }
        });


//        ButtonGroup buttonGroup=new ButtonGroup();
//        buttonGroup.add(rb_self);
//        buttonGroup.add(rb_purchase);


    }



    ItemListener itemListener=new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {



            workFlowWorker.produceType=ProduceType.SELF_MADE;


            bindData(workFlowWorker);

        }
    };
    ItemListener rb_purchaseitemListener=new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {




            workFlowWorker.produceType=ProduceType.PURCHASE;

            bindData(workFlowWorker);

        }
    };



    @Override
    public void bindWorkFlows(List<ErpWorkFlow> workFlows)
    {
        //this.workFlows = workFlows;




    }


    private  void  setPeitiEnable(boolean enable)
    {

        tie.setEnabled(enable);
        mu.setEnabled(enable);
        pu.setEnabled(enable);

    }

    @Override
    public void bindUsers(List<User> users)
    {
        this.users = users;

        cb_user.setModel(new DefaultComboBoxModel(ArrayUtils.changeListToVector(users)));


    }
    @Override
    public void bindData(WorkFlowWorker workFlowWorker) {
        this.workFlowWorker = workFlowWorker;


        btn_delete.setVisible(workFlowWorker != null && workFlowWorker.id > 0);
         if (workFlowWorker == null) return;

        User selected=null;
        for(User user:users)
        {
            if(user.id==workFlowWorker.userId)
            {
                selected=user;
                break;
            }
        }
        if(selected!=null)
        cb_user.setSelectedItem(selected);




        List<ErpWorkFlow> workFlows=workFlowWorker.produceType==ProduceType.PURCHASE?ErpWorkFlow.purchaseWorkFLows:ErpWorkFlow.WorkFlows;



        cb_workFlow.setModel(new DefaultComboBoxModel(ArrayUtils.changeListToVector(workFlows)));

        ErpWorkFlow  selectedWorkFlow=null;
        for(ErpWorkFlow workFlow:workFlows)
        {
            if(workFlow.step==workFlowWorker.workFlowStep)
            {
                selectedWorkFlow=workFlow;
                break;
            }
        }
        if(selectedWorkFlow!=null)
            cb_workFlow.setSelectedItem(selectedWorkFlow);



        receive.setSelected(workFlowWorker.receive);
        send.setSelected(workFlowWorker.send);
        tie.setSelected(workFlowWorker.tie);
        mu.setSelected(workFlowWorker.mu);
        pu.setSelected(workFlowWorker.pu);


//
         rb_purchase.removeItemListener(rb_purchaseitemListener);
        rb_self.removeItemListener(itemListener);
        rb_purchase.setSelected(workFlowWorker.produceType== ProduceType.PURCHASE);
        rb_self.setSelected(workFlowWorker.produceType== ProduceType.SELF_MADE);
        setPeitiEnable(rb_self.isSelected());
         rb_purchase.addItemListener(rb_purchaseitemListener);
        rb_self.addItemListener(itemListener);
        jgh.setText(workFlowWorker.jghnames);


    }

    @Override
    public void getData(WorkFlowWorker workFlowWorker) {


     User user= (User) cb_user.getSelectedItem();
        if(user!=null)
        {
            workFlowWorker.userId=user.id;
            workFlowWorker.userName=user.code+user.name+user.chineseName;
        } ErpWorkFlow workFlow= (ErpWorkFlow) cb_workFlow.getSelectedItem();
        if(workFlow!=null)
        {
            workFlowWorker.workFlowCode=workFlow.code;
            workFlowWorker.workFlowStep=workFlow.step;
            workFlowWorker.workFlowName=workFlow.name;
        }
        workFlowWorker.receive=receive.isSelected();
        workFlowWorker.send=send.isSelected();
        workFlowWorker.tie=tie.isSelected();
        workFlowWorker.mu=mu.isSelected();
        workFlowWorker.pu=pu.isSelected();
        workFlowWorker.produceType=rb_purchase.isSelected()?ProduceType.PURCHASE:ProduceType.SELF_MADE;
        workFlowWorker.produceTypeName=rb_purchase.isSelected()?ProduceType.PURCHASE_NAME:ProduceType.SELF_MADE_NAME;
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
