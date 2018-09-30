package com.giants.hd.desktop.local;

import com.giants.hd.desktop.viewImpl.LoadingDialog;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

/**
 * 自定义worker 类 封装 进度条等方法
 *
 * TODO   根据进度值 更新进度条
 *
 */
public abstract class HdSwingStepWorker<T,V>  extends SwingWorker<RemoteData<T>,V>{
      LoadingDialog dialog ;


    private String message;


    public HdSwingStepWorker(Window component)
    {

        dialog  = new LoadingDialog(component, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { cancel(true
            );

            }
        });
    }

    public HdSwingStepWorker(Window component, String message)
    {


        dialog  = new LoadingDialog(component,message, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { cancel(true
            );

            }
        });
    }


    @Override
    protected void done() {
        super.done();

        RemoteData<T> result= null;
        HdException exception=null;
        try {
            result = get();
        } catch (InterruptedException e) {

            exception=HdException.create(e.getCause());
            e.printStackTrace();
        } catch (ExecutionException e) {
            exception=HdException.create(e.getCause());
            e.printStackTrace();
        }

        dialog.setVisible(false);
        dialog.dispose();

        if(exception==null) {
            onResult(result);
        }else
        {
            onHandleError(exception);
        }
    }


    /**
     * 启动任务bu
     */
    public void go()
    {

        execute();
        dialog.setVisible(true);

    }

    public void onHandleError(HdException exception)
    {


        //TODO  处理线程异常



    }

    public    abstract void onResult(RemoteData<T> data);

}
