package com.giants.hd.desktop.model;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.utils.Config;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import org.apache.commons.collections.map.LRUMap;
import org.apache.poi.ss.usermodel.Cell;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public     class ItemPickTableModel<T> extends AbstractTableModel implements CellConfig {



    private AbsTableModel<T> absTableModel;

    boolean[] selectedState;

    public ItemPickTableModel(AbsTableModel<T> absTableModel) {
        this.absTableModel = absTableModel;
        selectedState=new boolean[absTableModel.getRowCount()];
    }


    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return absTableModel.getRowCount();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return absTableModel.getColumnCount()+1;
    }

    /**
     * Returns a default name for the column using spreadsheet conventions:
     * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     * returns an empty string.
     *
     * @param column the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column) {

        if(column==0) return "";
        return absTableModel.getColumnName(column-1);
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if(columnIndex==0) return selectedState[rowIndex];
        return absTableModel.getValueAt(rowIndex,columnIndex-1);
    }


    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     * @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        if(columnIndex==0) return Boolean.class;
        return absTableModel.getColumnClass(columnIndex-1);
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param l the TableModelListener
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        super.addTableModelListener(l);
        absTableModel.addTableModelListener(l);



    }




    public  void  updateSelected(int rowIndex)
    {

        selectedState[rowIndex]=!selectedState[rowIndex];
        fireTableRowsUpdated(rowIndex,rowIndex);
    }


    public  boolean[] getSelectState()
    {
        return selectedState;
    }


    @Override
    public int getRowHeight() {
        return absTableModel.getRowHeight();
    }

    @Override
    public int getColumnWidth(int column) {
        return absTableModel.getColumnWidth(column);
    }

    @Override
    public int[] getColumnWidth() {
        return null;
    }

    public void selectAll() {

        setAllState(true);
    }

    public void clearAll() {

        setAllState(false);

    }


    private void setAllState(boolean selected)
    {

        if(selectedState==null) return ;
        for (int i = 0; i < selectedState.length; i++) {
            selectedState[i]=selected;
        }

        fireTableDataChanged();

    }
}
