package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.WorkFlowEvent;
import com.giants3.hd.entity.WorkFlowWorker;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class WorkFlowEventTableModel extends  BaseListTableModel<WorkFlowEvent> {




    public WorkFlowEventTableModel( ) {
        super(WorkFlowEvent.class, TableStructureUtils.getWorkFlowWorker());

    }






}
