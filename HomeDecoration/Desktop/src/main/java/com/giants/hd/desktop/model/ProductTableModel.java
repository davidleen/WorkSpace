package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants3.hd.noEntity.ProductAgent;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.noEntity.ModuleConstant;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Product;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 产品表格模型
 */

public class ProductTableModel extends BaseTableModel<Product> {


    public static final String COLUMN_SPEC="spec";

    public static final String COLUMN_SHOW_COST="column_show_cost";
    private static final String COLUMN_PACK_SPEC = "column_packSpec";
    public static String[] columnNames = new String[]{"图片", "货号","版本号","包装类型", "规格","箱规", "单位", "类别", "日期","材料成本","厂家编号","厂家名称","备注"};
    public static int[] columnWidth=new int[]{ ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 120, 60, 100,120,150,40, 60,120 ,    120, 80,80,ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{"thumbnail", "name",  "pVersion","pack",COLUMN_SPEC, COLUMN_PACK_SPEC,"pUnitName", "pClassName", "rDate",COLUMN_SHOW_COST,"factoryCode","factoryName","memo"};

    public  static Class[] classes = new Class[]{ImageIcon.class, Object.class, Object.class, Object.class, Object.class, Object.class};


    private boolean viewPrice=false;

    @Inject
    public ProductTableModel() {
        super(columnNames,fieldName,classes,Product.class);

        viewPrice= AuthorityUtil.getInstance().canViewPrice(ModuleConstant.NAME_PRODUCT);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }

    @Override
    public int[] getMultiLineColumns() {
        return new int[]{ArrayUtils.indexOnArray(fieldName,COLUMN_SPEC),ArrayUtils.indexOnArray(fieldName,COLUMN_SHOW_COST)};
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Product product=getItem(rowIndex);
        if(product.id<=0) return null;

        //显示成本简易显示信息
        switch ( fieldName[columnIndex]) {
            case COLUMN_SHOW_COST:
                if (!viewPrice) return "";
                String value = "";
                value += "白胚 : " + product.conceptusCost + StringUtils.row_separator;
                value += "组装 : " + product.assembleCost + StringUtils.row_separator;
                value += "油漆 : " + product.paintCost + StringUtils.row_separator;
                value += "包装 : " + product.packCost + StringUtils.row_separator;

                return value;

            case COLUMN_PACK_SPEC:

                return ProductAgent.getProductFullPackageInfo(product)  ;

                default:
            return super.getValueAt(rowIndex, columnIndex);
        }


    }
}
