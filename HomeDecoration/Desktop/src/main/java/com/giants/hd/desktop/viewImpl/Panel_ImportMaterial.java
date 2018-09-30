package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.filters.ExcelFileFilter;
import com.giants.hd.desktop.filters.PictureFileFilter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.reports.EXCEL_TYPE;
import com.giants.hd.desktop.utils.FileChooserHelper;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Material;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.utils.pools.ObjectPool;
import com.giants3.hd.utils.pools.PoolCenter;
import com.google.inject.Inject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 从excel导入材料
 */
public class Panel_ImportMaterial extends BasePanel {
    private JTextField jtf_file;
    private JTextField jtf_sheet;
    private JFormattedTextField jtf_row;
    private JButton btn_file;
    private JButton btn_import;
    private JPanel panel1;




    @Inject
    ApiManager apiManager;

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    public Panel_ImportMaterial() {

        btn_import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

               final String path = jtf_file.getText().toString().trim();

                if (StringUtils.isEmpty(path)) {

                    JOptionPane.showMessageDialog((Component) e.getSource(), "请选择Excel文件!");
                }
                new HdSwingWorker<Void, Long>(getWindow((Component) e.getSource())) {
                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {
                        readExcel(path, jtf_sheet.getText().toString().trim(), Integer.valueOf(jtf_row.getText().toString().trim()));
                        return null;
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {


                        JOptionPane.showMessageDialog((Component) e.getSource(), "导入材料成功!");




                    }
                }.go();



            }
        });


        btn_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final File file = FileChooserHelper.chooseFile(JFileChooser.FILES_ONLY, false, new ExcelFileFilter("xls"));



                if (file != null) {
                /*
                    获得你选择的文件绝对路径
                */
                    String path = file.getAbsolutePath();
                    jtf_file.setText(path);

                }
            }
        });

    }








    /**
     * 读取excel 值 更新材料表
     */
    private void readExcel(String path, String sheetName, int firstRow) {


        try

        {

        String fileType=path.substring(path.lastIndexOf(".")+1);


        InputStream is = new FileInputStream(path);
        Workbook rwb;
        if(EXCEL_TYPE.XLSX.equals(fileType))
        {
            rwb=new XSSFWorkbook(is);
        }else {
            rwb=new HSSFWorkbook(is);
        }






            //这里有两种方法获取sheet表:名字和下标（从0开始）

            Sheet st = rwb.getSheet(sheetName);


            int rsRows = st.getLastRowNum();


            List<Material> materials=new ArrayList<>();
            int batchSize=100;
            ObjectPool<Material> materialObjectPool= PoolCenter.getObjectPool(Material.class,batchSize);
            Material material;

            for (int i = firstRow; i < rsRows; i++) {



                Row row=st.getRow(i);

                Cell cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);


                String value = cell.getStringCellValue();

                if(StringUtils.isEmpty(value))
                {
                    //如果编码为空 直接跳过
                    continue;

                }
                material = materialObjectPool.newObject();

                //复制值。
                material.code = value;
                material.name = row.getCell(1, Row.RETURN_BLANK_AS_NULL).getStringCellValue();
                try {
                    material.wLong =FloatHelper.scale(row.getCell(2, Row.RETURN_BLANK_AS_NULL).getNumericCellValue());
                } catch (Throwable t) {
                    material.wLong=0;
                }
                try {
                    material.wWidth = FloatHelper.scale(row.getCell(3, Row.RETURN_BLANK_AS_NULL).getNumericCellValue());
                } catch (Throwable t) {
                    material.wWidth=0;
                }
                try {
                    material.wHeight = FloatHelper.scale(row.getCell(4, Row.RETURN_BLANK_AS_NULL).getNumericCellValue());
                } catch (Throwable t) {
                    material.wHeight=0;
                }

                try {
                    material.available = FloatHelper.scale(row.getCell(5, Row.RETURN_BLANK_AS_NULL).getNumericCellValue());
                } catch (Throwable t) {
                    material.available=0;
                }

                try {
                    material.discount = FloatHelper.scale(row.getCell(6, Row.RETURN_BLANK_AS_NULL).getNumericCellValue());
                } catch (Throwable t) {
                    material.discount=0;
                }

                try {
                    material.price = FloatHelper.scale(row.getCell(7, Row.RETURN_BLANK_AS_NULL).getNumericCellValue());
                } catch (Throwable t) {
                    material.price=0;
                }

                try {
                    material.typeId = (int )row.getCell(8, Row.RETURN_BLANK_AS_NULL).getNumericCellValue() ;
                } catch (Throwable t) {
                    material.typeId=0;
                }

                material.typeName = String.valueOf(material.typeId);

                material.classId = row.getCell(9, Row.RETURN_BLANK_AS_NULL).getStringCellValue();

                material.spec =row.getCell(10, Row.RETURN_BLANK_AS_NULL).getStringCellValue();

                material.unitName = row.getCell(11, Row.RETURN_BLANK_AS_NULL).getStringCellValue();

                material.className = row.getCell(12, Row.RETURN_BLANK_AS_NULL).getStringCellValue();




                materials.add(material);
                int size=materials.size();

                //如果是大于100  或者已经最后一行了 提交
                if(size>=batchSize||i ==rsRows-1)
                {


                    apiManager.saveMaterials(materials);
                    for (int j = 0; j < size; j++) {
                        Material temp=materials.get(j);
                        materialObjectPool.release(temp);
                    }

                    materials.clear();


                }

            }





            //Sheet st = rwb.getSheet(0);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (HdException e) {
            e.printStackTrace();
        }

    }
}
