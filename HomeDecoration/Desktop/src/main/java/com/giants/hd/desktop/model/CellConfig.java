package com.giants.hd.desktop.model;

/**表格對應參數的配置接口
 * Created by davidleen29 on 2017/7/31.
 */
public interface CellConfig{

        int getRowHeight();
          int getColumnWidth(int column);
    int[] getColumnWidth();
}
