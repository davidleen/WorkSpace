package com.giants.hd.desktop.reports;


/**
 * Created by david on 2015/9/11.
 */
public interface ExcelOperator {


    void addString(String value,int x,int y);
    void addNumber(float value,int x,int y);
    void addNumber(double value,int x,int y);
    void addNumber(int value,int x,int y);
    void addPicture(String url,int x,int y,int width,int height);
     void duplicateRow(  int startRow,int defaultRowCount,int dataSize);
}
