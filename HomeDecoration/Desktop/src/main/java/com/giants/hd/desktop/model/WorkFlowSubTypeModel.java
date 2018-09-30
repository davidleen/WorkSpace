package com.giants.hd.desktop.model;

import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowSubType;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 生产进度数据模型
 */
public class WorkFlowSubTypeModel extends BaseTableModel<WorkFlowSubType> {

    public static String[] columnNames = new String[]{"类型编号", "类型名称"};
    public static int[] columnWidth = new int[]{80, 100};


    public static String[] fieldName = new String[]{"typeId", "typeName"};

    public static Class[] classes = new Class[]{Object.class, Object.class, User.class, User.class, User.class, User.class, User.class, User.class};


    @Inject
    public WorkFlowSubTypeModel() {
        super(columnNames, fieldName, classes, WorkFlowSubType.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
//        if (StringUtils.index(fieldName, USER_CODE) == columnIndex || StringUtils.index(fieldName, USER_NAME) == columnIndex || StringUtils.index(fieldName, USER_CNAME) == columnIndex)
//            return true;
//        if (StringUtils.index(fieldName, CHECKER_CODE) == columnIndex || StringUtils.index(fieldName, CHECKER_NAME) == columnIndex || StringUtils.index(fieldName, CHECKER_CNAME) == columnIndex)
//            return true;
        return false;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


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
