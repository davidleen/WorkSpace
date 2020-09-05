package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.ProducerValueConfig;
import com.giants3.hd.entity.WorkFlowWorker;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class ProducerValueConfigTableModel extends BaseListTableModel<ProducerValueConfig> {


    private boolean edit;

    public ProducerValueConfigTableModel() {
        super(ProducerValueConfig.class, TableStructureUtils.fromJson("producerValueConfig.json"));

    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if (edit) {
            switch (getFieldName(columnIndex)) {
                case "maxOutputValue":
                case "minOutputValue":
                case "workerNum":
                case "memo":
                    return true;
            }
        }
        return false;


    }


    public void setEdit(boolean edit) {

        this.edit = edit;
        fireTableDataChanged();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String columnName = getFieldName(columnIndex);

        ProducerValueConfig item = getItem(rowIndex);
        switch (columnName) {
            case "maxOutputValue":
                try {
                    float value = Float.parseFloat(aValue.toString());
                    item.maxOutputValue = value;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;
            case "minOutputValue":
                try {
                    float value = Float.parseFloat(aValue.toString());
                    item.minOutputValue = value;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;
                case "workerNum":
                try {
                    int value = Integer.valueOf(aValue.toString());
                    item.workerNum = value;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                break;
            case "memo": {

                try {
                    item.memo = aValue.toString();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            break;



        }
        fireTableRowsUpdated(rowIndex,rowIndex);

    }
}
