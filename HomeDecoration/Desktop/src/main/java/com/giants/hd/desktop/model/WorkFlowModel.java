package com.giants.hd.desktop.model;

import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 生产进度数据模型
 */
public class WorkFlowModel extends BaseTableModel<ErpWorkFlow> {

    public static String[] columnNames = new String[]{"流程编码","流程序号", "流程名称",};
    public static int[] columnWidth = new int[]{80,80, 100, };


    public static String[] fieldName = new String[]{"code","step", "name"};

    public static Class[] classes = new Class[]{Object.class, Object.class};


    @Inject
    public WorkFlowModel() {
        super(columnNames, fieldName, classes, ErpWorkFlow.class);
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
