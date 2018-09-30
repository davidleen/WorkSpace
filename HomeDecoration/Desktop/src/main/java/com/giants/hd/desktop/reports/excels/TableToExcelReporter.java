package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.model.TableField;
import com.giants3.report.ResourceUrl;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/9/23.
 */
public class TableToExcelReporter<T> extends SimpleExcelReporter<List<T >> {
    public static final String FILE_NAME = "Table导出模板";
    private static final String TEMPLATE_FILE_NAME = "excel/" + FILE_NAME + ".xls";

    private String fileName;
    private List<TableField> fields;

    public TableToExcelReporter(String fileName, List<TableField> fields) {
        this.fileName = fileName;

        this.fields = fields;
    }

//    @Override
//    public void report(List<Map > datas, String fileOutputDirectory) throws IOException, HdException {
//
//
//        try {
//            Workbook workbook = new HSSFWorkbook();
//
//
//            Sheet writableSheet = workbook.createSheet(title);
//            writableSheet.setDefaultColumnWidth(10);// 设置列宽
//            writableSheet.setDefaultRowHeightInPoints(  100);// 设置列宽
//
//            // 取得Table的行数(rowNum), 列数(colNum)
//            int rowNum = table.getRowCount();
//            int colNum = table.getColumnCount();
//
//            // 填写主标题
//            fillHeading(writableSheet, heading, colNum);
//
//            // 填写列名
//            fillColumnName(writableSheet, table, colNum);
//
//            // 填写落款
//            fillInscribe(writableSheet, describe, rowNum+4, Math.max(0,colNum-2));
//
//            // 填写数据
//            fillCell(writableSheet, table, rowNum, colNum);
//
//            FileOutputStream fos = new FileOutputStream(fileOutputDirectory);
//
//            workbook.write(fos);
//            workbook.close();
//
//
//            fos.close();
//
//            // 导出成功提示框
//            int dialog = JOptionPane.showConfirmDialog(null, "表导出成功！是否现在打开？", "提示", JOptionPane.YES_NO_OPTION);
//            if (dialog == JOptionPane.YES_OPTION) {
//                Runtime.getRuntime().exec("cmd /c start \"\" \"" + new File(fileOutputDirectory) + "\"");
//            }
//
//        } catch (FileNotFoundException e) {
//            JOptionPane.showMessageDialog(null, "导入数据前请关闭工作表");
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "没有进行筛选");
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void report(List<T > data, Workbook workbook) {

        Sheet writableSheet = workbook.getSheetAt(0);
        int startItemRow=1;int defaultRowCount=1;
        int dataSize=data.size();
        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);

        // 填写列名
        fillColumnName(writableSheet, fields);
        fillCell(workbook,writableSheet,data,fields);



    }

    @Override
    protected String getTemplateFilePath() {
        return TEMPLATE_FILE_NAME;
    }

    @Override
    protected String getDestFileName(List<T> data) {
        return fileName;
    }


    /**
     * 填写落款
     *
     * @param sheet
     * @param inscribe
     * @param colNum
     * @param rowNum
     */
    private void fillInscribe(Sheet sheet, String inscribe, int rowNum, int colNum) {
        if (inscribe == null || inscribe.length() < 1) {

            inscribe = "导出时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        addString(sheet, inscribe, colNum,rowNum );

//        WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.NO_BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 定义格式化对象
//
//        format.setAlignment(Alignment.RIGHT);// 水平居中显示
//
//        sheet.mergeCells(0, rowNum + 3, colNum - 1, rowNum + 3);// 合并单元格
//
//        sheet.addCell(new Label(0, rowNum + 3, inscribe, format));// 填写工作表

    }

    /**
     * 填写主标题
     *
     * @param sheet
     * @param heading
     * @param colNum
     */
    private void fillHeading(Sheet sheet, String heading, int colNum) {


        addString(sheet, heading,colNum  ,0 );


//        WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.RED);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 创建格式化对象
//
//        format.setAlignment(Alignment.CENTRE);// 水平居中显示
//
//        format.setVerticalAlignment(VerticalAlignment.CENTRE);// 垂直居中显示
//
//        sheet.mergeCells(0, 0, colNum - 1, 0); // 合并单元格
//
//        sheet.setRowView(0, 600); // 设置行高
//
//        sheet.addCell(new Label(0, 0, heading, format));// 填写工作表


    }

    /**
     * 填写列名
     *
     * @param sheet
     * @param fields
     */
    private void fillColumnName(Sheet sheet, List<TableField> fields) {


//        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 定义格式化对象
//
//        format.setAlignment(Alignment.CENTRE);// 水平居中显示
//
//        sheet.setColumnView(0, 15);// 设置列宽


        for (int col = 0; col < fields.size(); col++) {

//            Label colName = new Label(col, 1, table.getModel().getColumnName(col), format);
//
//            sheet.addCell(colName);
            addString(sheet, fields.get(col).columnName, col, 0);
            setColumnWidth(sheet,col, (int) (fields.get(col).width*1.5f*256/7));
        }
    }


    /**
     * 填写数据
     *
     * @param sheet
     * @param datas
     * @param fields
     */
    private void fillCell(Workbook workbook,Sheet sheet, List<T> datas,List<TableField> fields) {
//        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD,
//                false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);// 定义字体
//
//        WritableCellFormat format = new WritableCellFormat(font);// 定义格式化对象
//
//        format.setAlignment(Alignment.CENTRE); // 水平居中显示
        int startRow = 1;
        int colNum=fields.size();
        int rowNum=datas.size();
        for (int i = 0; i < colNum; i++) {    // 列

            for (int j = 0; j <  rowNum; j++) {// 行
                final TableField tableField = fields.get(i);
                Object object = getData(datas.get(j),tableField.fileName);
                if(object!=null) {
                    String str =  object.toString();
                    switch (tableField.aClass) {
                        case iMG:

                            String url = ResourceUrl.completeUrl(str);
                            attachPicture(workbook, sheet, url, i, j + startRow, i, j + startRow);
                            ;
                            break;
                        case S:
                            addString(sheet, str, i, j + startRow);

                            break;
                        case I:
                        case L:
                        case F:
                            final Float value = Float.valueOf(str);
                            addNumber(sheet, value, i, j + startRow);
                            break;
                        default:
                            addString(sheet, str, i, j + startRow);
                    }
                }




            }
        }
    }


    private Object getData(T data,String keyOrFieldName)
    {

        if(data instanceof  Map)
        {
            return ((Map)data).get(keyOrFieldName);
        }else
        {

            try {
             return    data.getClass().getField(keyOrFieldName).get(data);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;



    }
}
