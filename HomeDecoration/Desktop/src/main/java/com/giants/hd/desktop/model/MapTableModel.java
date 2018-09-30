package com.giants.hd.desktop.model;

import com.giants3.hd.utils.file.ImageUtils;

import java.util.List;
import java.util.Map;

/**Map类型数据下发， 适用于用来展示报表类数据，避免建立各种一次性数据结构。
 * Created by davidleen29 on 2018/9/16.
 */
public class MapTableModel extends  BaseListTableModel<Map > {


    public MapTableModel(  List<TableField> tableFields) {
        super(Map.class, tableFields);
    }

    @Override
    protected Object getFieldData(Object data, String fieldName) {

        if(data instanceof  Map)
        {
            return ((Map) data).get(fieldName);
        }

        return null;

    }


    @Override
    public int getRowHeight() {
        return super.getRowHeight();
    }
}
