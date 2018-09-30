package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.WorkFlowWorkerTableModel;
import com.giants.hd.desktop.mvp.presenter.WorkFlowWorkerIPresenter;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.mvp.viewer.WorkFlowWorkerViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.WorkFlowWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public class Panel_Work_Flow_Worker extends BasePanel implements WorkFlowWorkerViewer {
    private JPanel root;
    private JButton btn_add;
    private JHdTable jt;


    WorkFlowWorkerTableModel model;
    private WorkFlowWorkerIPresenter presenter;

    public Panel_Work_Flow_Worker(final WorkFlowWorkerIPresenter presenter) {
        this.presenter = presenter;
        this.model = new WorkFlowWorkerTableModel();
        jt.setModel(model);
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.addOne();
            }
        });
        jt.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    int[] row = JTableUtils.getSelectedRowSOnModel(jt);
                    if (row != null && row.length > 0) {
                        WorkFlowWorker workFlowWorker = model.getItem(row[0]);
                        presenter.showOne(workFlowWorker);
                    }
                }
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
    public void bindData(List<WorkFlowWorker> workFlowWorkers) {
        model.setDatas(workFlowWorkers);
    }
}
