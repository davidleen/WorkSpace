package com.giants.hd.desktop.model;

import com.giants.hd.desktop.viewImpl.Panel_QuotationDetail;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.entity.QuotationItem;
import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.logic.QuotationAnalytics;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 报价明细项数据模型
 */

public class QuotationItemTableModel extends BaseTableModel<QuotationItem> {


    private boolean hasVerify = false;
    private Panel_QuotationDetail.QuotationCostPriceRatioChangeListener listener;

    /**
     * 设置是否已经审核
     *
     * @param hasVerify
     */
    public void setHasVerify(boolean hasVerify) {
        this.hasVerify = hasVerify;
    }


    public static final String COLUMN_SPEC = "spec";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_COST_PRICE_RATIO = "cost_price_ratio";
    public static String[] columnNames = new String[]{"*内盒*", "*每箱数*", "客户箱规", "单位", "成本价", "成本利润比", "FOB", "立方数", "净重", "货品规格", "材质", "镜面尺寸", "备注"};
    public static int[] columnWidths = new int[]{50, 60, 120, 40, 50, 50, 50, 50, 50, 100, 120, 80, 400};

    public static final String MEMO = "memo";
    public static String[] fieldName = new String[]{"inBoxCount", "packQuantity", "packageSize", "unit", COLUMN_COST, COLUMN_COST_PRICE_RATIO, COLUMN_PRICE, "volumeSize", "weight", COLUMN_SPEC, "constitute", "mirrorSize", MEMO};
    public static Class[] classes = new Class[]{};

    public static boolean[] editables = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, true};


    QuoteAuth quoteAuth = CacheManager.getInstance().bufferData.quoteAuth;

    @Inject
    public QuotationItemTableModel(Panel_QuotationDetail.QuotationCostPriceRatioChangeListener listener) {
        super(columnNames, fieldName, classes, QuotationItem.class);
        this.listener = listener;
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {


        //如果有修改fob的权限   其他都不可以修改
        if (quoteAuth.fobEditable && fieldName[columnIndex].equals(COLUMN_COST_PRICE_RATIO)) {

            return true;

        }

        //已经审核 不能修改
        if (hasVerify) return false;

        return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {


        QuotationItem item = getItem(rowIndex);
        if (item.productId <= 0)
            return "";
        if (fieldName[columnIndex].equals(COLUMN_COST) && !quoteAuth.costVisible) {
            return "***";
        }


        if (fieldName[columnIndex].equals(COLUMN_PRICE) && !quoteAuth.fobVisible) {
            return "***";
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
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        super.setValueAt(aValue, rowIndex, columnIndex);

        QuotationItem quotationItem = getItem(rowIndex);


        switch (columnIndex) {

            case 5:

                  float newValue=0;
                try {
                    newValue= FloatHelper.scale(Float.valueOf(aValue.toString()));
                }catch (Throwable t)
                {
                    t.printStackTrace();
                }

                if(newValue!=0&&newValue!=quotationItem.cost_price_ratio) {
                    if (listener != null) {

                        listener.onChange(quotationItem, quotationItem.cost_price_ratio, newValue);
                    }

                    quotationItem.cost_price_ratio = newValue;
                }
                break;
            case 11:

                quotationItem.memo = aValue.toString();
                break;
        }


        fireTableRowsUpdated(rowIndex, rowIndex);


    }


    @Override
    public int[] getMultiLineColumns() {
        return new int[]{ArrayUtils.indexOnArray(fieldName, COLUMN_SPEC), ArrayUtils.indexOnArray(fieldName, MEMO)};
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }


}
