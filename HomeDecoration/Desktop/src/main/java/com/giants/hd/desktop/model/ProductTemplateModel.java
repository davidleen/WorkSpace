package com.giants.hd.desktop.model;

import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.noEntity.ProductDetail;
import com.google.inject.Inject;

/**
 *  产品套版模型
 */
public class ProductTemplateModel extends  BaseTableModel<ProductDetail> {

    public static String[] columnNames = new String[]{    "套版名称 "  };
    public static int[] columnWidth=new int[]{   200,    };

    public static String[] fieldName = new String[]{ "product" };

    public  static Class[] classes = new Class[]{Object.class };

    @Inject
    public ProductTemplateModel() {
        super(columnNames, fieldName, classes, ProductDetail.class);
    }



    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if(fieldName[columnIndex].equals("product") )
        {

          return  getItem(rowIndex).product.name;

        }

        return   super.getValueAt(rowIndex, columnIndex);
    }





    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }
    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT ;
    }
}
