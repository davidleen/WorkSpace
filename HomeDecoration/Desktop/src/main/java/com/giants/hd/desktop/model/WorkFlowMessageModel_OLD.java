package com.giants.hd.desktop.model;

import com.giants3.hd.entity.Authority;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;

/**
 *  业务员表格数据模型
 */
public class WorkFlowMessageModel_OLD extends  BaseTableModel<WorkFlowMessage> {

    public static String[] columnNames = new String[]{ "图片",   "订单 ","货号", "订单数量","交接数量","制令单","发起流程", "发起人"  ,"发起时间","交接区域","接收流程","接收人","接收时间"  };
    public static int[] columnWidth=new int[]{   ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 80,80, 70, 70,  100,80,   110, 110 ,80, 80 ,80, 80 ,80 };

    public static String[] fieldName = new String[]{ "thumbnail","orderName", "productName","orderItemQty","transportQty","mrpNo",  "fromFlowName","senderName","createTimeString","area","toFlowName","receiverName","receiveTimeString"};

    public  static Class[] classes = new Class[]{ImageIcon.class  };


    @Inject
    public WorkFlowMessageModel_OLD() {
        super(columnNames, fieldName, classes, WorkFlowMessage.class);
    }







    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }
    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT ;
    }
}
