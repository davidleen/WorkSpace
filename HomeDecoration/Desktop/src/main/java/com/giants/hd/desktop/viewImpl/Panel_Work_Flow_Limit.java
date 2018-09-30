package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.WorkFlowTimeLimitModel;
import com.giants.hd.desktop.mvp.presenter.WorkFlowLimitConfigIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowLimitConfigViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.header.ColumnGroup;
import com.giants.hd.desktop.widget.header.GroupableTableHeader;
import com.giants3.hd.entity.WorkFlowTimeLimit;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public class Panel_Work_Flow_Limit extends BasePanel implements WorkFlowLimitConfigViewer {
    private JPanel root;
    private JButton btn_save;
    private JHdTable jt;
    private JCheckBox cb_updateComplete;


    WorkFlowTimeLimitModel model;

    private WorkFlowLimitConfigIPresenter presenter;

    public Panel_Work_Flow_Limit(final WorkFlowLimitConfigIPresenter presenter) {

        this.presenter = presenter;
        this.model = new WorkFlowTimeLimitModel();


        jt.setModel(model);



        TableColumnModel cm = jt.getColumnModel();

        int startIndex=1;
        String[] group=new String[]{"胚体加工(木)","胚体加工(铁)","白胚（木）","白胚（铁）","颜色（木）","颜色（铁）","包装（木）","包装（铁）"};

        ColumnGroup[] g_names = new ColumnGroup[group.length] ;
        for (int i = 0; i < group.length; i++) {
            g_names[i]= new ColumnGroup(group[i]);
        }
        for(int i=0;i+startIndex<cm.getColumnCount();i++)
        {
            int groupIndex=i/2;
            ColumnGroup g_name = g_names[groupIndex];
            g_name.add(cm.getColumn(startIndex+i));
        }


        GroupableTableHeader header = (GroupableTableHeader)jt.getTableHeader();
        for (ColumnGroup columnGroup:g_names)
        {
            header.addColumnGroup(columnGroup);
        }




        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean updateComplete=cb_updateComplete.isSelected();
                presenter.save(updateComplete);
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
    public void bindData(java.util.List<WorkFlowTimeLimit> flowLimit) {
        model.setDatas(flowLimit);

    }

    private void createUIComponents() {
        jt = new JHdTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        jt.setShowVerticalLines(true);
        jt.setShowHorizontalLines(true);

    }
}
