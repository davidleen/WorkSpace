package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.TaskLogDialog;
import com.giants.hd.desktop.viewImpl.Panel_Tasks;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.HdTaskLog;
import com.giants3.hd.noEntity.RemoteData;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;

/**
 *
 * 任务管理面板
 * Created by david on 2015/11/23.
 */
public class TaskListInternalFrame extends BaseInternalFrame {


    public TaskListInternalFrame( ) {
        super("定时任务列表");


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadHdTask();

            }
        });


    }

    Panel_Tasks panel_tasks;

    @Override
    protected Container getCustomContentPane() {

        panel_tasks=new Panel_Tasks(this);
        return panel_tasks.getRoot();

    }

    public  void loadHdTask()
    {


        UseCaseFactory.getInstance().readTaskListUseCase( ).execute(new Subscriber<java.util.List<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(java.util.List<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.setData(tasks);


            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();
    }

    public void addHdTask(HdTask task) {


        UseCaseFactory.getInstance().addHdTaskUseCase(task).execute(new Subscriber<java.util.List<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(java.util.List<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage("任务添加成功");
                panel_tasks.setData(tasks);


            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();

    }


    /**
     * 删除任务
     * @param id
     */
    public void deleteHdTask(long id) {


        UseCaseFactory.getInstance().deleteHdTaskUseCase(id).execute(new Subscriber<java.util.List<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(java.util.List<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage("任务删除成功");
                panel_tasks.setData(tasks);



            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();
    }

    /**
     * 查找该任务运行记录
     * @param data
     */
    public void findTaskLog(HdTask data) {


        if(data.executeCount==0)
        {
            panel_tasks.showMesssage("该任务未执行过");

            return;


        }

        UseCaseFactory.getInstance().findTaskLogUseCase(data.id).execute(new Subscriber<java.util.List<HdTaskLog>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(java.util.List<HdTaskLog> tasks) {

                panel_tasks.hideLoadingDialog();


                if(tasks.size()==0)
                {
                    panel_tasks.showMesssage("该任务暂无运行记录");
                    return;
                }




                TaskLogDialog logDialog=       new TaskLogDialog( SwingUtilities.getWindowAncestor(TaskListInternalFrame.this));
                logDialog.setData(tasks);
                logDialog.setVisible(true);



            }
        });




        panel_tasks.showLoadingDialog();
    }

    public void pauseTask(long id) {

        setTaskState(id,HdTask.STATE_PAUSED);
    }

    public void resumeTask(long id) {



        setTaskState(id,HdTask.STATE_NORMAL);
    }


    private void setTaskState(long taskId,int taskState)
    {


        UseCaseFactory.getInstance().updateHdTaskStateUseCase(taskId,taskState).execute(new Subscriber<RemoteData<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(RemoteData<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage("任务更新成功");
                panel_tasks.setData(tasks.datas);



            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();
    }

    public void executeTask(int taskType) {
        UseCaseFactory.getInstance().executeHdTaskUseCase(taskType).execute(new Subscriber<RemoteData<HdTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(RemoteData<HdTask> tasks) {

                panel_tasks.hideLoadingDialog();
                panel_tasks.showMesssage("任务执行成功");
                loadHdTask();



            }
        });
        //显示dialog
        panel_tasks.showLoadingDialog();
    }
}
