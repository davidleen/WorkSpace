package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.frames.TaskListInternalFrame;
import com.giants.hd.desktop.model.HdTaskModel;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.entity.HdTask;
import com.giants3.hd.utils.DateFormats;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;

/** 任务列表面板
 * Created by david on 2015/12/11.
 */
public class Panel_Tasks  extends BasePanel{
    private JHdTable jtable;
    private JButton add;
    private JButton delete;
    private JPanel root;
    private JComboBox cbTaskType;
    private JSpinner timePicker;
    private JTextField memo;
    private JComboBox cb_repeat;
    private JSpinner timeSpinner;
    private JPanel panel_task;
    private JButton btn_pause;
    private JButton btn_resume;
    private JButton btn_execute;
    @Inject
    HdTaskModel taskModel;



    @Inject
    ApiManager apiManager;
    private TaskListInternalFrame frame;


      int[] taskTypes=new int[]{HdTask.TYPE_SYNC_ERP};
      String[] taskTypeNames=new String[]{HdTask.NAME_SYNC_ERP};
    @Override
    public JComponent getRoot() {
        return root;
    }
    public Panel_Tasks(final TaskListInternalFrame frame) {
        super();
        panel_task.setVisible(false);


        this.frame = frame;
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtable.setModel(taskModel);
        jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        cb_repeat.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                timeSpinner.setEnabled(cb_repeat.getSelectedIndex()==3);

            }});


        cb_repeat.setSelectedIndex(0);


        jtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.getClickCount()==2)
                {
                    //显示执行记录

                    int[] modelRows=JTableUtils.getSelectedRowSOnModel(jtable);
                    if(modelRows!=null&&modelRows.length==1)
                    {
                        HdTask data=taskModel.getItem(modelRows[0]);

                        frame.findTaskLog(data);
                    }



                }
            }
        });


//        jtable.setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
//
//
//                    @Override
//                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                        Component component =super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//
//
//                        if(component instanceof  JLabel)
//                        {
//
//                            JLabel jLabel=(JLabel)component;
//
//                            int rowIndexToModel=  table.convertRowIndexToModel(row);
//
//                            HdTask data=      taskModel.getItem(rowIndexToModel);
//
//                            switch (data.state)
//                            {
//
//                                case HdTask.STATE_SUCCESS:
//                                    jLabel.setForeground(Color.GREEN);
//                                    break;
//                                case HdTask.STATE_FAIL:
//                                    jLabel.setForeground(Color.RED);
//                                    break;
//                                default:
//                                    long time=
//                                    Calendar.getInstance().getTimeInMillis();
//
//                                    jLabel.setForeground(data.startDate>time?Color.DARK_GRAY:Color.LIGHT_GRAY);
//
//                            }
//
//
//
//                        }
//
//                        return component;
//                    }
//                }
//        );

        timePicker.setModel(new SpinnerDateModel());

        final JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timePicker, "yyyy-MM-dd HH:mm");
        timePicker.setEditor(timeEditor);


        for(String typeName:taskTypeNames)
        {
            cbTaskType.addItem(typeName);
        }

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(cbTaskType.getSelectedItem()==null)
                {
                    showMesssage("请选择一个任务类型");
                    return;
                }

                Date date;
                try {
                      date = (Date) timePicker.getValue();
                }catch (Throwable t)
                {
                    showMesssage("时间格式输入不正确");
                    return;

                }


                if(date==null||date.before(new Date()))
                {

                    showMesssage( "时间不能为过去时刻");
                    return;
                }



                HdTask task=new HdTask();
                int selectIndex=cbTaskType.getSelectedIndex();
                task.taskType=taskTypes[selectIndex];
                task.taskName=taskTypeNames[selectIndex];
                task.startDate =date.getTime();
                task.memo=memo.getText().trim();
                task.dateString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(date);


                int repeatIndex=cb_repeat.getSelectedIndex();
                task.repeatCount=repeatIndex==0?Integer.MAX_VALUE:Integer.valueOf(timeSpinner.getValue().toString());
                frame.addHdTask(task);




            }
        });



        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               int[]  modelRow= JTableUtils.getSelectedRowSOnModel(jtable);
                if(modelRow.length==0)
                {
                    showMesssage("请选择一条任务进行删除。");
                    return;
                }


                if(showConfirmMessage("确定删除选中任务？","确认信息")) {


                    HdTask item = taskModel.getItem(modelRow[0]);
                    frame.deleteHdTask(item.id
                    );

                }


            }
        });

        btn_pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[]  modelRow= JTableUtils.getSelectedRowSOnModel(jtable);
                if(modelRow.length==0)
                {
                    showMesssage("请选择一条任务进行暂停。");
                    return;
                }

                HdTask item = taskModel.getItem(modelRow[0]);
                if(item.id<=0) return ;
                frame.pauseTask(item.id
                );
            }
        });

        btn_resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[]  modelRow= JTableUtils.getSelectedRowSOnModel(jtable);
                if(modelRow.length==0)
                {
                    showMesssage("请选择一条任务进行恢复。");
                    return;
                }

                HdTask item = taskModel.getItem(modelRow[0]);

                if(item.id<=0) return ;

                frame.resumeTask(item.id
                );
            }
        });
        btn_execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[]  modelRow= JTableUtils.getSelectedRowSOnModel(jtable);
                if(modelRow.length==0)
                {
                    showMesssage("请选择一条任务进行执行。");
                    return;
                }

                HdTask item = taskModel.getItem(modelRow[0]);

                if(item.id<=0) return ;


                int option = JOptionPane.showConfirmDialog(getWindow(),"立刻执行选中任务《《《"+item.taskName+"》》》吗?", " 提示", JOptionPane.OK_CANCEL_OPTION);

                if (JOptionPane.OK_OPTION == option) {
                    //点击了确定按钮
                    frame.executeTask(item.taskType
                    );
                }

            }
        });


    }





    /**
     * 数据绑定
     * @param tasks
     */
    public void setData(List<HdTask> tasks)
    {

        taskModel.setDatas(tasks);
    }
}
