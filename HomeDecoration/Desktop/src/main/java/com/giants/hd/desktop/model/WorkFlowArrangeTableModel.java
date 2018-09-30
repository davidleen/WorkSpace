package com.giants.hd.desktop.model;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class WorkFlowArrangeTableModel extends  BaseListTableModel<WorkFlowArrangeTableModel.WorkFlowConfig> {




    public WorkFlowArrangeTableModel(Class   itemClass) {
        super(itemClass);

    }
    public WorkFlowArrangeTableModel(   Class itemClass,List<TableField> tableFields) {
        super(itemClass,tableFields);

    }




    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
       String fieldName= getFieldName(columnIndex);
        if(fieldName.equals("workFlowStep")||fieldName.equals("workFlowName")) return  false;
        return true;
    }

    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        WorkFlowConfig  item=getItem(rowIndex);

        boolean booleanValue=false;
        try{

            booleanValue=Boolean.valueOf(aValue.toString());
        }catch (Throwable t){t.printStackTrace();}
        switch (columnIndex)
        {
            case 2:

                item.tiejian=booleanValue;
                break;
            case 3:
                item.mujian=booleanValue;
                break;
            case 4:
                item.pu=booleanValue;
                break;

        }

        fireTableCellUpdated(rowIndex,columnIndex);


    }


    public static class WorkFlowConfig {
        public int workFlowStep;
        public String workFlowName;
        public boolean tiejian;
        public boolean mujian;
        public boolean pu;
    }

}
