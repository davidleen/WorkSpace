package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 简单实现的对话框类， 提供  列表对话框的  读取  保存 基本操作
 */
public abstract class BaseSimpleDialog
        <T>  extends BaseDialog<T>{


    List<T> oldData;


    public BaseSimpleDialog(Window window) {
        super(window);
        //设置默认配置
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
        new HdSwingWorker<T,Object>(this)
        {
            @Override
            protected RemoteData<T> doInBackground() throws Exception {



                return  readData();

            }

            @Override
            public void onResult(RemoteData<T> data) {


                if(data.isSuccess())
                {

                    setNewData(data);

                }else
                {
                    JOptionPane.showMessageDialog(BaseSimpleDialog.this, data.message);

                }


            }
        }.go();
    }




    /**
     * 数据保存方法
     */
    protected   void doSaveWork()
    {

            final  List<T> newData=getTableModel().getValuableDatas();
            if(newData.equals(oldData))
            {


                    JOptionPane.showMessageDialog(BaseSimpleDialog.this, "数据无改变！");
                    return;

            }

            new HdSwingWorker<T,Object>(this)
            {
                @Override
                protected RemoteData<T> doInBackground() throws Exception {



                 return saveData(newData);



                }

                @Override
                public void onResult(RemoteData<T> data) {

                    if(data==null)
                    {
                        JOptionPane.showMessageDialog(BaseSimpleDialog.this, "对话框未覆盖方法 saveData  ");
                        return;
                    }


                    if(data.isSuccess())
                    {

                        setNewData(data);

                        JOptionPane.showMessageDialog(BaseSimpleDialog.this,"保存成功");
                    }else
                    {
                        JOptionPane.showMessageDialog(BaseSimpleDialog.this, data.message);
                        doSomethingOnError(data);
                    }





                }
            }.go();
    }


    public void doSomethingOnError(RemoteData<T> data)
    {

    }

    protected void setNewData(RemoteData<T>  newData) {
        oldData = (java.util.List<T>) ObjectUtils.deepCopy(newData.datas);
        getTableModel().setDatas(newData.datas);
    }


    protected abstract RemoteData<T> readData()throws HdException;
    protected   RemoteData<T> saveData(List<T> datas)throws HdException
    {

        return null;
    }

    protected abstract BaseTableModel<T> getTableModel() ;

    protected abstract  void init();

}
