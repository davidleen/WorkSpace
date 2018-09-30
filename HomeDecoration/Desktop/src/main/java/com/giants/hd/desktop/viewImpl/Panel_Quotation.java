package com.giants.hd.desktop.viewImpl;


import com.giants.hd.desktop.frames.QuotationDetailFrame;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.QuotationHelper;
import com.giants.hd.desktop.model.QuotationTableModel;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.QuotationDetail;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 报价列表界面
 */
public class Panel_Quotation extends BasePanel {
    private JPanel panel1;
    private JTextField jtf_product;
    private JButton btn_search;
    private JButton btn_add;
    private JHdTable tb;
    private Panel_Page pagePanel;
    private JButton btn_add_xiankang;
    private JComboBox cb_salesman;


    //加载进度条
    LoadingDialog dialog;

    @Inject
    ApiManager apiManager;

    @Inject
    QuotationTableModel tableModel;

    boolean limitSelf;

    public Panel_Quotation() {
        limitSelf = CacheManager.getInstance().bufferData.quoteAuth.limitSelf;

        tb.setModel(tableModel);

        dialog = new LoadingDialog(getWindow(getRoot()), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        });


    }

    private void init() {



        cb_salesman.setVisible(!limitSelf);
        List<User> users= QuotationHelper.getQuotationUsersOnLoginUser();
        for (User user:users)
        cb_salesman.addItem(user);

        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                loadData();
            }
        });

        jtf_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loadData();
            }
        });

        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                loadData(pageIndex, pageSize);

            }
        });

        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {


                    final Quotation quotation = tableModel.getItem(tb.convertRowIndexToModel(tb.getSelectedRow()));


                    UseCaseFactory.getInstance().createQuotationDetail(quotation.id).execute(new Subscriber<QuotationDetail>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.setVisible(false);
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()), e.getMessage());
                        }

                        @Override
                        public void onNext(final QuotationDetail o) {

                            dialog.setVisible(false);
                            JFrame frame = new QuotationDetailFrame(o);
                            frame.setLocationRelativeTo(getRoot());
                            frame.setVisible(true);


                        }
                    });

                    dialog.setVisible(true);


                }
            }
        });


        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addQuotationDetailFrame(Quotation.QUOTATION_TYPE_NORMAL, "普通报价");


            }
        });


        btn_add_xiankang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addQuotationDetailFrame(Quotation.QUOTATION_TYPE_XK, "咸康报价");


            }
        });


        boolean quotationAddable = AuthorityUtil.getInstance().addQuotation();

        btn_add.setVisible(quotationAddable);
        btn_add_xiankang.setVisible(quotationAddable);
    }


    private void addQuotationDetailFrame(int quotationType, String typeName) {
        QuotationDetail quotationDetail = new QuotationDetail();
        quotationDetail.init();
        quotationDetail.quotation.quotationTypeId = quotationType;
        quotationDetail.quotation.quotationTypeName = typeName;
        JFrame frame = new QuotationDetailFrame(quotationDetail);
        frame.setLocationRelativeTo(getRoot());
        frame.setVisible(true);
    }


    @Override
    public JComponent getRoot() {
        return panel1;
    }


    /**
     * 读取数据
     */
    public void loadData()

    {

        loadData(0, pagePanel.getPageSize());

    }


    private void loadData(final int pageIndex, final int pageSize) {
        final String searchValue = jtf_product.getText().trim();

        final long salemansId;
        if (limitSelf) {
            salemansId = CacheManager.getInstance().bufferData.loginUser.id;
        } else {
            salemansId = ((User) cb_salesman.getSelectedItem()).id;
        }


        new HdSwingWorker<Quotation, Object>(getWindow(getRoot())) {
            @Override
            protected RemoteData<Quotation> doInBackground() throws Exception {


                return apiManager.loadQuotation(searchValue, salemansId, pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<Quotation> data) {


                pagePanel.bindRemoteData(data);
                tableModel.setDatas(data.datas);

            }
        }.go();


    }


}
