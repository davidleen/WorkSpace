package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.WorkFlowWorker;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class WorkFlowWorkerTableModel extends  BaseListTableModel<WorkFlowWorker> {




    public WorkFlowWorkerTableModel( ) {
        super(WorkFlowWorker.class, TableStructureUtils.getWorkFlowWorker());

    }






}
