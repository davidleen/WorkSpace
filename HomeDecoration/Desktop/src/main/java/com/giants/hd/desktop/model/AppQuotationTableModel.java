package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.WorkFlowWorker;
import com.giants3.hd.entity.app.Quotation;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class AppQuotationTableModel extends  BaseListTableModel<Quotation> {




    public AppQuotationTableModel( ) {
        super(Quotation.class, TableStructureUtils.getAppQuotation());

    }






}
