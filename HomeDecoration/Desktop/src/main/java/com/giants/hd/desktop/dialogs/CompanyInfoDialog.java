package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdUIException;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.Company;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompanyInfoDialog extends BaseDialog {
    private JPanel contentPane;
    private JTextField tf_name;
    private JTextField tf_eName;

    private JTextField tf_email;
    private JTextField tf_address;

    private JButton save;

    private JTextField tf_eAddress;
    private JTextField tf_tel;
    private JTextField tf_fax;
    private JTextField tf_booth;

    Company oldData;
    Company newData;
    @Inject
    ApiManager apiManager;


    public CompanyInfoDialog(Window window) {
        super(window);
        setTitle(ModuleConstant.TITLE_COMPANY_INFO);

        setContentPane(contentPane);
        setSize(new Dimension(700, 500));
        final Company company= (Company) ObjectUtils.deepCopy(CacheManager.getInstance().bufferData.company);
        init(company);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                try {
                    getData(newData);
                }catch (HdException ex)
                {
                    JOptionPane.showMessageDialog(getParent(),ex.getMessage());
                    return ;
                }

               if(newData.equals(oldData))
               {

                   JOptionPane.showMessageDialog(getParent(),"数据无改变");
                   return;
               }



                UseCaseFactory.getInstance().createUpdateCompanyUseCase(newData).execute(new Subscriber<RemoteData<Company>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        JOptionPane.showMessageDialog(getParent(),e.getMessage());


                    }


                    @Override
                    public void onNext(RemoteData<Company> data) {


                        if (data.isSuccess()) {
                            JOptionPane.showMessageDialog(getParent(),"修改成功");
                            CacheManager.getInstance().bufferData.company=data.datas.get(0);

                        }else
                        {
                            JOptionPane.showMessageDialog(getParent(),data.message);
                        }
                    }

                });





            }
        });
    }




    public void getData(Company company) throws HdException {

        try {


            company.name =
                    tf_name.getText() ;
            company.eName = tf_eName.getText();
            company.email =  tf_email.getText() ;
            company.address = tf_address.getText().trim() ;
            company.eAddress = tf_eAddress.getText();
            company.tel= tf_tel.getText() ;
            company.fax= tf_fax.getText();
            company.boothNo= tf_booth.getText();




        }catch (Throwable t)
        {
            throw   HdUIException.create("输入的数据有错， 请输入数字,不能为空");
        }

    }


    private void init(Company company)
    {

        newData=company;
        this.oldData=(Company) ObjectUtils.deepCopy(company);

        tf_eName.setText(String.valueOf(company.eName));

        tf_email.setText(String.valueOf(company.email));
        tf_address.setText(String.valueOf(company.address));
        tf_name.setText(String.valueOf(company.name));

        tf_tel.setText(String.valueOf(company.tel))  ;
        tf_eAddress.setText(String.valueOf(company.eAddress))  ;

        tf_fax.setText(String.valueOf(company.fax))  ;
        tf_booth.setText(String.valueOf(company.boothNo))  ;



    }
}
