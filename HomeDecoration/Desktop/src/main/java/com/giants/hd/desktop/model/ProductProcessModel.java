package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.utils.file.ImageUtils;

/**
 * 工序表格模型
 */
public class ProductProcessModel  extends  BaseTableModel<ProductProcess>{


    public static String[] columnNames = new String[]{"序号","工序编码", "工序名称","说明"};
    public static int[] columnWidths=new int[]{    40,      150        ,150, ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"code", "name","memo" };
    public  static Class[] classes = new Class[]{Object.class,String.class, String.class  };

    public  static boolean[] editables = new boolean[]{false,true, true, true, true};

    public boolean canEdit=true;

    public ProductProcessModel( ) {
       this(true);
    }

    public ProductProcessModel(boolean canEdit ) {
        super(columnNames, fieldName, classes, ProductProcess.class);
        this.canEdit=canEdit;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit&& editables[columnIndex];
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductProcess data=getItem(rowIndex);

        String value=aValue==null?"":aValue.toString();
        switch (columnIndex)
        {

            case  1:
                data.code=value;

                break;
            case 2:

                data.name=value;
                break;


            case 3:


                data.memo=value;
                break;


            case 5:

                data.memo=value;
                break;


        }


        fireTableRowsUpdated(rowIndex,rowIndex);


    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT*2/3;
    }
}
