package com.giants.hd.desktop.model;

import com.giants3.hd.entity.PackMaterialClass;
import com.giants3.hd.entity.PackMaterialPosition;
import com.giants3.hd.entity.PackMaterialType;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 材料分类表格模型
 */

public class PackMaterialClassTableModel extends BaseTableModel<PackMaterialClass> {



    public static final   String COLUMN_NAME="name";
    public static String[] columnNames = new String[]{   "包装材料类别", "默认包材材质类型", "默认包装使用位置" };
    public static int[] columnWidth=new int[]{      150,         150,           150 };

    public static String[] fieldName = new String[]{ COLUMN_NAME, "type", "position"};

    public  static Class[] classes = new Class[]{ Object.class, PackMaterialType.class,PackMaterialPosition.class};

    @Inject
    public PackMaterialClassTableModel() {
        super(columnNames,fieldName,classes,PackMaterialClass.class);
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
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        PackMaterialClass materialClass=getItem(rowIndex);
        if(materialClass.id>0&& StringUtils.isEmpty(materialClass.name))
        {
            //系统设定空行 不能修改
            return false;
        }


        //如果材料分类名称是预定义的 不能修改。
        if(COLUMN_NAME.equals(fieldName[columnIndex] ) &&!StringUtils.isEmpty(materialClass.getName())&& ArrayUtils.indexOnArray(PackMaterialClass.PRESERVED_CLASS,materialClass.getName())>-1)
            return false;



        return true;
    }



    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        PackMaterialClass materialClass=getItem(rowIndex);

        if(aValue instanceof  PackMaterialType)
        {

            materialClass.type=(PackMaterialType)aValue;
        }
        else
        if(aValue instanceof  PackMaterialPosition)
        {
            materialClass.position=(PackMaterialPosition)aValue;
        }



        fireTableCellUpdated(rowIndex,columnIndex);

    }
}
