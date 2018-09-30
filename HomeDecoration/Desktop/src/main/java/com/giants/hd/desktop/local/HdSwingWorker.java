package com.giants.hd.desktop.local;

import com.giants.hd.desktop.dialogs.LoginDialog;
import com.giants.hd.desktop.viewImpl.LoadingDialog;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * 自定义worker 类 封装 进度条等方法。
 */
public abstract class HdSwingWorker<T, V> extends SwingWorker<RemoteData<T>, V> {
    protected LoadingDialog dialog;

    private Window window;


    public HdSwingWorker(Window component) {

        this(component, null);
    }

    public HdSwingWorker(Window component, String message) {


        this.window = component;

        dialog = new LoadingDialog(component, message, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(true
                );

            }
        });
    }


    public void publishMessage(V... message)

    {

        publish(message);
    }

    @Override
    protected void done() {
        super.done();

        RemoteData<T> result = null;
        HdException exception = null;
        try {
            result = get();
        } catch (InterruptedException e) {
            exception = getCauseException(e);

            //  exception=HdException.create(e.getCause());
            e.printStackTrace();
        } catch (ExecutionException e) {
            exception = getCauseException(e);
            //   exception=HdException.create(e.getCause());
            e.printStackTrace();
        } catch (CancellationException e) {

            e.printStackTrace();

        } catch (Throwable e) {
            exception = getCauseException(e);
            e.printStackTrace();

        }


        dialog.setVisible(false);
        dialog.dispose();

        if (exception == null) {

            if(result==null)
            {
                //操作取消
                return;
            }
            if (result != null && result.code == RemoteData.CODE_UNLOGIN) {


                JOptionPane.showMessageDialog(window, "登录超时，请重新登录。");

                LoginDialog dialog = new LoginDialog(window);
                dialog.setModal(true);
                dialog.setVisible(true);

                return;
            }
            onResult(result);
        } else {


            onHandleError(exception);
        }
    }


    @Override
    protected void process(List<V> chunks) {
        super.process(chunks);
    }

    private HdException getCauseException(Throwable t) {

        Throwable newT = t;
        do {

            if (newT instanceof HdException) {
                return (HdException) newT;

            }
            newT = newT.getCause();
        } while (newT != null);

        return HdException.create(t);


    }

    /**
     * 启动任务bu
     */
    public void go() {

        execute();
        dialog.setVisible(true);

    }

    public void onHandleError(HdException exception) {


        //TODO  处理线程异常


        String errorMessage = "";
        if (exception.getCause() != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            exception.getCause().printStackTrace(new PrintStream(byteArrayOutputStream));
            errorMessage = byteArrayOutputStream.toString();
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMessage = exception.message;

        }

        JTextArea jTextArea = new JTextArea(errorMessage);
        jTextArea.setLineWrap(true);


        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setPreferredSize(new Dimension(800, 500));
        JOptionPane.showMessageDialog(null, jScrollPane);


    }

    public abstract void onResult(RemoteData<T> data);

}
