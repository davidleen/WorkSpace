package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.dialogs.ProductTemplateDialog;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.ProductTableModel;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.noEntity.ProductListViewType;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Product;
import com.giants3.hd.noEntity.ProductDetail;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * 产品列表界面
 */
@Singleton
public class Panel_ProductList extends BasePanel {

    @Inject
    ApiManager apiManager;


    private JPanel panel1;
    private JButton btn_search;
    private JLabel product_title;
    private JTextField productName;
    private JTable productTable;
    private JButton btn_add;
    private Panel_Page pagePanel;
    private JComboBox cb_type;


    @Inject
    ProductTableModel tableModel;


    public Panel_ProductList() {
        super();


        Vector<String> vector=new Vector<>();
        vector.add("    ");
        vector.add("白胚未处理");
        vector.add("组装未处理");
        vector.add("油漆未处理");
        vector.add("包装未处理");


        cb_type.setModel(new DefaultComboBoxModel<String>(ProductListViewType.vector));
        //productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct(productName.getText().toString().trim());


            }
        });

        productName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct(productName.getText().toString().trim());

            }
        });

        //添加按钮事件
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                if(CacheManager.getInstance().bufferData.demos!=null&& CacheManager.getInstance().bufferData.demos.size()>0)
                {
                    int size= CacheManager.getInstance().bufferData.demos.size();
                    if(size==1)
                    {
                        ProductDetail detail=CacheManager.getInstance().bufferData.demos.get(0);
                        detail=  (ProductDetail) ObjectUtils.deepCopy(detail);
                        detail.product.name="";
                        HdSwingUtils.showDetailPanel(SwingUtilities.getWindowAncestor(getRoot()),detail );
                    }else
                    {

                        ProductTemplateDialog dialog=    new ProductTemplateDialog(getWindow()) ;

                        dialog.setVisible(true);
                        ProductDetail detail=dialog.getResult();
                        if(detail!=null)
                        {
                            detail=  (ProductDetail) ObjectUtils.deepCopy(detail);
                            detail.product.name="";
                            HdSwingUtils.showDetailPanel(SwingUtilities.getWindowAncestor(getRoot()),detail );
                        }

                    }



                }else
                {
                    HdSwingUtils.showDetailPanel(null, getRoot());
                }





            }
        });
        productTable.setModel(tableModel);


        productTable.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = productTable.getSelectedRow();
                    Product product = tableModel.getItem(row);


                    int column = productTable.convertColumnIndexToModel(productTable.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {
                        ImageViewDialog.showProductDialog(getWindow(getRoot()), product.getName(), product.getpVersion(),product.url);
                    } else {

                        HdSwingUtils.showDetailPanel(product, getRoot());

                    }


                }

            }
        });





        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {



                searchProduct(productName.getText().trim(), cb_type.getSelectedIndex(),pageIndex, pageSize);
            }
        });


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                searchProduct("");
            }
        });


        btn_add.setVisible(AuthorityUtil.getInstance().addProduct());


    }


    private void searchProduct(String productNameValue) {


       int viewType= cb_type.getSelectedIndex();


        searchProduct(productNameValue,viewType, 0, pagePanel.getPageSize());

    }

    private void searchProduct(final String productNameValue, final int viewType, final int pageIndex, final int pageSize) {


        new HdSwingWorker<Product, Object>(getWindow(getRoot())) {
            @Override
            protected RemoteData<Product> doInBackground() throws Exception {

                return apiManager.readProductList(productNameValue, viewType,pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<Product> data) {


                pagePanel.bindRemoteData(data);
                tableModel.setDatas(data.datas);
                productTable.scrollRectToVisible(productTable.getCellRect(0 - 1, 0, true));



            }
        }.go();


    }


    @Override
    public JComponent getRoot() {
        return panel1;
    }
}
