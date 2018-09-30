package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.ProductDeleteModel;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.viewImpl.Panel_Page;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.ProductDelete;
import com.giants3.hd.noEntity.ProductDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductDeleteDialog  extends BaseDialog<ProductDelete> {
    private JPanel contentPane;
    private JTextField jtf;
    private JButton btn_search;
    private JHdTable table;
    private Panel_Page panel_page;


    @Inject
    private ProductDeleteModel tableModel;



    @Inject
    ApiManager apiManager;

    public ProductDeleteDialog(Window window ) {
        super(window);
        setTitle("已经删除产品列表");
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
                    final ProductDelete productDelete = tableModel.getItem(modelRowId);

                    //报价详情

                    new HdSwingWorker<ProductDetail,Object>((Window)getParent())
                    {
                        @Override
                        protected RemoteData<ProductDetail> doInBackground() throws Exception {


                            return   apiManager.productDetailOfDeleted(productDelete.id);

                        }

                        @Override
                        public void onResult(RemoteData<ProductDetail> data) {

                            //显示产品信息
                            if(data.isSuccess())

                            {


                                HdSwingUtils.showDetailPanel(ProductDeleteDialog.this,data.datas.get(0),productDelete);


                            }else
                            {
                                JOptionPane.showMessageDialog(ProductDeleteDialog.this,data.message);

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
    private  void bindRemoteData(RemoteData<ProductDelete> productDeleteRemoteData)
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



        new HdSwingWorker<ProductDelete,Object>((Window)getParent())
        {
            @Override
            protected RemoteData<ProductDelete> doInBackground() throws Exception {


                return   apiManager.loadDeleteProducts(value,pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<ProductDelete> data) {


                bindRemoteData(data);

            }
        }.go();




    }



}
