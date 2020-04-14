package com.giants.hd.desktop.model;

import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.CompanyPosition;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import java.util.List;

/**
 * 业务员表格数据模型
 */
public class UserModel extends BaseTableModel<User> {

    public static String[] columnNames = new String[]{"编号 ", "名称", "中文名称      ", "密码", "电话", "邮件", "是否业务员", "  职位  ", "外网连接","停用"};
    public static int[] columnWidth = new int[]{80, 100, 120, 80, 80, 80, 50, 100, 50,50};

    private static final String POSITION = "positionName";
    public static String[] fieldName = new String[]{"code", "name", "chineseName", "password", "tel", "email", "isSalesman", POSITION, "internet","stopped"};

    public static Class[] classes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Boolean.class, CompanyPosition.class, Boolean.class,Boolean.class};


    @Inject
    public UserModel() {
        super(columnNames, fieldName, classes, User.class);
        setRowAdjustable(true);

    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        User customer = getItem(rowIndex);
        String stringValue = String.valueOf(aValue);
        switch (columnIndex) {
            case 0:
                customer.code = stringValue;
                break;
            case 1:
                customer.name = stringValue;
                break;
            case 2:
                customer.chineseName = stringValue;
                break;
            case 3:
                customer.password = stringValue;
                break;

            case 4:
                customer.tel = stringValue;
                break;
            case 5:
                customer.email = stringValue;
                break;
            case 6:
                customer.isSalesman = Boolean.valueOf(stringValue);
                break;


            case 7:

                if (aValue instanceof CompanyPosition) {
                    customer.positionName = ((CompanyPosition) aValue).positionName;
                    customer.position = ((CompanyPosition) aValue).position;


                }

                break;
            case 8:
                try {
                    customer.internet = Boolean.valueOf(aValue.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

                case 9:
                try {
                    customer.stopped = Boolean.valueOf(aValue.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }


    @Override
    public void setDatas(List<User> newDatas) {

        MiniRowCount = newDatas.size() + 20;
        super.setDatas(newDatas);
    }
}
