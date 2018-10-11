package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;
import java.util.List;

/**
 * 客户路表格数据模型
 */
public class CustomerModel extends BaseTableModel<Customer> {

    public static String[] columnNames = new String[]{"图片","编码 ", "名称","公司", "电话", "传真", "邮箱", "地址", "国家"};
    public static int[] columnWidth = new int[]{120,80, 100, 120,100, 100, 140, 200, ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{"nameCardFileUrl","code", "name", "company","tel", "fax", "email", "addr", "nation"};

    public static Class[] classes = new Class[]{ImageIcon.class,Object.class, Object.class, Object.class};


    @Inject
    public CustomerModel() {
        super(columnNames, fieldName, classes, Customer.class);

    }








    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }



}
