package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

import static com.giants.hd.desktop.local.ConstantData.COLUMN_INDEX;

/**
 * 订单表格模型
 */

public class ErpOrderItemTableModel extends BaseListTableModel<ErpOrderItem> {


    @Inject
    public ErpOrderItemTableModel() {
        super(ErpOrderItem.class, TableStructureUtils.fromJson("erp_order_item.json"));

    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }


    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {

        ErpOrderItem data = getItem(rowIndex);

        String field=getFieldName(columnIndex);

        if(field.equalsIgnoreCase("currentOverDueDay"))
        {

            int currentOverDueDay=data.currentOverDueDay;

            if(currentOverDueDay==0)
            {
               return "";
            }
            else {



                if (currentOverDueDay > 0) {
                   return "超期" + Math.abs(currentOverDueDay) + "天";
                } else {
                    return  "剩余" + Math.abs(currentOverDueDay) + "天";
                }


            }



        }

        if(field.equalsIgnoreCase("totalLimit"))
        {

            int totalLimit=data.totalLimit;

            if(totalLimit==0)
            {
                return "";
            }
            else {

                String pre="";
                if( data.workFlowState== ErpWorkFlow.STATE_WORKING)
                {
                    pre="预计";

                }
                if (totalLimit > 0) {
                 return (pre+"超期" + Math.abs(totalLimit )+ "天");
                } else {
                    return (pre+"提前" + Math.abs(totalLimit )+ "天");
                }

            }


        }








        return super.getValueAt(rowIndex, columnIndex);
    }


}
