package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.viewImpl.Panel_QuotationDetail;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.QuotationXKItem;
import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.noEntity.Product2;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 * 报价明细项数据模型
 */

public class QuotationItemFixColumnXKTableModel extends BaseTableModel<QuotationXKItem> {


    private boolean hasVerify = false;

    public static final String COLUMN_COST_PRICE_RATIO = "cost_price_ratio";
    QuoteAuth quoteAuth = CacheManager.getInstance().bufferData.quoteAuth;
    private Panel_QuotationDetail.QuotationXkCostPriceRatioChangeListener listener;
    /**
     * 设置是否已经审核
     *
     * @param hasVerify
     */
    public void setHasVerify(boolean hasVerify) {
        this.hasVerify = hasVerify;
    }


    public static final String COLUMN_PRODUCT = "productName";
    public static final String PHOTO_URL = "thumbnail";

    public static String[] columnNames = new String[]{"序号", "图片", "品名", "配方号(折叠)", "配方号(加强)", "成本利润比"};
    public static int[] columnWidths = new int[]{40, ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT, 100, 60, 60, 50};


    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX, PHOTO_URL, COLUMN_PRODUCT, "pVersion", "pVersion2", COLUMN_COST_PRICE_RATIO};

    public static Class[] classes = new Class[]{Object.class, ImageIcon.class, String.class, Product.class, Product2.class};

    public static boolean[] editables = new boolean[]{false, false, false, true, true, true};


    @Inject
    public QuotationItemFixColumnXKTableModel(Panel_QuotationDetail.QuotationXkCostPriceRatioChangeListener listener) {
        super(columnNames, fieldName, classes, QuotationXKItem.class);
        this.listener = listener;

    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (quoteAuth.fobEditable && (fieldName[columnIndex].equals(COLUMN_COST_PRICE_RATIO))) {

            return true;
        }

        //已经审核 不能修改
        if (hasVerify) return false;

        return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        QuotationXKItem item = getItem(rowIndex);
        if (item.productId <= 0 && item.productId2 <= 0)
            return "";


        //贤康报价图片 必选显示一张
        if (fieldName[columnIndex].equals(PHOTO_URL)) {
            String destUrl = "";
            if (!StringUtils.isEmpty(item.thumbnail)) {
                destUrl = item.thumbnail;
            } else {
                destUrl = item.thumbnail2;
            }

            if (!StringUtils.isEmpty(destUrl))
                return loadImage(rowIndex, columnIndex, destUrl);

            return "";

        }


        if (fieldName[columnIndex].equals(COLUMN_PRODUCT)) {
            if (item.productId > 0)
                return item.productName;
            if (item.productId2 > 0)
                return item.productName2;
            return "";

        }
        if (fieldName[columnIndex].equals(COLUMN_COST_PRICE_RATIO)) {
            if (quoteAuth.fobVisible) {
                if (item.cost_price_ratio > 0) return item.cost_price_ratio;
                else
                    return CacheManager.getInstance().bufferData.globalData.cost_price_ratio;
            } else

                return "***";
        }


        return super.getValueAt(rowIndex, columnIndex);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        QuotationXKItem quotationItem = getItem(rowIndex);

        switch (columnIndex) {
            case 5:

                float newValue = 0;
                try {
                    newValue = FloatHelper.scale(Float.valueOf(aValue.toString()));
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                if (newValue != 0 && newValue != quotationItem.cost_price_ratio) {
                    if (listener != null) {

                        listener.onChange(quotationItem, quotationItem.cost_price_ratio, newValue);
                    }

                    quotationItem.cost_price_ratio = newValue;
                }
                break;


        }


        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
