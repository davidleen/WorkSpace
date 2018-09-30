package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.dialogs.CopyProductDialog;
import com.giants.hd.desktop.dialogs.OperationLogDialog;
import com.giants.hd.desktop.dialogs.ProductQRDialog;
import com.giants.hd.desktop.dialogs.SearchDialog;
import com.giants.hd.desktop.filters.PictureFileFilter;
import com.giants.hd.desktop.interf.ComonSearch;
import com.giants.hd.desktop.interf.DataChangeListener;
import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.*;
import com.giants.hd.desktop.model.*;
import com.giants.hd.desktop.mvp.presenter.ProductDetailIPresenter;
import com.giants.hd.desktop.mvp.viewer.ProductDetailViewer;
import com.giants.hd.desktop.reports.excels.Report_Excel_ProductMaterialList;
import com.giants.hd.desktop.reports.products.Excel_ProductReport;
import com.giants.hd.desktop.utils.*;
import com.giants.hd.desktop.widget.AttachPanel;
import com.giants.hd.desktop.widget.TableMouseAdapter;
import com.giants.hd.desktop.widget.TablePopMenu;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.noEntity.ConstantData;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.StringUtils;
import com.google.inject.Inject;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * 产品详情界面
 */
public class Panel_ProductDetail extends BasePanel implements ProductDetailViewer {


    @Inject
    ApiManager apiManager;


    private JTextField tf_product;
    private JLabel lable2;
    private JLabel title;
    private JButton bn_save;

    private JLabel photo;
    private JTabbedPane tab_list;
    private JTextField tf_unit;
    private JComboBox<PClass> cb_class;
    private JTextField tf_constitute;
    private JTextField tf_product_cost;
    private JTextArea ta_spec_inch;
    private JTextArea ta_memo;
    private JPanel cellPanel;
    private JTable tb_conceptus_cost;


    private JTextField tf_fob;
    private JTextField tf_price;
    private JTextField tf_cost;


    private JTextField tf_weight;
    private JTextField tf_version;
    private JTextField tf_gross_profit;

    private JTextField tf_inboxCount;
    private JTextField tf_long;
    private JScrollPane contentPane;
    private JTable tb_pack_cost;
    private JTable tb_pack_wage;
    private JTable tb_product_paint;
    private JTextField jtf_paint_wage;
    private JTextField jtf_paint_cost;
    private JTabbedPane tabbedPane3;
    private JTable tb_conceptus_wage;
    private JTabbedPane tabbedPane4;
    private JTable tb_assemble_cost;
    private JTable tb_assemble_wage;
    private JTextField jtf_conceptus_wage;
    private JTextField jtf_conceptus_cost;
    private JTextField jtf_assemble_wage;
    private JTextField jtf_assemble_cost;

    private JDatePickerImpl date;
    private JCheckBox cb_xiankang;
    private Panel_Xiankang panel_xiankang;
    private JButton btn_export;
    private JTextField jtf_cost1;
    private JTextField jtf_cost8;
    private JTextField jtf_cost6;
    private JTextField jtf_cost7;
    private JTextField jtf_cost_banyungongzi;
    private JTextField jtf_cost11_15;
    private JTextField tf_price_dollar;
    private JTextField tf_volume;
    private JTextField tf_width;
    private JTextField tf_height;
    private JTextField tf_quantity;
    private JTextField tf_cost4;
    private JComboBox cb_pack;
    private JTabbedPane tabbedPane5;
    private JButton btn_copy;
    private JTextField jtf_mirror_size;
    private JButton btn_delete;
    private JTextArea ta_spec_cm;
    private Panel_XK_Pack jp_pack;


    private JLabel creator;

    private JLabel updateTime;
    private JLabel updator;
    private JLabel createTime;
    private JPanel jp_log;
    private JLabel viewLog;
    private JPanel panel_nomal;
    private JButton btn_resume;
    private JPanel panel_delete;
    private JTextField jtf_cost_repair;
    private JTextField jtf_pack_wage;
    private JTextField jtf_pack_cost;
    private JButton btn_attach_add;
    private AttachPanel panel_attach;
    private JComboBox<Factory> cb_factory;
    private JTextField tf_gangza;
    private JLabel lb_gangza;
    private JLabel lb_qr;
    private JButton btn_export_pic;
    private JButton exportList;
    private JTabbedPane tabbedPane1;
    private JTextArea ta_packinfo;
    private JButton btn_add_pack_image;
    private AttachPanel pack_attaches;

    private JButton workFlow;
    private JButton save2;


    /**
     * 旧数据 传递进来的数据
     */
    private ProductDetail oldData;

    public ProductDetail productDetail;


    @Inject
    ProductMaterialTableModel conceptusMaterialTableModel;

    @Inject
    ProductMaterialTableModel assembleMaterialTableModel;


    @Inject
    ProductWageTableModel conceptusWageTableModel;
    @Inject
    ProductWageTableModel assembleWageTableModel;


    @Inject
    ProductPaintTableModel productPaintModel;


    @Inject
    ProductPackMaterialTableModel packMaterialTableModel;
    @Inject
    ProductWageTableModel packWageTableModel;


    // ProductDetailTableModule module;


    /**
     * 规格英寸输入框的监听
     */
    private DocumentListener ta_spec_inchListener;
    /**
     * 规格厘米输入框的监听
     */
    private DocumentListener ta_spec_cmListener;


    /**
     * 所有表格表格监听对象
     */
    TableModelListener allTableModelListener;


    /**
     * 表格菜单的回调接口
     */
    private TablePopMenu.TableMenuLister tableMenuLister;


    private DocumentListener tf_quantityListener;
    private DocumentListener tf_inboxCountListener;
    /**
     * 单位改变的监听器
     */
    private DocumentListener tf_unitDocumentListener;


    /**
     * 外厂模式下包装长度改变的监听器
     */
    private DocumentListener tf_packSizeDocumentListener;
    /**
     * 外厂模式下实际成本改变监听器
     */
    private DocumentListener tf_foreign_relate_documentListener;


    /**
     * 包装改变监听类。
     */
    private ItemListener cb_packItemListener;

    /**
     * 工厂 改变监听类。
     */
    private ItemListener cb_factoryItemListener;

    GlobalData globalData;

    /**
     * 详情界面逻辑展示
     */
    ProductDetailIPresenter presenter;

    private boolean cannotViewPrice = false;


    public Panel_ProductDetail() {
        super();
        globalData = CacheManager.getInstance().bufferData.globalData;
        cannotViewPrice = !AuthorityUtil.getInstance().canViewPrice(ModuleConstant.NAME_PRODUCT);
        initComponent();


    }


    public void setPresenter(ProductDetailIPresenter presenter) {
        this.presenter = presenter;
    }


    /**
     * 设置数据
     */
    @Override
    public void setProductDetail(ProductDetail productDetail) {


        initPanel(productDetail);


    }

    /**
     * 设置数据
     */
    public void setProductDetail(ProductDetail productDetail, ProductDelete productDelete) {


        initPanel(productDetail, productDelete);


    }


    /**
     * 将产品信息 关联到包装表模型
     */
    private void setProductInfoToPackageModel() {

        //设置包装材料的 产品引用数据
        packMaterialTableModel.setProduct(productDetail.product);
        //设置包装材料的 产品引用数据
        packWageTableModel.setProduct(productDetail.product);
    }

    /*
    *   添加监听 监听文本变化，及表格变化。。（这些监听在开始时候需要移除 避免在初始化绑定数据时候触发监听）
     */
    private void addListeners() {


        tf_inboxCount.getDocument().addDocumentListener(tf_inboxCountListener);

        //数量改变

        updatePackListenersBaseOnFactory((Factory) cb_factory.getSelectedItem());


//        //修改规格监听
        //    ta_spec_cm.getDocument().addDocumentListener(ta_spec_cmListener);
//        //修改规格监听
        // ta_spec_inch.getDocument().addDocumentListener(ta_spec_inchListener);

        //设置厂家监听
        cb_factory.addItemListener(cb_factoryItemListener);
    }


    public void removeListeners() {
        //数量改变

        clearPackQuantityListener();
        tf_long.getDocument().removeDocumentListener(tf_packSizeDocumentListener);
        tf_width.getDocument().removeDocumentListener(tf_packSizeDocumentListener);
        tf_height.getDocument().removeDocumentListener(tf_packSizeDocumentListener);
        tf_product_cost.getDocument().removeDocumentListener(tf_foreign_relate_documentListener);


        tf_inboxCount.getDocument().removeDocumentListener(tf_inboxCountListener);


//        //修改规格监听
        ta_spec_cm.getDocument().removeDocumentListener(ta_spec_cmListener);
//        //修改规格监听
        ta_spec_inch.getDocument().removeDocumentListener(ta_spec_inchListener);
        //设置厂家分类
        cb_factory.removeItemListener(cb_factoryItemListener);

//
//        //注册表模型的监听
//        productPaintModel.removeTableModelListener(allTableModelListener);
//        assembleMaterialTableModel.removeTableModelListener(allTableModelListener);
//        assembleWageTableModel.removeTableModelListener(allTableModelListener);
//        conceptusMaterialTableModel.removeTableModelListener(allTableModelListener);
//        conceptusWageTableModel.removeTableModelListener(allTableModelListener);
//        packMaterialTableModel.removeTableModelListener(allTableModelListener);
//        packWageTableModel.removeTableModelListener(allTableModelListener);
    }


    private void initListeners() {
        //定义表格模型数据改变监听对象
        allTableModelListener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //数据改变  更新统计数据
                //汇总计算油漆工资与材料费用

                productDetail.conceptusMaterials = conceptusMaterialTableModel.getDatas();
                productDetail.conceptusWages = conceptusWageTableModel.getDatas();

                productDetail.assembleMaterials = assembleMaterialTableModel.getDatas();
                productDetail.assembleWages = assembleWageTableModel.getDatas();
                productDetail.paints = productPaintModel.getDatas();
                productDetail.packMaterials = packMaterialTableModel.getDatas();
                productDetail.packWages = packWageTableModel.getDatas();

                ProductAnalytics.updateProductStatistics(productDetail, globalData);


                bindStatisticsValue(productDetail.product);

            }
        };


//        //注册表模型的监听
//        productPaintModel.addTableModelListener(allTableModelListener);
//        assembleMaterialTableModel.addTableModelListener(allTableModelListener);
//        assembleWageTableModel.addTableModelListener(allTableModelListener);
//        conceptusMaterialTableModel.addTableModelListener(allTableModelListener);
//        conceptusWageTableModel.addTableModelListener(allTableModelListener);
//        packMaterialTableModel.addTableModelListener(allTableModelListener);
//        packWageTableModel.addTableModelListener(allTableModelListener);


        //装箱数量 修改监听器
        tf_quantityListener = new DocumentListener() {


            @Override
            public void insertUpdate(DocumentEvent e) {


                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        update();
                    }
                });

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        update();
                    }
                });

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


                // update();

            }


            private void update() {


                int newValue = 0;
                try {

                    newValue = Integer.valueOf(tf_quantity.getText().trim());
                    productDetail.product.packQuantity = newValue;
                    packMaterialTableModel.updateProduct();
                    packWageTableModel.fireTableDataChanged();


                } catch (Throwable t) {

                    if (Config.DEBUG) {
                        t.printStackTrace();
                    }
                }


            }
        };
//内盒数量 修改监听器
        tf_inboxCountListener = new DocumentListener() {


            @Override
            public void insertUpdate(DocumentEvent e) {


                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        update();
                    }
                });

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        update();
                    }
                });

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


                // update();

            }


            private void update() {


                int newValue = 0;
                try {

                    newValue = Integer.valueOf(tf_inboxCount.getText().trim());
                    productDetail.product.insideBoxQuantity = newValue;
                } catch (Throwable t) {

                }


            }
        };

        //英寸输入监听
        ta_spec_inchListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {


                updateCm();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCm();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }


            public void updateCm() {


                String cmString = StringUtils.convertInchStringToCmString(ta_spec_inch.getText().trim());
                bindProductSpecCmData(cmString);


            }
        };


        //厘米输入监听
        ta_spec_cmListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {


                updateInch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateInch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            public void updateInch() {


                String inchString = StringUtils.convertCmStringToInchString(ta_spec_cm.getText().trim());
                bindProductSpecInchData(inchString);

            }
        };


        //包材选择切换监听
        cb_packItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {


                if (e.getStateChange() == ItemEvent.SELECTED) {
                    productDetail.product.pack = (Pack) e.getItem();
                    packMaterialTableModel.updateProduct();
                }
            }
        };


        //packDataListener=new

        //厂家选择切换监听
        cb_factoryItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {


                if (Config.DEBUG) {

                    Config.log("itemStateChanged..................");
                }
                if (e.getStateChange() == ItemEvent.SELECTED) {


                    final Factory factory = (Factory) e.getItem();


                    updatePanelStateBaseOnFactory(factory);

                    updatePackListenersBaseOnFactory(factory);


                }
            }
        };


        tf_unitDocumentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            private void update() {

                productDetail.product.pUnitName = tf_unit.getText().trim();
                packMaterialTableModel.updateProduct();

            }
        };


        //外厂模式下  包装数据改变监听器
        tf_packSizeDocumentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {


                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        update();
                    }
                });
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            private void update() {


                if (Config.DEBUG) {
                    Config.log("updating。。。。。。。。。。。。。。。");
                }


                int inboxCount = 0;
                try {
                    inboxCount = Integer.valueOf(tf_inboxCount.getText().trim());
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                int quantity = 0;
                try {
                    quantity = Integer.valueOf(tf_quantity.getText().trim());
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                try {
                    float packLong = Float.valueOf(tf_long.getText().trim());
                    float packWidth = Float.valueOf(tf_width.getText().trim());
                    float packHeight = Float.valueOf(tf_height.getText().trim());
                    ProductAnalytics.updatePackData(productDetail.product, globalData, inboxCount, quantity, packLong, packWidth, packHeight);

                    tf_volume.setText(String.valueOf(productDetail.product.packVolume));
                    tf_gangza.setText(String.valueOf(productDetail.product.gangza));
                    tf_fob.setText(cannotViewProductPrice() ? "***" : String.valueOf(productDetail.product.fob));
                    tf_price.setText(cannotViewProductPrice() ? "***" : String.valueOf(productDetail.product.price));
                    tf_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(productDetail.product.cost));
                } catch (Throwable t) {
                    t.printStackTrace();
                }


            }
        };


        //外厂模式下  实际成本数据改变监听器
        tf_foreign_relate_documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        update();
                    }
                });
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            private void update() {

                float productCost = 0;
                try {
                    productCost = Float.valueOf(tf_product_cost.getText().trim());
                } catch (Throwable t) {
                    t.printStackTrace();
                }


                ProductAnalytics.updateCostOnForeignFactory(productDetail.product, globalData, productCost);

                tf_fob.setText(cannotViewProductPrice() ? "***" : String.valueOf(productDetail.product.fob));
                tf_price.setText(cannotViewProductPrice() ? "***" : String.valueOf(productDetail.product.price));
                tf_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(productDetail.product.cost));


            }
        };

        /**
         * 导出数据
         */
        btn_export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                File file = SwingFileUtils.getSelectedDirectory();
                if (file == null) return;

                try {
                    //ExportHelper.export(productDetail, file.getPath());
                    new Excel_ProductReport().exportProductDetail(productDetail, file.getPath());
                    JOptionPane.showMessageDialog(getRoot(), "导出成功");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(getRoot(), e1.getLocalizedMessage());
                }

            }
        });


        exportList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final File file = SwingFileUtils.getSelectedDirectory();
                if (file == null) return;


                try {
                    new Report_Excel_ProductMaterialList().report(productDetail, file.getAbsolutePath());
                    JOptionPane.showMessageDialog(getRoot(), "导出成功");
                    return;
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(getRoot(), e1.getMessage());
                } catch (HdException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(getRoot(), e1.getMessage());

                }

            }
        });


        btn_export_pic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (productDetail == null) return;
                if (productDetail.product == null)
                    return;

                if (StringUtils.isEmpty(productDetail.product.url)) {
                    JOptionPane.showMessageDialog(getRoot(), "图片不存在");
                    return;
                }


                final File file = SwingFileUtils.getSelectedDirectory();
                if (file == null) return;


                final String url = HttpUrl.loadProductPicture(productDetail.product.url);
                SwingWorker worker = new SwingWorker<String, Object>() {
                    @Override
                    protected String doInBackground() throws Exception {

                        Product product = productDetail.product;
                        String fileName = file.getAbsolutePath() + File.separator + product.name + (StringUtils.isEmpty(product.pVersion) ? "" : ("-" + product.pVersion)) + ".JPG";
                        DownloadFileManager.download(url, fileName);
                        return fileName;
                    }

                    @Override
                    protected void done() {

                        hideLoadingDialog();
                        String result = null;
                        try {
                            result = get();


                            JOptionPane.showMessageDialog(getRoot(), "导出图片成功，路径在：" + result);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                            JOptionPane.showMessageDialog(getRoot(), "导出图片失败，原因：" + e.getMessage());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(getRoot(), "导出图片失败，原因：" + e.getMessage());
                        }


                    }


                };
                worker.execute();
                showLoadingDialog();


            }
        });


        //咸康包装描述数据改变监听接口。
        jp_pack.setDataChangeListener(new DataChangeListener<Xiankang>() {
            @Override
            public void onDataChanged(Xiankang data) {

                productDetail.product.xiankang = data;
                packMaterialTableModel.updateProduct();


            }
        });

    }

    /**
     * 根据内外厂不同更新界面状态
     *
     * @param factory
     */
    private void updatePanelStateBaseOnFactory(Factory factory) {
        boolean isSelfProduct = factory.code.equals(Factory.CODE_LOCAl);
        //设置港杂费是否显示

        lb_gangza.setVisible(!isSelfProduct);
        tf_gangza.setVisible(!isSelfProduct);

        //实际成本是否可输入  外厂可输入
        tf_product_cost.setEnabled(!isSelfProduct);
        tf_product_cost.setEditable(!isSelfProduct);


        //包装内盒，长宽高是否可输入
        //内盒可以手动输入
//                    tf_inboxCount.setEditable(!isSelfProduct);
//                    tf_inboxCount.setEnabled(!isSelfProduct);
        tf_long.setEnabled(!isSelfProduct);
        tf_long.setEditable(!isSelfProduct);
        tf_width.setEnabled(!isSelfProduct);
        tf_width.setEditable(!isSelfProduct);
        tf_height.setEnabled(!isSelfProduct);
        tf_height.setEditable(!isSelfProduct);


        tab_list.setVisible(isSelfProduct);
        panel_xiankang.setVisible(isSelfProduct);


//
        getWindow(getRoot()).pack();
    }


    private void updatePackListenersBaseOnFactory(Factory factory) {
        boolean isSelfProduct = factory.code.equals(Factory.CODE_LOCAl);
        if (isSelfProduct) {

            tf_quantity.getDocument().addDocumentListener(tf_quantityListener);
            tf_quantity.getDocument().removeDocumentListener(tf_packSizeDocumentListener);
            tf_long.getDocument().removeDocumentListener(tf_packSizeDocumentListener);
            tf_width.getDocument().removeDocumentListener(tf_packSizeDocumentListener);
            tf_height.getDocument().removeDocumentListener(tf_packSizeDocumentListener);
            tf_product_cost.getDocument().removeDocumentListener(tf_foreign_relate_documentListener);

        } else {

            tf_quantity.getDocument().removeDocumentListener(tf_quantityListener);
            tf_quantity.getDocument().addDocumentListener(tf_packSizeDocumentListener);
            tf_long.getDocument().addDocumentListener(tf_packSizeDocumentListener);
            tf_width.getDocument().addDocumentListener(tf_packSizeDocumentListener);
            tf_height.getDocument().addDocumentListener(tf_packSizeDocumentListener);
            tf_product_cost.getDocument().addDocumentListener(tf_foreign_relate_documentListener);

        }
    }

    /**
     * 判断是否不能查看单价
     *
     * @return
     */
    private boolean cannotViewProductPrice() {
        return cannotViewPrice;
    }


    /**
     * 收集数据   将各当前数据 存入数据中
     *
     * @see
     */
    private void collectionData() {


        Product product = productDetail.product;

        //货号
        String productName = tf_product.getText().trim();


        product.setName(productName);

        //规格
        String ta_spcValue = ta_spec_inch.getText().trim();
        product.setSpec(ta_spcValue);


        //规格
        String ta_spc_cmValue = ta_spec_cm.getText().trim();
        product.setSpecCm(ta_spc_cmValue);

        //备注
        String ta_memoValue = ta_memo.getText().trim();
        product.setMemo(ta_memoValue);

        //产品分类
        PClass pClassData = (PClass) cb_class.getSelectedItem();
        if (pClassData != null) {
            product.setpClassId(pClassData.id);
            product.setpClassName(pClassData.name);
        }


        //厂家分类
        Factory pFactory = (Factory) cb_factory.getSelectedItem();
        if (pFactory != null) {
            product.factoryCode = pFactory.code;
            product.factoryName = pFactory.name;
        }


        //包装类型
        Pack pack = (Pack) cb_pack.getSelectedItem();
        product.pack = pack;


        //产品版本号
        String tf_versionValue = tf_version.getText().trim();
        product.setpVersion(tf_versionValue);

        //产品的材料构成  文字描述
        String tf_constituteValue = tf_constitute.getText().trim();
        product.setConstitute(tf_constituteValue);

        //产品单位

        String tf_unitValue = tf_unit.getText().trim();
        product.setpPUnitName(tf_unitValue);


        //镜面尺寸

        String mirrorSizeValue = jtf_mirror_size.getText();
        product.mirrorSize = mirrorSizeValue;


        //是否咸康数据

        boolean isXiangkang = cb_xiankang.isSelected();
        product.isXK = isXiangkang;

        product.xiankang = panel_xiankang.getData();


        //产品净重

        try {
            float tf_weightValue = Float.valueOf(tf_weight.getText().trim());
            product.setWeight(tf_weightValue);
        } catch (Throwable t) {
            // t.printStackTrace();
            product.setWeight(0);
        }


        date.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //日期
        String tf_dateValue = date.getJFormattedTextField().getText().toString();


        //日期格式验证
        product.setrDate(tf_dateValue);


        //附件


        product.attaches = StringUtils.combine(panel_attach.getAttachFiles());

        product.packInfo = ta_packinfo.getText();

        product.packAttaches = StringUtils.combine(pack_attaches.getAttachFiles());


        //产品油漆数据

        List<ProductPaint> paints = productPaintModel.getValuableDatas();
//        //数据检验
//        int size = paints.size();
//        for (int i = 0; i < size; i++) {
//
//            ProductPaint paint = paints.get(i);
//
//
//            int rowIndex = tb_product_paint.convertRowIndexToView(i);
//
//
//            if (StringUtils.isEmpty(paint.processName))
//                throw HdUIException.create(tb_product_paint, "第" + (rowIndex + 1) + "行数据有误，工序名称为必须");
//
//        }


        productDetail.paints = paints;


        List<ProductMaterial> conceptusMaterial = conceptusMaterialTableModel.getValuableDatas();
        //TODO  白胚材料的 数据检验


        productDetail.conceptusMaterials = conceptusMaterial;


        List<ProductMaterial> assembleMaterials = assembleMaterialTableModel.getValuableDatas();
        //TODO  组装材料的 数据检验


        productDetail.assembleMaterials = assembleMaterials;


        List<ProductWage> assembleWages = assembleWageTableModel.getValuableDatas();
        //TODO  组装工资的 数据检验


        productDetail.assembleWages = assembleWages;


        List<ProductWage> conceptusWages = conceptusWageTableModel.getValuableDatas();
        //TODO  白胚工资的 数据检验


        productDetail.conceptusWages = conceptusWages;


        /////////////包装

        List<ProductMaterial> packMaterials = packMaterialTableModel.getValuableDatas();
        //TODO  包装材料 数据检验


        productDetail.packMaterials = packMaterials;


        List<ProductWage> packWages = packWageTableModel.getValuableDatas();
        //TODO   包装工资  数据检验


        productDetail.packWages = packWages;


    }


    private void checkData(ProductDetail productDetail) throws HdUIException {

        //检验输入

        if (StringUtils.isEmpty(productDetail.product.name)) {
            throw HdUIException.create(tf_product, "请输入货号。。。");
        }

        if (productDetail.product.pack == null) {
            throw HdUIException.create(cb_pack, "请选择包装类型。。。");
        }

//        if(StringUtils.isEmpty()productDetail.product.classid==null)
//        {
//            throw HdUIException.create(cb_class, "请选择产品类型。。。");
//        }

    }

    /**
     * 更新界面方法  负责数据的绑定
     */
    private void bindData() {


        Product product = productDetail.product;

        bindProductBaseInfo(product);

        bindStatisticsValue(product);


        bindTableDatas(tb_assemble_cost, assembleMaterialTableModel, productDetail.assembleMaterials);
        bindTableDatas(tb_assemble_wage, assembleWageTableModel, productDetail.assembleWages);

        bindTableDatas(tb_conceptus_cost, conceptusMaterialTableModel, productDetail.conceptusMaterials);
        bindTableDatas(tb_conceptus_wage, conceptusWageTableModel, productDetail.conceptusWages);

        bindTableDatas(tb_product_paint, productPaintModel, productDetail.paints);


        bindTableDatas(tb_pack_cost, packMaterialTableModel, productDetail.packMaterials);
        bindTableDatas(tb_pack_wage, packWageTableModel, productDetail.packWages);

        jp_pack.productDetiail = productDetail;


    }


    /**
     * 重新表格绑定数据
     *
     * @param model
     * @param datas
     * @param <T>
     */
    private <T> void bindTableDatas(JTable table, BaseTableModel<T> model, List<T> datas) {
        //为了避免触发监听，先移除后添加
        model.removeTableModelListener(allTableModelListener);
        model.setDatas(datas);
        table.setModel(model);
        model.addTableModelListener(allTableModelListener);

    }


    /**
     * \
     * 绑定产品的基本信息   基本信息+包装成本
     *
     * @param product
     */
    private void bindProductBaseInfo(Product product) {


        if (product == null) return;

        tf_product.setText(product == null ? "" : product.getName());


        //产品 版本号 一旦保存就不能再修改
        tf_product.setEditable(product.id <= 0);
        //tf_version.setEditable(product.id <= 0);

        jtf_mirror_size.setText(product == null ? "" : product.mirrorSize);


        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon, String url) {
                photo.setIcon(icon);
            }

            @Override
            public void onError(String message) {
                photo.setText("");
            }
        }, HttpUrl.loadPicture(product.url), photo.getWidth(), photo.getHeight());

        photo.setText(product == null ? "产品图片" : "");


        bindProductSpecInchData(product == null ? "" : product.getSpec());
        bindProductSpecCmData(product == null ? "" : product.getSpecCm());

        ta_memo.setText(product == null ? "" : product.getMemo());
        date.getJFormattedTextField().setText(product == null ? "" : product.getrDate());


        tf_unit.getDocument().removeDocumentListener(tf_unitDocumentListener);
        tf_unit.setText(product == null ? "S/1" : product.pUnitName);
        tf_unit.getDocument().addDocumentListener(tf_unitDocumentListener);

        tf_weight.setText(product.getWeight() > 0.0f ? String.valueOf(product.getWeight()) : "");

        tf_constitute.setText(product == null ? "" : product.getConstitute());
        tf_version.setText(product == null ? "" : product.getpVersion());

        //实际成本
        tf_product_cost.setText(product == null ? "" : String.valueOf(product.productCost));


        //港杂费用
        tf_gangza.setText(String.valueOf(product == null ? 0 : product.gangza));

        //设置产品分类
        int selectClassIndex = -1;
        if (product != null)
            for (int i = 0, count = cb_class.getItemCount(); i < count; i++) {

                if (cb_class.getItemAt(i).id == product.pClassId) {

                    selectClassIndex = i;
                    break;
                }

            }
        cb_class.setSelectedIndex(selectClassIndex);


        int selectedFactoryIndex = -1;
        Factory selectedFactory = null;
        if (product != null)
            for (int i = 0, count = cb_factory.getItemCount(); i < count; i++) {

                if (cb_factory.getItemAt(i).code.equals(product.factoryCode)) {
                    selectedFactory = cb_factory.getItemAt(i);
                    selectedFactoryIndex = i;
                    break;
                }

            }
        if (selectedFactoryIndex > -1) {
            cb_factory.setSelectedIndex(selectedFactoryIndex);
            updatePanelStateBaseOnFactory(selectedFactory);

        }


        //设置包装信息
        cb_pack.removeItemListener(cb_packItemListener);
        if (product.pack != null)
            cb_pack.setSelectedItem(product.pack);
        else
            cb_pack.setSelectedIndex(0);
        cb_pack.addItemListener(cb_packItemListener);


        boolean isXiangkang = product != null && product.isXK;


        cb_xiankang.setSelected(isXiangkang);
        panel_xiankang.setVisible(isXiangkang);
        jp_pack.setVisible(isXiangkang);
        panel_xiankang.setData(product.xiankang);
        jp_pack.setData(product.xiankang);


        tf_fob.setText(cannotViewProductPrice() ? "***" : product == null ? "" : String.valueOf(product.fob));
        tf_cost.setText(cannotViewProductPrice() ? "***" : product == null ? "" : String.valueOf(product.cost));
        tf_price.setText(cannotViewProductPrice() ? "***" : product == null ? "" : String.valueOf(product.price));


        String[] fileNames = StringUtils.isEmpty(product.attaches) ? new String[]{} : product.attaches.split(";");
        panel_attach.setAttachFiles(fileNames);


        //包装外信息
        ta_packinfo.setText(product.packInfo);
        fileNames = StringUtils.isEmpty(product.packAttaches) ? new String[]{} : product.packAttaches.split(";");
        pack_attaches.setAttachFiles(fileNames);


    }


    /**
     * 数据初始化 产品详界面
     *
     * @param detail
     */

    private void initPanel(ProductDetail detail) {

        initPanel(detail, null);
    }

    private void initPanel(ProductDetail detail, final ProductDelete productDelete) {

        removeListeners();

        workFlow.setVisible(detail == null || detail.product == null || detail.product.id <= 0 ? false : true);

        if (detail == null) {

            productDetail = new ProductDetail();

            oldData = (ProductDetail) ObjectUtils.deepCopy(productDetail);

        } else {
            oldData = (ProductDetail) ObjectUtils.deepCopy(detail);
            productDetail = detail;

        }
        setProductInfoToPackageModel();
        bindData();


        bindLogData(detail == null ? null : detail.productLog);


        //非删除数据 添加对数据的监听。
        if (productDelete == null)
            addListeners();


        panel_nomal.setVisible(null == productDelete);
        panel_delete.setVisible(productDelete != null);

        for (ActionListener listener : btn_resume.getActionListeners()) {
            btn_resume.removeActionListener(listener);
        }
        if (productDelete != null)
            btn_resume.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    new HdSwingWorker<ProductDetail, Void>(SwingUtilities.getWindowAncestor(Panel_ProductDetail.this.getRoot())) {
                        @Override
                        protected RemoteData<ProductDetail> doInBackground() throws Exception {
                            return apiManager.resumeDeleteProduct(productDelete.id);
                        }

                        @Override
                        public void onResult(RemoteData<ProductDetail> data) {


                            if (data.isSuccess()) {
                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()), "数据恢复成功");
                                if (listener != null)
                                    listener.close();
                            } else {

                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getRoot()), data.message);
                            }


                        }
                    }.go();


                }
            });


        pack_attaches.setTitle("包装附件");
        panel_attach.setTitle("产品附件");
        workFlow.setVisible(false);


//        workFlow.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//
//                presenter.showProductWorkFlow();
//            }
//        });
    }


    /**
     * 绑定修改记录信息
     *
     * @param productLog
     */
    private void bindLogData(ProductLog productLog) {


        jp_log.setVisible(productLog != null);


        if (productLog == null) {
            return;
        }


        creator.setText(productLog.creatorCName);
        createTime.setText(productLog.createTimeString);
        updateTime.setText(productLog.updateTimeString);
        updator.setText(productLog.updatorCName);


    }


    private void createUIComponents() {

        // cellPanel = new APanel();
        //cellPanel.setGridColor(Color.GRAY);
        // cellPanel.setPaintInBackground(false);
        cb_class = new JComboBox<PClass>();
        for (PClass pClass : CacheManager.getInstance().bufferData.pClasses) {
            cb_class.addItem(pClass);
        }

        cb_pack = new JComboBox<Pack>();
        for (Pack pack : CacheManager.getInstance().bufferData.packs) {
            cb_pack.addItem(pack);
        }


        cb_factory = new JComboBox<Factory>();
        for (Factory factory : CacheManager.getInstance().bufferData.factories) {
            cb_factory.addItem(factory);
        }
        JDatePanelImpl picker = new JDatePanelImpl(null);
        date = new JDatePickerImpl(picker, new HdDateComponentFormatter());


    }


    /**
     * 初始化控件装配。
     */
    public void initComponent() {


        panel_delete.setVisible(false);
        panel_nomal.setVisible(false);


        //附件垂直显示

        panel_attach.setLayout(new GridLayout(0, 2, 10, 10));
        pack_attaches.setLayout(new GridLayout(0, 2, 5, 5));

        tf_product.setToolTipText("以【" + ConstantData.DEMO_PRODUCT_NAME + "】开头的货号，将会默认选为套版使用。");


        //咸康信息 默认不显示
        panel_xiankang.setVisible(false);

        /**
         * 表格弹出菜单回调接口
         */
        tableMenuLister = new TablePopMenu.TableMenuLister() {
            @Override
            public void onTableMenuClick(int index, BaseTableModel tableModel, int rowIndex[]) {

                if (index < 0 || rowIndex == null || rowIndex.length == 0) return;


                switch (index) {


                    case TablePopMenu.ITEM_INSERT:

                        tableModel.addNewRow(rowIndex[0]);

                        break;
                    case TablePopMenu.ITEM_DELETE:

                        tableModel.deleteRows(rowIndex);
                        break;
                    case TablePopMenu.ITEM_PAST:


                        try {
                            tableModel.insertNewRows(TableDuplicateHelper.getBufferData(), rowIndex.length == 0 ? 0 : rowIndex[0]);
                            TableDuplicateHelper.clear();
                        } catch (Throwable t) {
                            t.printStackTrace();
                            JOptionPane.showMessageDialog(getRoot(), "数据格式不对， 不能黏贴。");
                        }


                        // handleCopyTableData(tableModel, rowIndex[0]);
                        break;

                    case TablePopMenu.ITEM_COPY:

                        ObjectUtils.deepCopy(tableModel.getValuableDatas());
                        TableDuplicateHelper.saveBufferData((List<Object>) ObjectUtils.deepCopy(tableModel.getValuableDatas()));
//                        JOptionPane.showMessageDialog(getRoot(),"成功");
                        // handleClipBordDataToTable(tableModel, rowIndex[0]);
                        break;
                }
            }
        };


        TableMouseAdapter adapter = new TableMouseAdapter(tableMenuLister);
        //设置表格点击监听
        tb_conceptus_cost.addMouseListener(adapter);
        tb_conceptus_wage.addMouseListener(adapter);

        tb_assemble_cost.addMouseListener(adapter);
        tb_assemble_wage.addMouseListener(adapter);
        tb_product_paint.addMouseListener(adapter);
        tb_pack_cost.addMouseListener(adapter);
        tb_pack_wage.addMouseListener(adapter);


        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getSource() instanceof JTable) {

                    JTable table = (JTable) e.getSource();

                    //黏贴
                    if (e.isControlDown()) {

                        if (e.getKeyCode() == KeyEvent.VK_V) {
                            handleClipBordDataToTable((BaseTableModel) table.getModel(), table.convertRowIndexToModel(table.getSelectedRow()));
                        }

                    }
                }

            }
        };


        tb_conceptus_cost.addKeyListener(keyListener);
        tb_assemble_cost.addKeyListener(keyListener);
        tb_pack_cost.addKeyListener(keyListener);
        tb_product_paint.addKeyListener(keyListener);


        /**
         * 保存功能
         */
        final ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //非新增数据
                    if (!isModified()) {

                        if (productDetail.product.id > 0) {
                            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()), "数据无改变！");
                            return;
                        }
                    }


                    checkData(productDetail);

                } catch (HdUIException exception) {
                    JOptionPane.showMessageDialog(exception.component, exception.message);
                    exception.component.requestFocus();
                }
                presenter.save(productDetail);


            }
        };
        bn_save.addActionListener(l);
        save2.addActionListener(l);


        //添加附件
        btn_attach_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final File file = FileChooserHelper.chooseFile(JFileChooser.FILES_ONLY, false, new PictureFileFilter());

                if (file != null) {
                    //上传文件
                    new HdSwingWorker<String, String>(getWindow(getRoot())) {

                        @Override
                        protected RemoteData<String> doInBackground() throws Exception {


                            return apiManager.uploadTempPicture(file);


                        }

                        @Override
                        public void onResult(RemoteData<String> data) {


                            if (data.isSuccess()) {
                                panel_attach.addUrl(data.datas.get(0));


                            } else {

                            }


                        }
                    }.go();
                }


            }
        });


        btn_copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (productDetail.product.id <= 0) {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "产品数据未建立，请先保存");
                    return;
                }


                if (isModified()) {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "产品数据有改动，请先保存");
                    return;
                }


                //显示对话框


                CopyProductDialog dialog = new CopyProductDialog(SwingUtilities.getWindowAncestor(getRoot()), productDetail.product);
                dialog.pack();
                dialog.setMinimumSize(new Dimension(400, 300));
                dialog.setLocationByPlatform(true);
                dialog.setModal(true);
                dialog.setVisible(true);
                if (dialog.getResult() != null)
                    HdSwingUtils.showDetailPanel(getWindow(getRoot()), dialog.getResult());


            }
        });


        btn_add_pack_image.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final File[] files = FileChooserHelper.chooseFiles(JFileChooser.FILES_ONLY, false, new PictureFileFilter());


                if (files != null) {
                    presenter.addPackagePicture(files);
                }

            }
        });


        /**
         * 删除
         */
        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (listener != null) listener.delete();


            }
        });


        /**
         *
         */
        lb_qr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                if (e.getClickCount() == 2) {

                    if (productDetail.product != null) {
                        ProductQRDialog dialog = new ProductQRDialog(getWindow(getRoot()), productDetail.product);
                        dialog.setVisible(true);
                    }

                }
            }
        });


        photo.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {


                    if (productDetail.product != null) {
                        ImageViewDialog.showProductDialog(getWindow(getRoot()), tf_product.getText().trim(), tf_version.getText().trim(), productDetail.product.url);
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
                if (productDetail != null && productDetail.product != null && productDetail.product.id > 0) {
                    if (e.isPopupTrigger()) {

                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem jMenuItem = new JMenuItem("修复缩略图");
                        menu.add(jMenuItem);
                        menu.show(e.getComponent(), e.getX(), e.getY());
                        jMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                presenter.correctThumbnail(productDetail.product.id);
                            }
                        });

                    }
                }
            }
        });


        cb_xiankang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                panel_xiankang.setVisible(cb_xiankang.isSelected());
                jp_pack.setVisible(cb_xiankang.isSelected());
            }
        });


        //配置表格的自定义编辑输入   //材料
        configTableCellMaterialEditor(tb_conceptus_cost);
        configTableCellMaterialEditor(tb_assemble_cost);
        configTableCellMaterialEditor(tb_product_paint);
        configTableCellMaterialEditor(tb_pack_cost);

        configTableCellMaterialEditor(tb_pack_wage);
        configTableCellMaterialEditor(tb_conceptus_wage);
        configTableCellMaterialEditor(tb_assemble_wage);


        //配置权限  是否修改  是否可以删除

        boolean modifiable = AuthorityUtil.getInstance().editProduct() || AuthorityUtil.getInstance().addProduct();

        bn_save.setVisible(modifiable);
        save2.setVisible(modifiable);


        btn_attach_add.setVisible(modifiable);

        btn_copy.setVisible(AuthorityUtil.getInstance().editProduct());


        btn_delete.setVisible(AuthorityUtil.getInstance().deleteProduct());


        btn_export.setVisible(AuthorityUtil.getInstance().exportProduct());

        btn_export_pic.setVisible(AuthorityUtil.getInstance().exportProduct());

        btn_add_pack_image.setVisible(modifiable);


        viewLog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {


                    Window window = getWindow(Panel_ProductDetail.this.getRoot());
                    OperationLogDialog dialog = new OperationLogDialog(window, Product.class, productDetail.product.id);
                    dialog.setLocationRelativeTo(window);
                    dialog.setVisible(true);


                }
            }
        });


        //配置表格的自定义编辑输入   //工序


        initListeners();
    }


    /**
     * 处理复制黏贴数据
     *
     * @param tableModel
     * @param rowIndex
     */
    private void handleClipBordDataToTable(final BaseTableModel tableModel, final int rowIndex) {


        String clipboardText = ClipBordHelper.getSysClipboardText();


        String[] rows = clipboardText.split("[\n]+");

        final List<String> codes = new ArrayList<String>();
        final List<String> names = new ArrayList<>();
        int length = rows.length;
        final String[][] tableData = new String[length][];
        for (int i = 0; i < length; i++) {
            tableData[i] = rows[i].split("\t");


            //油漆表
            if (tableModel instanceof ProductPaintTableModel) {

                //复制数据油漆名称在第四例
                if (tableData[i].length > 3 && !StringUtils.isEmpty(tableData[i][3])) {
                    names.add(tableData[i][3]);
                }

            } else {
                if (tableData[i].length > 0 && !StringUtils.isEmpty(tableData[i][0])) {

                    codes.add(tableData[i][0]);

                }
            }


        }


        new HdSwingWorker<Material, Object>(getWindow(getRoot())) {


            @Override
            public void onResult(RemoteData<Material> data) {


                int firstRow = rowIndex;
                for (String[] row : tableData) {


                    Object object = tableModel.addNewRow(firstRow++);
                    if (object instanceof ProductMaterial) {


                        String code = row == null || row.length == 0 ? "" : row[0].trim();
                        if (StringUtils.isEmpty(code))
                            continue;
                        ProductMaterial productMaterial = (ProductMaterial) object;


                        for (Material material : data.datas) {


                            if (code.equals(material.code)) {
                                float quantity = 0;
                                try {
                                    quantity = FloatHelper.scale(Float.valueOf(row[2]), 4);
                                } catch (Throwable t) {

                                }
                                productMaterial.setQuantity(quantity);

                                float pLong = 0;
                                try {
                                    pLong = FloatHelper.scale(Float.valueOf(row[3]));
                                } catch (Throwable t) {

                                }
                                productMaterial.setpLong(pLong);

                                float pWidth = 0;
                                try {
                                    pWidth = FloatHelper.scale(Float.valueOf(row[4]));
                                } catch (Throwable t) {

                                }
                                productMaterial.setpWidth(pWidth);


                                float pHeight = 0;
                                try {
                                    pHeight = FloatHelper.scale(Float.valueOf(row[5]));
                                } catch (Throwable t) {

                                }
                                productMaterial.setpHeight(pHeight);


                                ProductAnalytics.setMaterialToProductPaint(productMaterial, material);
                                //tableModel.fireTableDataChanged();

                                break;
                            }

                        }


                    } else if (object instanceof ProductPaint) {


                        ProductPaint productPaint = (ProductPaint) object;
                        String name = row == null || row.length <= 3 ? "" : row[3].trim();


                        String processCode = "";
                        try {
                            processCode = row[0];
                        } catch (Throwable t) {

                        }
                        productPaint.setProcessCode(processCode);


                        String processName = "";
                        try {
                            processName = row[1];
                        } catch (Throwable t) {

                        }
                        productPaint.setProcessName(processName);


                        float processPrice = 0;
                        try {
                            processPrice = FloatHelper.scale(Float.valueOf(row[2]));
                        } catch (Throwable t) {

                        }
                        productPaint.setProcessPrice(processPrice);

                        float ingredientRatio = 0;
                        try {
                            ingredientRatio = FloatHelper.scale(Float.valueOf(row[4]));
                        } catch (Throwable t) {

                        }
                        productPaint.ingredientRatio = ingredientRatio;
                        ;


                        float materialQuantity = 0;
                        try {
                            materialQuantity = FloatHelper.scale(Float.valueOf(row[6]));
                        } catch (Throwable t) {

                        }
                        productPaint.quantity = materialQuantity;

                        //查找匹配材料   油漆表中 材料不是必须
                        for (Material material : data.datas) {

                            if (name.equals(material.name)) {

                                ProductAnalytics.setMaterialToProductPaint(productPaint, material, globalData);


                                break;
                            }

                        }

                        ProductAnalytics.updateProductPaintPriceAndCostAndQuantity(productPaint, globalData);


                    }


                }
                tableModel.fireTableDataChanged();

            }

            @Override
            protected RemoteData<Material> doInBackground() throws Exception {


                if (codes.size() > 0) {
                    return apiManager.readMaterialListByCodeEquals(codes);

                } else
                    return apiManager.readMaterialListByNameEquals(names);


            }
        }.go();


    }


    /**
     * 绑定汇总数据
     */
    private void bindStatisticsValue(Product product) {
        if (product == null) return;
        jtf_paint_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.paintCost));
        jtf_paint_wage.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.paintWage));
        jtf_assemble_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.assembleCost));
        jtf_assemble_wage.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.assembleWage));

        jtf_conceptus_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.conceptusCost));
        jtf_conceptus_wage.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.conceptusWage));

        jtf_pack_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.packCost));
        jtf_pack_wage.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.packWage));

        jtf_cost1.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.cost1));
        jtf_cost6.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.cost6));

        jtf_cost7.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.cost7));
        jtf_cost8.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.cost8));
        jtf_cost11_15.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.cost11_15));

        jtf_cost_repair.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.repairCost));
        jtf_cost_banyungongzi.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.banyunCost));

        tf_product_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.productCost));
        tf_cost4.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.cost4));
        tf_fob.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.fob));
        tf_price.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.price));
        tf_cost.setText(cannotViewProductPrice() ? "***" : String.valueOf(product.cost));
        tf_volume.setText(String.valueOf(product.packVolume));
        tf_long.setText(String.valueOf(product.packLong));
        tf_width.setText(String.valueOf(product.packWidth));
        tf_height.setText(String.valueOf(product.packHeight));
        tf_inboxCount.setText(String.valueOf(product.insideBoxQuantity));


        //to avoid fire listener  first check value changed or not  and remove listener and rebind.
        if (!tf_quantity.getText().trim().equals(String.valueOf(product.packQuantity))) {
            clearPackQuantityListener();
            tf_quantity.setText(String.valueOf(product.packQuantity));
            if (Config.DEBUG) {
                Config.log("product.packQuantity:" + product.packQuantity);
            }
            resetPackQuantityListener();
        }

//        if (!tf_inboxCount.getText().trim().equals(String.valueOf(product.insideBoxQuantity))) {insideBoxQuantity
        tf_inboxCount.getDocument().removeDocumentListener(tf_inboxCountListener);
        tf_inboxCount.setText(String.valueOf(product.insideBoxQuantity));
        tf_inboxCount.getDocument().addDocumentListener(tf_inboxCountListener);


    }

    private void clearPackQuantityListener() {
        tf_quantity.getDocument().removeDocumentListener(tf_quantityListener);
        tf_quantity.getDocument().removeDocumentListener(tf_packSizeDocumentListener);

    }

    private void resetPackQuantityListener() {
        Factory factory = (Factory) cb_factory.getSelectedItem();
        boolean isSelfProduct = factory.code.equals(Factory.CODE_LOCAl);

        if (isSelfProduct)
            tf_quantity.getDocument().addDocumentListener(tf_quantityListener);
        else
            tf_quantity.getDocument().addDocumentListener(tf_packSizeDocumentListener);

    }

    /**
     * 绑定英寸规格数据
     *
     * @param inchString
     */
    private void bindProductSpecInchData(String inchString) {


        ta_spec_inch.getDocument().removeDocumentListener(ta_spec_inchListener);
        ta_spec_inch.setText(inchString);
        ta_spec_inch.getDocument().addDocumentListener(ta_spec_inchListener);

    }


    /**
     * 绑定厘米规格数据
     *
     * @param cmString
     */
    private void bindProductSpecCmData(String cmString) {


        ta_spec_cm.getDocument().removeDocumentListener(ta_spec_cmListener);
        ta_spec_cm.setText(cmString);
        ta_spec_cm.getDocument().addDocumentListener(ta_spec_cmListener);

    }

    /**
     * 配置表格的 弹出选择框  材料选择
     */
    private void configTableCellMaterialEditor(final JTable table) {


        //定制表格的编辑功能 弹出物料选择单

        final JTextField jtf = new JTextField();
        jtf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf.hasFocus())
                    jtf.requestFocus();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!jtf.hasFocus())
                    jtf.requestFocus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


            }
        });


        jtf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                final String text = ((JTextField) e.getSource()).getText().trim();

                handleTableMaterialInput(table, text);

            }
        });


        DefaultCellEditor editor = new DefaultCellEditor(jtf);
        table.setDefaultEditor(Material.class, editor);


        final JTextField processjtf = new JTextField();
        processjtf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!processjtf.hasFocus())
                    processjtf.requestFocus();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document object = e.getDocument();
                if (!processjtf.hasFocus())
                    processjtf.requestFocus();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {


            }
        });


        processjtf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                final String text = ((JTextField) e.getSource()).getText().trim();
                handleTableProcessInput(table, text);

            }
        });


        table.setDefaultEditor(ProductProcess.class, new DefaultCellEditor(processjtf));


        JComboBox<PackMaterialType> packMaterialTypeComboBox = new JComboBox<>();
        for (PackMaterialType type : CacheManager.getInstance().bufferData.packMaterialTypes)
            packMaterialTypeComboBox.addItem(type);
        DefaultCellEditor comboboxEditor = new DefaultCellEditor(packMaterialTypeComboBox);

        table.setDefaultEditor(PackMaterialType.class, comboboxEditor);


        JComboBox<PackMaterialPosition> packMaterialPositionComboBox = new JComboBox<>();
        for (PackMaterialPosition position : CacheManager.getInstance().bufferData.packMaterialPositions)
            packMaterialPositionComboBox.addItem(position);
        table.setDefaultEditor(PackMaterialPosition.class, new DefaultCellEditor(packMaterialPositionComboBox));


        JComboBox<PackMaterialClass> packMaterialClassComboBox = new JComboBox<>();
        for (PackMaterialClass packMaterialClass : CacheManager.getInstance().bufferData.packMaterialClasses)
            packMaterialClassComboBox.addItem(packMaterialClass);
        table.setDefaultEditor(PackMaterialClass.class, new DefaultCellEditor(packMaterialClassComboBox));


        //让浮点数去除尾吧0
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private DecimalFormat format = new DecimalFormat("#.#####");

            @Override
            protected void setValue(Object value) {
                if (value instanceof Float) {

                    super.setValue(format.format(((Float) value).floatValue()));
                    // super.setValue( FloatHelper.scale(((Float) value).floatValue(), 10));
                } else
                    super.setValue(value);
            }
        };

        table.setDefaultRenderer(Object.class, renderer);


    }

    /**
     * 配置表格的 弹出选择框  工序选择
     */
    private void configTableCellProcessEditor(final JTable table) {


        //定制表格的编辑功能 弹出物料选择单


    }

    /**
     * 处理表格的 材料输入时间
     *
     * @param table
     * @param text
     * @return
     */


    private void handleTableMaterialInput(final JTable table, final String text) {

        final int rowIndex = table.convertRowIndexToModel(table.getSelectedRow());
        final Materialable materialable;
        if (table.getModel() instanceof Materialable) {

            materialable = ((Materialable) table.getModel());
        } else {
            return;
        }
        //查询  单记录直接copy
        new HdSwingWorker<Material, Object>(SwingUtilities.getWindowAncestor(getRoot())) {
            @Override
            protected RemoteData<Material> doInBackground() throws Exception {


                return apiManager.loadMaterialByCodeOrName(text, 0, 100);

            }

            @Override
            public void onResult(RemoteData<Material> data) {

                //只有一条记录 ，并且该材料未被停用
                if (data.isSuccess() && data.totalCount == 1 && !data.datas.get(0).outOfService) {

                    materialable.setMaterial(data.datas.get(0), rowIndex);


                } else {


                    SearchDialog<Material> dialog = new SearchDialog.Builder().setWindow(getWindow(contentPane)).setTableModel(new MaterialTableModel()).setComonSearch(new ComonSearch<Material>() {
                        @Override
                        public RemoteData<Material> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.loadMaterialByCodeOrName(value, pageIndex, pageCount);
                        }
                    }).setResultChecker(new SearchDialog.ResultChecker<Material>() {
                        @Override
                        public boolean isValid(Material result) {


                            if (result.outOfService) {
//
                                return false;

                            } else
                                return true;
                        }

                        @Override
                        public void warn(Material material) {
                            JOptionPane.showMessageDialog(getWindow(contentPane), "材料：【" + material.code + "】  已经被停用，请选择其他材料");
                        }
                    }).setValue(text).setRemoteData(data).createSearchDialog();
                    dialog.setMinimumSize(new Dimension(800, 600));
                    dialog.pack();
                    dialog.setLocationRelativeTo(table);
                    dialog.setVisible(true);
                    Material material = dialog.getResult();
                    if (material != null) {
                        if (table.getModel() instanceof Materialable) {


                            ((Materialable) table.getModel()).setMaterial(material, rowIndex);
                        }

                    }


                }


            }
        }.go();


    }


    /**
     * 处理工序输入检索
     *
     * @param table
     * @param text
     */
    private void handleTableProcessInput(final JTable table, final String text) {


        final int rowIndex = table.convertRowIndexToModel(table.getSelectedRow());
        final Processable processable;
        if (table.getModel() instanceof Processable) {

            processable = ((Processable) table.getModel());
        } else {
            return;
        }
        //查询  单记录直接copy
        new HdSwingWorker<ProductProcess, Object>(SwingUtilities.getWindowAncestor(getRoot())) {
            @Override
            protected RemoteData<ProductProcess> doInBackground() throws Exception {


                return apiManager.loadProcessByCodeOrName(text, 0, 100);

            }

            @Override
            public void onResult(RemoteData<ProductProcess> data) {

                if (data.isSuccess() && data.totalCount == 1) {

                    processable.setProcess(data.datas.get(0), rowIndex);


                } else {


                    SearchDialog<ProductProcess> dialog = new SearchDialog.Builder().setWindow(getWindow(contentPane)).setTableModel(new ProductProcessModel(false)).setComonSearch(new ComonSearch<ProductProcess>() {
                        @Override
                        public RemoteData<ProductProcess> search(String value, int pageIndex, int pageCount) throws HdException {
                            return apiManager.loadProcessByCodeOrName(value, pageIndex, pageCount);
                        }
                    }).setValue(text).setRemoteData(data).createSearchDialog();
                    dialog.setMinimumSize(new Dimension(800, 600));
                    dialog.pack();
                    dialog.setLocationRelativeTo(table);
                    dialog.setVisible(true);
                    ProductProcess process = dialog.getResult();
                    if (process != null) {
                        if (table.getModel() instanceof Processable) {


                            ((Processable) table.getModel()).setProcess(process, rowIndex);
                        }

                    }

                }


            }
        }.go();


    }


    @Override
    public JComponent getRoot() {
        return contentPane;
    }

    public ProductDetail getData() {
        return productDetail;
    }


    /**
     * 预留方法
     *
     * @param data
     */
    public void getData(ProductDetail data) {
    }

    /**
     * @return
     */
    public boolean isModified() {


        collectionData();


        return !productDetail.equals(oldData);

    }


    public void setData(Xiankang data) {
    }

    public void getData(Xiankang data) {
    }

    public boolean isModified(Xiankang data) {
        return false;
    }

    @Override
    public void showPackAttachFiles(List<String> attachStrings) {

        String[] urls = new String[attachStrings.size()];
        attachStrings.toArray(urls);
        pack_attaches.setAttachFiles(urls);

    }
}
