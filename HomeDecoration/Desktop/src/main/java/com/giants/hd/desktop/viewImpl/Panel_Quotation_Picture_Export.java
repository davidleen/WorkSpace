package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.BaseListTableModel;
import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.model.WorkFlowArrangeTableModel;
import com.giants.hd.desktop.mvp.presenter.QuotationPictureExportPresenter;
import com.giants.hd.desktop.mvp.viewer.QuotationPictureExportViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

/**
 * 流程配置。
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_Quotation_Picture_Export extends BasePanel implements QuotationPictureExportViewer {


    List<TableField> tableFields;

    List<WorkFlowArrangeTableModel.WorkFlowConfig> data;


    BaseListTableModel<Product> tableModel;
    private QuotationPictureExportPresenter presenter;


    public Panel_Quotation_Picture_Export(final QuotationPictureExportPresenter presenter) {
        this.presenter = presenter;
        this.tableFields = TableStructureUtils.getQuotationPictureModel();
        tableModel = new BaseListTableModel<Product>(Product.class, tableFields);
        table.setModel(tableModel);


        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSearch();


            }
        });

        tf_key.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSearch();
            }
        });
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                File file = SwingFileUtils.getSelectedDirectory();
                if (file == null) return;

                presenter.exportPicture(file);
            }
        });


        table.addMouseListener(new MouseAdapter() {

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
                    final JTable source = (JTable) e.getSource();
                    JPopupMenu jPopupMenu=new JPopupMenu();
                    final JMenuItem delete = new JMenuItem("移除");
                    jPopupMenu.add(delete);
                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                           int[] rows= JTableUtils.getSelectedRowSOnModel(source);
                            if(rows!=null&&rows.length>0)
                            presenter.removeRows(rows);



                        }
                    });

                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        });


    }

    private JPanel root;
    private JHdTable table;

    private JTextField tf_key;
    private JButton search;
    private JButton export;
    private JCheckBox includeCopy;


    private void startSearch()
    {
        String key = tf_key.getText().toString().trim();
        if (StringUtils.isEmpty(key)) {
            showMesssage("请输入货号");
            return;
        }

        presenter.searchProduct(key,includeCopy.isSelected());
    }
    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return root;
    }


    @Override
    public void showProducts(List<Product> products) {


        tableModel.setDatas(products);
    }
}
