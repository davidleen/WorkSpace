package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import java.util.List;

/**
 * 客户路表格数据模型
 */
public class CustomerModel extends BaseTableModel<Customer> {

    public static String[] columnNames = new String[]{"编码 ", "名称", "电话", "传真", "邮箱", "地址", "国家"};
    public static int[] columnWidth = new int[]{80, 100, 100, 100, 140, 200, ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{"code", "name", "tel", "fax", "email", "addr", "nation"};

    public static Class[] classes = new Class[]{Object.class, Object.class, Object.class};


    @Inject
    public CustomerModel() {
        super(columnNames, fieldName, classes, Customer.class);
        setRowAdjustable(true);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return true;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        Customer customer = getItem(rowIndex);
        String stringValue = String.valueOf(aValue);
        switch (columnIndex) {
            case 0:
                customer.code = stringValue;
                break;
            case 1:
                customer.name = stringValue;
                break;
            case 2:
                customer.tel = stringValue;

                break;
            case 3:
                customer.fax = stringValue;
                break;
            case 4:
                customer.email = stringValue;
                break;
            case 5:
                customer.addr = stringValue;
                break;
            case 6:
                customer.nation = stringValue;
                break;

        }
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }


    @Override
    public void setDatas(List<Customer> newDatas) {

        MiniRowCount = newDatas.size() + 20;
        super.setDatas(newDatas);
    }
}
