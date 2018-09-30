package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants3.hd.exception.HdException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 简单实现的对话框类， 提供  列表对话框的  读取  保存 基本操作
 */
public abstract class BaseListDialog
        <T>  extends BaseDialog<T>{




    public BaseListDialog(Window window) {
        super(window);
        //设置默认配置
        setModal(true);
        setMinimumSize(new Dimension(800, 600));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                init();
                doLoadWork();
            }
        });



    }


    /**
     * 数据读取方法
     */
    protected   void doLoadWork()
    {
        new SwingWorker<List<T> ,String>( )
        {
            @Override
            protected List<T> doInBackground() throws Exception {



                return  readData();

            }

            @Override
            protected void done() {


                List<T> data= null;
                try {
                    data = get();
                    setNewData(data);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }




            }
        }.execute();




    }


    private void setNewData(List<T>  newData) {

        getTableModel().setDatas(newData);
    }


    protected abstract List<T> readData()throws HdException;


    protected abstract BaseTableModel<T> getTableModel() ;

    protected abstract  void init();

}
