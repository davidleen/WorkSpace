package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.WorkFlowArranger;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class WorkFlowArrangerTableModel extends  BaseListTableModel<WorkFlowArranger> {




    public WorkFlowArrangerTableModel( ) {
        super(WorkFlowArranger.class, TableStructureUtils.getWorkFlowArranger());

    }






}
