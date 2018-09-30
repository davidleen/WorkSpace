package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.RandomUtils;
import com.giants3.hd.entity.Material;
import com.giants3.hd.entity.ProductMaterial;
import com.giants3.hd.logic.ProductAnalytics;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import java.util.List;

/**
 * 材料列表  表格模型
 */

public class ProductMaterialTableModel extends BaseTableModel<ProductMaterial> implements Materialable {


    public static String[] columnNames = new String[]{"序号", "物料编码", "材料名称", "数量", "长", "宽", "高", "毛长", "毛宽", "毛高", "配额", "单位", "利用率", "开法", "类型", "单价", "金额", "副单位", "副单价", "分件备注"};
    public static int[] columnWidths = new int[]{40, 100, 120, 40, 40, 40, 40, 40, 40, 40, 80, 40, 60, 60, 40, 60, 80, 60, 60, ConstantData.MAX_COLUMN_WIDTH};

    private static final String PRICE = "price";
    private static final String AMOUNT = "amount";
    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX, "materialCode", "materialName", "quantity", "pLong", "pWidth", "pHeight", "wLong", "wWidth", "wHeight", "quota", "unitName", "available", "cutWay", "type", PRICE, AMOUNT, "unit2", "price2", "memo"};
    public static Class[] classes = new Class[]{Object.class, Material.class, Material.class};

    public static boolean[] editables = new boolean[]{false, true, true, true, true, true, true, false, false, false, false, false, true, false, false, false, false, false, false, true};

    @Inject
    public ProductMaterialTableModel() {
        super(columnNames, fieldName, classes, ProductMaterial.class);
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        if (fieldName[columnIndex].equals(AMOUNT) || fieldName[columnIndex].equals(PRICE)) {
            if (AuthorityUtil.getInstance().cannotViewProductPrice())
                return "";
        }
        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editables[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductMaterial material = getItem(rowIndex);
        String valueString = aValue.toString().trim();
        float floatValue = 0;
        try {
            floatValue = Float.valueOf(valueString);
        } catch (Throwable t) {

        }
        switch (columnIndex) {
            case 3:
                //设置用量


                material.setQuantity(floatValue);
                ProductAnalytics.update(material);

                break;


            case 4:
                //设置长
            {


                material.setpLong(floatValue);
                ProductAnalytics.update(material);
            }
            break;


            case 5:
                //设置宽
            {

                material.setpWidth(floatValue);
                ProductAnalytics.update(material);
            }
            break;

            case 6:
                //设置高
            {

                material.setpHeight(floatValue);
                ProductAnalytics.update(material);
            }
            break;

            case 12:
                //设置利用率

                material.setAvailable(floatValue);
                ProductAnalytics.update(material);


                break;

            case 19:
                //设置备注
                material.setMemo(aValue.toString());
                break;


        }


        fireTableRowsUpdated(rowIndex, rowIndex);


    }


    @Override
    public void setMaterial(Material material, int rowIndex) {


        ProductMaterial productMaterial = getItem(rowIndex);
        if (productMaterial != null) {
            ProductAnalytics.setMaterialToProductPaint(productMaterial, material);
        }


        fireTableRowsUpdated(rowIndex, rowIndex);

    }


    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT * 2 / 3;
    }


    @Override
    public ProductMaterial addNewRow(int index) {
        ProductMaterial productMaterial = super.addNewRow(index);

        productMaterial.id = -RandomUtils.nextInt();
        return productMaterial;
    }

    @Override
    public void insertNewRows(List<ProductMaterial> insertDatas, int index) {

        for (ProductMaterial material : insertDatas) {
            material.id = -1;
        }
        super.insertNewRows(insertDatas, index);
    }
}
