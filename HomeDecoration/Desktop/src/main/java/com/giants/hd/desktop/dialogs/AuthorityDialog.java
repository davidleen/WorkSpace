package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.AuthorityModel;
import com.giants.hd.desktop.model.UserModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Authority;
import com.giants3.hd.entity.User;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 权限设置
 */
public class AuthorityDialog extends BaseDialog<Authority> {
    private JPanel contentPane;
    private JHdTable jt_user;
    private JHdTable jt_authority;
    private JButton btn_save;



    private java.util.List<Authority> oldData;

    @Inject
    ApiManager apiManager;

    @Inject
    UserModel userModel;

    @Inject
    AuthorityModel authorityModel;
    public AuthorityDialog(Window window) {

        super(window,"权限配置");
        setContentPane(contentPane);
        setMinimumSize(new Dimension(1100,700));


        jt_user.setModel(userModel);
        userModel.setEditable(false);
        userModel.setRowAdjustable(false);
        authorityModel.setRowAdjustable(false);

        jt_authority.setModel(authorityModel);

        jt_user.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {


                    int modelRow = jt_user.convertRowIndexToModel(jt_user.getSelectedRow());
                    loadAuthority(userModel.getItem(modelRow));
                }
            }
        });

        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                save();

            }
        });

        loadData();
    }


    /**
     * 读取数据
     */
    private void loadData() {
        new HdSwingWorker<User,Object>(this)
        {
            @Override
            protected RemoteData<User> doInBackground() throws Exception {
                return  apiManager.readUsers();
            }

            @Override
            public void onResult(RemoteData<User> data) {
                if(data.isSuccess())
                {
                    userModel.setDatas(data.datas);

                }else
                {
                    JOptionPane.showMessageDialog(AuthorityDialog.this, data.message);
                }


            }
        }.go();
    }

    /**
     * 读取权限列表
     * @param user
     */
    private void loadAuthority(final User user)
    {



        new HdSwingWorker<Authority,Object>(this)
        {
            @Override
            protected RemoteData<Authority> doInBackground() throws Exception {
                return apiManager.readAuthorityByUser(user.id);
            }

            @Override
            public void onResult(RemoteData<Authority> data) {
                if(data.isSuccess())
                {
                    oldData= (java.util.List<Authority>) ObjectUtils.deepCopy(data.datas);
                    authorityModel.setDatas(data.datas);
                }else
                {
                    JOptionPane.showMessageDialog(AuthorityDialog.this, data.message);
                }


            }
        }.go();


    }

    private void save()
    {

     final   User user=userModel.getItem(jt_user.convertRowIndexToModel(jt_user.getSelectedRow()));
      final  java.util.List<Authority> newData=authorityModel.getDatas();
        if(oldData.equals(newData))
        {
            JOptionPane.showMessageDialog(this,"数据无改变");
            return;
        }
        else
        {


            new HdSwingWorker<Authority,Object>(this)
            {
                @Override
                protected RemoteData<Authority> doInBackground() throws Exception {
                    return apiManager.saveAuthorities(user.id,newData);
                }

                @Override
                public void onResult(RemoteData<Authority> data) {
                    if(data.isSuccess())
                    {
                        JOptionPane.showMessageDialog(AuthorityDialog.this,"保存成功");
                        oldData= (java.util.List<Authority>) ObjectUtils.deepCopy(data.datas);
                        authorityModel.setDatas(data.datas);
                    }else
                    {
                        JOptionPane.showMessageDialog(AuthorityDialog.this, data.message);
                    }


                }
            }.go();


        }


    }

}
