package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.ProductTemplateModel;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ProductDetail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 产品套版选择对话框
 */
public class ProductTemplateDialog extends BaseListDialog<ProductDetail> {
    private JPanel contentPane;
    private JHdTable jt;

    private ProductTemplateModel model;
    public ProductTemplateDialog(Window window) {
        super(window);

        model=new ProductTemplateModel();
    }

    @Override
    protected java.util.List<ProductDetail> readData() throws HdException {
        return CacheManager.getInstance().bufferData.demos;
    }

    @Override
    protected BaseTableModel<ProductDetail> getTableModel() {
        return model;
    }

    @Override
    protected void init() {
        setContentPane(contentPane);
        setTitle("产品套版选择");

        jt.setModel(model);
        setMinimumSize(new Dimension(300, 400));
        jt.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.getClickCount()==2)
                {
                 int[] row=   JTableUtils.getSelectedRowSOnModel(jt);
                 ProductDetail detail=model.getItem(row[0]);
                    setResult(detail);
                    dispose();
                }
            }
        });
    }


}
