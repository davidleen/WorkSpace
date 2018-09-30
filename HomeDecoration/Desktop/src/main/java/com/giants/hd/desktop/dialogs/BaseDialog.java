package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.mvp.IPresenter;
import com.google.inject.Guice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 所有对话框类的基类 提供注射功能
 *
 */
public class BaseDialog<T> extends JDialog implements IPresenter {



    protected  T result;
    public BaseDialog(Window window)
    {
        this(window, "默认对话框");

    }

    public BaseDialog(Window window, String title) {
        super(window, title);
        setModal(true);
        setLocationRelativeTo(window);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initDialog();
    }


    protected  void initDialog()
    {

         addWindowListener(new WindowAdapter() {
                               public void windowClosing(WindowEvent e) {

                                   System.out.println("closing........");
                                   close();
                               }

                             public void windowClosed(WindowEvent e) {
                                 removeWindowListener(this);
                                 System.out.println("windowClosed........");
                                 getContentPane().removeAll();
                                 final Window owner = BaseDialog.this.getOwner();
                                 if (owner != null) {
                                     owner.remove(BaseDialog.this);
                                 }

                             }
                               /**
                                * Invoked when a window has been opened.
                                *
                                * @param e
                                */

                           }
         );



         setMinimumSize(new Dimension(400,400));
         Guice.createInjector().injectMembers(this);
     }





   public  T getResult()
   {

            return result;

   }

    protected void setResult(T result)
    {
        this.result=result;
    }


    @Override
    public void close() {

        setVisible(false);
        dispose();
    }

    @Override
    public boolean hasModifyData() {
        return false;
    }
}
