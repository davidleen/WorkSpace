package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.frames.QuotationDetailFrame;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.QuotationDeleteModel;
import com.giants.hd.desktop.viewImpl.Panel_Page;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.QuotationDelete;
import com.giants3.hd.noEntity.QuotationDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class QuotationDeleteDialog extends BaseDialog<QuotationDelete> {
    private JPanel contentPane;
    private JTextField jtf;
    private JButton btn_search;
    private JHdTable table;
    private Panel_Page panel_page;


    @Inject
    private QuotationDeleteModel tableModel;



    @Inject
    ApiManager apiManager;

    public QuotationDeleteDialog(Window window) {
        super(window);
        setTitle("已经删除报价列表");
        setSize(new Dimension(800,600));
        setContentPane(contentPane);

        table.setModel(tableModel);



        panel_page.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                search(jtf.getText().trim(), pageIndex, pageSize);


            }
        });



        table.setModel(tableModel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {

                    int modelRowId = table.convertRowIndexToModel(table.getSelectedRow());
                    final QuotationDelete quotationDelete = tableModel.getItem(modelRowId);

                    //报价详情

                    new HdSwingWorker<QuotationDetail,Object>((Window)getParent())
                    {
                        @Override
                        protected RemoteData<QuotationDetail> doInBackground() throws Exception {


                            return   apiManager.quotationDetailOfDeleted(quotationDelete.id);

                        }

                        @Override
                        public void onResult(RemoteData<QuotationDetail> data) {

                            //显示产品信息
                            if(data.isSuccess())

                            {
                                QuotationDetail detail=data.datas.get(0);
;
                                JFrame frame;
                                //if (detail.quotation.quotationTypeId == Quotation.QUOTATION_TYPE_XK) {
                                    frame = new QuotationDetailFrame(detail,quotationDelete);

//                                } else {
//                                    frame = new QuotationDetailFrame(detail);
//                                }

                                frame.setLocationRelativeTo(QuotationDeleteDialog.this);
                                frame.setVisible(true);



                            }else
                            {
                                JOptionPane.showMessageDialog(QuotationDeleteDialog.this,data.message);

                            }


                        }
                    }.go();









                }
            }
        });


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String value = jtf.getText();
                search(value);
            }
        });


        search("");


    }


    /**
     * 绑定远程查询到的数据
     * @param productDeleteRemoteData
     */
    private  void bindRemoteData(RemoteData<QuotationDelete> productDeleteRemoteData)
    {
        panel_page.bindRemoteData(productDeleteRemoteData);
        tableModel.setDatas(productDeleteRemoteData.datas);
    }
    /**
     * 开启搜索功能
     *
     * @param value
     */

    public void search(final String value )
    {
        search(value, 0, panel_page.getPageSize());
    }
    public void search(final String value,final int pageIndex, final int pageSize)
    {



        new HdSwingWorker<QuotationDelete,Object>((Window)getParent())
        {
            @Override
            protected RemoteData<QuotationDelete> doInBackground() throws Exception {


                return   apiManager.loadDeleteQuotations(value, pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<QuotationDelete> data) {


                bindRemoteData(data);

            }
        }.go();




    }



}
