package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.file.ImageUtils;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class AppQuotationItemTableModel extends  BaseListTableModel<QuotationItem> {


    private OnValueChangeListener listener;

    public AppQuotationItemTableModel(OnValueChangeListener listener) {
        super(QuotationItem.class, TableStructureUtils.getAppQuotationItem());

        this.listener = listener;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        String columnName = getFieldName(columnIndex);
        switch (columnName)
        {
            case "price":
            case "qty":

            case "memo":

                return true;

        }

        return super.isCellEditable(rowIndex, columnIndex);
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        String columnName = getFieldName(columnIndex);

        switch (columnName)
        {
            case "price":
                try {
                    float value = Float.parseFloat(aValue.toString());
                    listener.onPriceChange(rowIndex,value);
                }catch (Throwable t)
                {
                    t.printStackTrace();
                }
                break;
            case "qty":
            {

                try {
                    int  value = Integer.parseInt(aValue.toString());
                    listener.onQtyChange(rowIndex,value);
                }catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
            break;
            case "memo":
            {

                try {
                    String   value = aValue.toString();
                    listener.onMemoChange(rowIndex,value);
                }catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
            break;



        }



    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }


  public static   interface  OnValueChangeListener
    {
        void onPriceChange(int itemIndex,float newValue);
        void onQtyChange(int itemIndex,int newQty);

        void onMemoChange(int rowIndex, String newValue);
    }
}
