package com.giants.hd.desktop.model;

import com.giants3.hd.entity.ProductDelete;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 已经删除的产品表格模型
 */

public class ProductDeleteModel extends BaseTableModel<ProductDelete> {





    public static String[] columnNames = new String[]{"图片", "货号","版本号","删除人", "删除人", "删除日期" };
    public static int[] columnWidth=new int[]{ ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 120, 60, 100,100,160 };
    public static String[] fieldName = new String[]{"url", "productName",  "pVersion","userName","userCName","timeString"};

    public  static Class[] classes = new Class[]{ImageIcon.class };



    @Inject
    public ProductDeleteModel() {
        super(columnNames,fieldName,classes,ProductDelete.class);
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
