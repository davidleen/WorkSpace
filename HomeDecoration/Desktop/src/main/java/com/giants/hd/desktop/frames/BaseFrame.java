package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.IPresenter;
import com.google.inject.Guice;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  所有frame 框架的基类
 */
public class BaseFrame  extends JFrame  implements IPresenter {




    public BaseFrame(  ) {

        this("");


    }

    public BaseFrame(  String title) {
        super(title);
        //setIcon;
        Guice.createInjector().injectMembers(this);

        try {
            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("icons/logo.jpg")));
        } catch (Throwable e) {
            e.printStackTrace();
        }



        //修改默认的关闭操作
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //添加关闭监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (hasModifyData()) {

                    int option = JOptionPane.showConfirmDialog(BaseFrame.this, "数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

                    if (JOptionPane.OK_OPTION == option) {
                        //点击了确定按钮
                        BaseFrame.this.dispose();
                    }

                } else {
                    BaseFrame.this.dispose();
                }


            }
        });
    }


    @Override
    public void close() {
        dispose();
    }

    /**
     * 默认为false
     * @return
     */
    @Override
    public boolean hasModifyData() {
        return false;
    }
}
