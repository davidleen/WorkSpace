package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.User;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 修改密码对话框
 */
public class UpdatePasswordDialog extends BaseDialog<User> {
    private JPanel contentPane;
    private JTextField tf_user;
    private JPasswordField pf_old;
    private JPasswordField pf_new;
    private JPasswordField pf_new2;
    private JButton save;




    @Inject
    ApiManager apiManager;

    public UpdatePasswordDialog(Window window) {
        super(window,"修改密码");
        setContentPane(contentPane);

        initValue();

    }

    private void initValue() {


        tf_user.setText(CacheManager.getInstance().bufferData.loginUser.toString());
        tf_user.setEnabled(false);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String oldPassword = new String(pf_old.getPassword()) .trim();
                String new1Password = new String(pf_new.getPassword()).trim();
                String new2Password = new String(pf_new2.getPassword()) .trim();
                if (!new1Password.equals(new2Password)) {
                    JOptionPane.showMessageDialog(UpdatePasswordDialog.this, "两次密码输入不一致，请重新输入");
                    pf_new.requestFocus();
                    return;
                }

                updatePassword(CacheManager.getInstance().bufferData.loginUser.id,oldPassword,new1Password);
            }
        });

    }


    public void updatePassword(final long userId, final String oldPassword, final String newPassword)
    {




        new HdSwingWorker<Void,Object>((Window)getParent())
        {
            @Override
            protected RemoteData<Void> doInBackground() throws Exception {


                return   apiManager.updatePassword(userId, oldPassword, newPassword);
            }

            @Override
            public void onResult(RemoteData<Void> data) {


                 if(data.isSuccess())
                 {
                     JOptionPane.showMessageDialog(UpdatePasswordDialog.this,"密码修改成功");
                     dispose();
                 }else
                 {
                     JOptionPane.showMessageDialog(UpdatePasswordDialog.this,data.message);
                 }

            }
        }.go();
    }


}
