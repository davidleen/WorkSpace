package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.dialogs.ProductQRDialog;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants.hd.desktop.local.SimpleSwingWorker;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.ProductTableModel;
import com.giants.hd.desktop.utils.QRHelper;
import com.giants3.hd.noEntity.QRProduct;
import com.giants3.hd.utils.FileUtils;
import com.giants3.report.jasper.ProductWithQR;
import com.giants3.report.jasper.QrProductReport;
import com.giants3.report.jasper.product_list_4.ProductList4Report;
import com.giants.hd.desktop.reports.products.Excel_ProductReport;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Product;
import rx.Subscriber;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 产品报表界面
 */
public class Panel_ProductReport extends BasePanel {
    private JPanel contentPane;
    private JTextField start;
    private JTextField end;
    private JCheckBox include;
    private JButton export2;
    private JButton search;
    private JHdTable jt;
    private JButton export1;
    private JTextField tf_random;

    private JTabbedPane tabbedPane1;
    private JCheckBox random_include;
    private JButton btn_random_search;
    private JButton btn_report3;
    private JButton export4;
    private JButton printQR;
    private JButton printQRA4;
    ProductTableModel model;


    private java.util.List<Product> products;


    private Window window;

    public Panel_ProductReport() {


        window = SwingUtilities.getWindowAncestor(getRoot());


        model = new ProductTableModel();
        jt.setModel(model);

        jt.addMouseListener(new MouseInputAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    super.mouseClicked(e);

                                    if (e.getClickCount() == 2) {


                                        int row = jt.getSelectedRow();
                                        Product product = model.getItem(row);


                                        int column = jt.convertColumnIndexToModel(jt.getSelectedColumn());
                                        //单击第一列 显示原图
                                        if (column == 0) {
                                            ImageViewDialog.showProductDialog(window, product.getName(), product.getpVersion(), product.url);
                                        } else {

                                            HdSwingUtils.showDetailPanel(product, window);

                                        }


                                    }

                                }


                                @Override
                                public void mouseReleased(MouseEvent e) {
                                    super.mouseReleased(e);
                                    showMenu(e);

                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    super.mouseReleased(e);
                                    showMenu(e);

                                }

                                private void showMenu(MouseEvent e) {
                                    if (e.isPopupTrigger()) {
                                        final JTable table = (JTable) e.getSource();
                                        JPopupMenu menu = new JPopupMenu();


                                        JMenuItem deleteItem = new JMenuItem("删除 ");
                                        deleteItem.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if (products == null) return;
                                                int tableRow[] = JTableUtils.getSelectedRowSOnModel(table);

                                                if (tableRow.length == 0) {
                                                    JOptionPane.showMessageDialog(table, "请选择行进行删除。");
                                                    return;
                                                }


                                                if (table.getModel() instanceof BaseTableModel) {
                                                    BaseTableModel model = (BaseTableModel) table.getModel();
                                                    for (int i : tableRow) {
                                                        products.remove(model.getItem(i));
                                                    }
                                                    model.setDatas(products);


                                                }


                                            }
                                        });
                                        menu.add(deleteItem);


                                        menu.show(e.getComponent(), e.getX(), e.getY());

                                    }
                                }
                            }
        );


        //添加行删除。


        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String startNum = start.getText().trim();
                String endNum = end.getText().trim();
                boolean withCopy = include.isSelected();


                UseCaseFactory.getInstance().createProductByNameBetween(startNum, endNum, withCopy).execute(new Subscriber<java.util.List<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingDialog();

                        showMesssage(e.getMessage());
                    }

                    @Override
                    public void onNext(java.util.List<Product> newProducts) {
                        hideLoadingDialog();
                        products = newProducts;

                        model.setDatas(products);


                    }
                });
                showLoadingDialog();

            }


        });


        export2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (products == null || products.size() == 0) {

                    showMesssage("无数据导出");
                    return;
                }

                final File file = SwingFileUtils.getSelectedDirectory();
                if (file == null) return;
                new HdSwingWorker<Void, Void>(window) {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {
                        new Excel_ProductReport().reportProduct2(products, file.getPath());
                        return new RemoteData<Void>();
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {


                        showMesssage("导出成功");
                    }
                }.go();

            }
        });


        export1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (products == null || products.size() == 0) {


                    return;
                }

                final File file = SwingFileUtils.getSelectedDirectory();
                if (file == null) return;
                new HdSwingWorker<Void, Void>(window) {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {
                        new Excel_ProductReport().reportProduct1(products, file.getPath());
                        return new RemoteData<Void>();
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {


                        showMesssage("导出成功");
                    }
                }.go();


            }
        });
        btn_report3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (products == null || products.size() == 0) {

                    showMesssage("无数据导出");
                    return;
                }

                final File file = SwingFileUtils.getSelectedDirectory();
                if (file == null) return;
                new HdSwingWorker<Void, Void>(window) {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {
                        new Excel_ProductReport().reportProduct3(products, file.getPath());
                        return new RemoteData<Void>();
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {


                        showMesssage("导出成功");

                    }
                }.go();


            }
        });


        export4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (products == null || products.size() == 0) {

                    showMesssage("无数据导出");

                    return;
                }

                new ProductList4Report(products).report();


            }
        });

        //查询动作
        ActionListener randomSearchActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String s = tf_random.getText().trim();
                if (StringUtils.isEmpty(s)) {

                    showMesssage("请输入产品货号");
                    tf_random.requestFocus();
                    return;
                }

                boolean withCopy = random_include.isSelected();
                UseCaseFactory.getInstance().createProductByNameRandom(s, withCopy).execute(new Subscriber<RemoteData<Product>>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingDialog();
                        showMesssage(e.getMessage());


                    }

                    @Override
                    public void onNext(RemoteData<Product> remoteData) {
                        hideLoadingDialog();

                        java.util.List<Product> newProducts = remoteData.datas;
                        if (newProducts.size() == 0) {
                            showMesssage("未查到产品记录");


                            tf_random.requestFocus();
                            return;

                        }

                        if (products == null) {
                            products = new ArrayList<Product>();

                        }

                        //最新查询的追加到最前。
                        products.addAll(0, newProducts);

                        model.setDatas(products);


                    }
                });
                showLoadingDialog();

            }


        };

        btn_random_search.addActionListener(randomSearchActionListener);

        tf_random.addActionListener(randomSearchActionListener);


        printQR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(products==null||products.size()==0)
                {

                    showMesssage("无记录打印");
                    return;
                }


                printQR("qrproduct" );


            }
        });


        printQRA4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(products==null||products.size()==0)
                {

                    showMesssage("无记录打印");
                    return;
                }


                printQR("qrproducta4" );


            }
        });
    }

    private void printQR(final String qrFileName) {
        new SimpleSwingWorker<java.util.List<ProductWithQR>, Void>(window,"正在处理。。。") {

            @Override
            protected java.util.List<ProductWithQR> doInBackground() throws Exception {

                java.util.List<ProductWithQR> qrProducts=new ArrayList<>();
                for (Product product:products)
                {
                    String  qrLocalPath= LocalFileHelper.path+"/qr/temp"+product.id+".png";
                    FileUtils.makeDirs(qrLocalPath);
                    QRProduct qrProduct=  QRHelper.generate(product);
                    int qrSize=400;
                    BufferedImage scaledImg = QRHelper.generateQRCode(qrProduct,qrSize, qrSize) ;
                    try {
                        ImageIO.write(scaledImg, "png",new File( qrLocalPath));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    ProductWithQR productWithQR=new ProductWithQR();
                    productWithQR.product=product;
                    productWithQR.qrFilePath=qrLocalPath;
                    qrProducts.add(productWithQR);
                }

                return qrProducts;

            }

            @Override
            public void onResult( java.util.List<ProductWithQR> data) {

                try {




                    new QrProductReport(data, qrFileName).report();
                } catch (Throwable e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(Panel_ProductReport.this.getWindow(),e1.getMessage());
                }


            }
        }.go();
    }

    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return contentPane;
    }


}
