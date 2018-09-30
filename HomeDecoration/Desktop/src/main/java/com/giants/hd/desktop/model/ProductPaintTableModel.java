package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.RandomUtils;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.entity.GlobalData;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.ProductPaint;
import com.giants3.hd.entity.ProductProcess;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.utils.file.ImageUtils;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * 油漆工表格模型
 */
public class ProductPaintTableModel extends BaseTableModel<ProductPaint> implements Materialable {

    GlobalData globalData= CacheManager.getInstance().bufferData.globalData;

    public static final String COLUMN_materialQuantity="materialQuantity";
    public static final String COLUMN_ingredientRatio="ingredientRatio";
    public static String[] columnNames = new String[]{"序号","工序代码", "工序名称", "工价","材料编码" ,"材料名称", "配料比例", "单位", "用量", "物料单价", "物料费用", "配料量","稀释剂","备注"};

    public static int[] columnWidths = new int []{      40,   80,             80,    60,     100,        120,       80,        40,     60,       60,      60,          60,        60, ConstantData.MAX_COLUMN_WIDTH };


    private static final String PRICE = "price";
    private static final String COST = "cost";
    public static String[] fieldName = new String[]{ ConstantData.COLUMN_INDEX,"processCode", "processName", "processPrice","materialCode", "materialName", "ingredientRatio", "unitName", "quantity", PRICE, COST, "materialQuantity","ingredientQuantity","memo"};
    public static Class[] classes = new Class[]{Object.class,String.class, String.class, Object.class,  Material.class,Material.class, Object.class, String.class };

    public static boolean[] editables = new boolean[]{false,true, true, true, true,true,  true, false, true, false, false, false,false, true };


    @Inject
    public ProductPaintTableModel() {
        super(new ArrayList<ProductPaint>(),columnNames, fieldName, classes, ProductPaint.class);
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        if(fieldName[columnIndex].equals(COST)||fieldName[columnIndex].equals(PRICE))
        {
            if(AuthorityUtil.getInstance().cannotViewProductPrice())
                return "";
        }
        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        //当前行是XISHUA 行  并且不是材料选择行
        String processName=getItem(rowIndex).processName;
        if(processName!=null&&processName.contains(ProductProcess.XISHUA)&&(columnIndex==6||columnIndex==8))
        {
            return false;
        }
        return editables[columnIndex];
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductPaint item = getItem(rowIndex);
        switch (columnIndex) {
            // "工序代码"
            case 1:
                item.setProcessCode(aValue.toString());
                break;
            //"工序名称"
            case 2:
                item.setProcessName(aValue.toString().trim());
                /**
                 * 当前行修改是洗刷统计行
                 */
                if(item.processName!=null&&item.processName.contains(ProductProcess.XISHUA) )
                {
                    updateQuantityOfIngredient();

                }


                break;
                //"工价"
            case 3:
                try {
                item.setProcessPrice(Float.valueOf(aValue.toString()));
                }catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }
                break;
            //"配料比例"
            case 6:
                try {
                item.ingredientRatio= Float.valueOf(aValue.toString());

                    ProductAnalytics.updateProductPaintPriceAndCostAndQuantity(item, globalData);


                    updateQuantityOfIngredient();
                }catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }
                break;

            //"用量"
            case 8:

                try {
                    item.quantity = Float.valueOf(aValue.toString());

                    ProductAnalytics.updateProductPaintPriceAndCostAndQuantity(item, globalData);


                    updateQuantityOfIngredient();
                }catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }
                break;

            //"备注说明"
            case 13:
                item.setMemo(aValue.toString());
                break;

        }


        fireTableRowsUpdated(rowIndex, rowIndex);




    }

    /**
     * 更新配料洗刷枪的费用的数据量值。
     */
    public void updateQuantityOfIngredient()
    {




           int index= ProductAnalytics.updateQuantityOfIngredient(datas, globalData);
            if(index>-1)
                fireTableRowsUpdated(index, index);




    }


    @Override
    public void  setMaterial(Material material,int rowIndex)
    {


        ProductPaint productPaint=getItem(rowIndex);
        if(productPaint!=null)
        {
            ProductAnalytics.setMaterialToProductPaint(productPaint, material,globalData);
        }





        fireTableRowsUpdated(rowIndex,rowIndex);

    }



    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT*2/3;
    }


    @Override
    public ProductPaint addNewRow(int index) {
        ProductPaint productPaint= super.addNewRow(index);

        productPaint.id= -RandomUtils.nextInt();
        return productPaint;
    }


    @Override
    public void insertNewRows(List<ProductPaint> insertDatas, int index) {

        for (ProductPaint paint:insertDatas)
        {
            paint.id=-1;
        }
        super.insertNewRows(insertDatas, index);
    }
}
