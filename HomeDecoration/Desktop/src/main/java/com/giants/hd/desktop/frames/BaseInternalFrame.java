package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.IPresenter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by david on 2015/11/23.
 */
public abstract class BaseInternalFrame extends JInternalFrame  implements IPresenter {



    private int frameResult=0;

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;


    private FrameResultListener frameResultListener;
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;
    static {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH=screenSize.width;
        SCREEN_HEIGHT=screenSize.height;


    }

    public BaseInternalFrame(String title) {
        super(title ,
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        ++openFrameCount;

        //...Create the GUI and put it in the window...

        //...Then set the window size or call pack...
        setContentPane(getCustomContentPane());
        setSize(SCREEN_WIDTH/2,SCREEN_HEIGHT/2);


        //Set the window's location.
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);

    }



    protected abstract Container getCustomContentPane();


    @Override
    public void close() {

        dispose();
    }

    @Override
    public void dispose() {


        super.dispose();
    }

    @Override
    public boolean hasModifyData() {
        return false;
    }


    public void showInMain()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main.getInstance().addInterFrame(BaseInternalFrame.this);
            }
        });

    }


    @Override
    public void doDefaultCloseAction() {

        if (hasModifyData()) {

            int option = JOptionPane.showConfirmDialog(BaseInternalFrame.this, "数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

            if (JOptionPane.OK_OPTION == option) {
                //点击了确定按钮
                super.doDefaultCloseAction();
                doOnResult();
            }

        } else {
            super.doDefaultCloseAction();
            doOnResult();
        }

    }

    private void doOnResult()
    {
        if(frameResultListener!=null)
        {


            frameResultListener.onFrameResult(0,frameResult,null);
        }
    }

    /**
     *  设置frame返回结果
     * @param frameResult
     */
    public void setFrameResult(int frameResult) {
        this.frameResult = frameResult;
    }

    public void setFrameResultListener(FrameResultListener frameResultListener) {
        this.frameResultListener = frameResultListener;
    }
}
