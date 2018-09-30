package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLClassLoader;

/**
 * 产品报价表格模型
 */

public class QuotationTableModel extends BaseTableModel<Quotation> {


    public static final String COLUMN_VERIFY="isVerified";
    public static String[] columnNames = new String[]{"报价日期", "报价单号","客户", "有效日期","币别", "业务员" ,"报价类型","审核","备注"};
    public static String[] fieldName = new String[]{"qDate",       "qNumber", "customerName", "vDate", "currency", "salesman","quotationTypeName",COLUMN_VERIFY,"memo"};
    public static int[] columnWidths = new int []{      120,              100,             100,    120,     80,          100,   120,50, ConstantData.MAX_COLUMN_WIDTH };


    public  static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class,Object.class ,ImageIcon.class,Object.class};

    private BufferedImage icon_verify_small;
    private BufferedImage icon_unverify_small=null;
    private BufferedImage icon_overdue_small=null;

    @Inject
    public QuotationTableModel() {
        super(columnNames,fieldName,classes,Quotation.class);

        try {
            icon_verify_small=ImageIO.read(((URLClassLoader)getClass().getClassLoader()).findResource("icons/verify_small.png"));
            icon_unverify_small=ImageIO.read(((URLClassLoader)getClass().getClassLoader()).findResource("icons/unverify_small.png"));
            icon_overdue_small=ImageIO.read(((URLClassLoader)getClass().getClassLoader()).findResource("icons/overdue_small.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Quotation quotation=  getItem(rowIndex);
        if(quotation.id<=0) return null;

        if(COLUMN_VERIFY.equals(fieldName[columnIndex]))
        {
                    return new ImageIcon( quotation.isOverdue()?icon_overdue_small:quotation.isVerified?icon_verify_small:icon_unverify_small);

        }



        return super.getValueAt(rowIndex, columnIndex);
    }
}
