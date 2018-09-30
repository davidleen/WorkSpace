package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants.hd.desktop.utils.Config;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.User;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginDialog extends BaseDialog<User> {
    private JPanel contentPane;
    private JTextField tf_password;
    private JButton btn_login;
    private JButton btn_logout;
    private JComboBox cb_user;


    @Inject
    ApiManager apiManager;


    public LoginDialog(Window window) {
        super(window, "登录");

        setMinimumSize(new Dimension(400, 300));
        setContentPane(contentPane);
        pack();

        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                login();
            }
        });


        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                dispose();
            }
        });


        tf_password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                login();
            }
        });


        loadUsers();


    }


    private void loadUsers() {

        UseCaseFactory.getInstance().createGetUserListUseCase().execute(new Subscriber<java.util.List<User>>() {
            @Override
            public void onCompleted() {


                if(Config.DEBUG)
                {

                    tf_password.setText("xin2975.");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    });

                }



            }

            @Override
            public void onError(Throwable e) {
                JOptionPane.showMessageDialog(LoginDialog.this, e.getMessage());
            }


            @Override
            public void onNext(List<User> users) {
                cb_user.removeAllItems();
                for (User user : users)
                    cb_user.addItem(user);


                User user = LocalFileHelper.get(User.class);
                int selectIndex = 0;
                if (user != null) {
                    for (int i = 0, count = cb_user.getItemCount(); i < count; i++) {

                        User temp = (User) cb_user.getItemAt(i);
                        if (temp.id == user.id) {
                            selectIndex = i;
                            break;
                        }


                    }


                }


                cb_user.setSelectedIndex(selectIndex);


                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        final User item = (User) cb_user.getSelectedItem();
                        if("999".equals(item.code))
                        {
                            tf_password.setText("111");

                        }


                    }
                });
            }

        });

    }


    public void login() {


        final String userName = ((User) (cb_user.getSelectedItem())).name;
        final String password = tf_password.getText().toString();

        new HdSwingWorker<User, Object>(this) {
            @Override
            protected RemoteData<User> doInBackground() throws Exception {
                return apiManager.login(userName, password);
            }

            @Override
            public void onResult(RemoteData<User> data) {

                if (data.isSuccess()) {


                    //登录成功

                    HttpUrl.setToken(data.token);

                    if (data.newVersionCode > 0 && !StringUtils.isEmpty(data.newVersionName)) {

                        JOptionPane.showMessageDialog(LoginDialog.this, "有最新版本客户端，请及时更新。。。");
                    }


                    User user = data.datas.get(0);
                    setResult(user);


                    LocalFileHelper.set(user);


                    dispose();


                } else {

                    JOptionPane.showMessageDialog(LoginDialog.this, data.message);
                }


            }
        }.go();
    }

}
