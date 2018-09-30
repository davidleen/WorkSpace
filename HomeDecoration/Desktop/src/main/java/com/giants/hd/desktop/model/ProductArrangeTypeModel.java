package com.giants.hd.desktop.model;

import com.giants3.hd.entity.WorkFlowSubType;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 产品排厂类型
 * 二级流程 铁件 木件  PU，其他
 */
public class ProductArrangeTypeModel extends BaseTableModel<WorkFlowSubType> {

    public static String[] columnNames = new String[]{"类型ID","产品排厂类型"};
    public static int[] columnWidth = new int[]{100,200};

    public static String[] fieldName = new String[]{"typeId","typeName"};

    public static Class[] classes = new Class[]{Object.class,Object.class};


    @Inject
    public ProductArrangeTypeModel() {
        super(columnNames, fieldName, classes, WorkFlowSubType.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

//        if (columnIndex == 0) return false;

        return true;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        WorkFlowSubType authority = getItem(rowIndex);


        switch (columnIndex) {
            case 0:
                try {
                    final Integer integer = Integer.valueOf(aValue.toString());
                    authority.typeId = integer;
                } catch (Throwable t) {
                }
                break;
            case 1:

                authority.typeName = String.valueOf(aValue);

                break;


        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }
}
