package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.MaterialClass;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 材料分类表格模型
 */

public class MaterialClassTableModel extends BaseTableModel<MaterialClass> {
    public static String[] columnNames = new String[]{   "物料代码前四位", "类别", "毛长", "毛宽", "毛高", "利用率","损耗率",  "类型" , "备注"};
    public static int[] columnWidth=new int[]{      100,         60,           40,     40,    40,     60,     60   , 30,       ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ "code", "name", "wLong", "wWidth", "wHeight", "available","discount" ,"type", "memo"};

    public  static Class[] classes = new Class[]{ Object.class, Object.class, Object.class, Object.class, Object.class, Object.class};

    @Inject
    public MaterialClassTableModel() {
        super(columnNames,fieldName,classes,MaterialClass.class);
    }




    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT ;
    }




    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        if(StringUtils.isEmpty(getItem(rowIndex).code))
            return null;
        return super.getValueAt(rowIndex, columnIndex);
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        MaterialClass materialClass=getItem(rowIndex);
        String convertValue=aValue.toString().trim();
        float floatValue=0;
        try{
            floatValue= StringUtils.isEmpty(convertValue)?0:Float.valueOf(convertValue);
        }catch (Throwable t)
        {}

        int newTypeValue=materialClass.type;
        try{
            newTypeValue= Integer.valueOf(convertValue);
        }catch (Throwable t)
        {}
        switch (columnIndex)
        {

            case 0:materialClass.code=convertValue;break;
            case  1:materialClass.name=convertValue;break;
            case 2:materialClass.wLong=floatValue;break;
            case 3:materialClass.wWidth=floatValue;break;
            case 4:materialClass.wHeight=floatValue;break;
            case 5:materialClass.available=floatValue;break;
            case 6:materialClass.discount=floatValue;break;
            case 7:materialClass.type=newTypeValue;break;

            case 8:materialClass.memo=convertValue;break;
        }


        fireTableCellUpdated(rowIndex,columnIndex);

    }
}
