package com.giants3.report.jasper;

import com.giants3.hd.noEntity.ProductAgent;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/4/14.
 */
public class QrProductReport extends JRReport {



    private final String jasperFileName;


    List<ProductWithQR> list=null;

    public QrProductReport(ProductWithQR product , String jasperFileName, int count) {

        this.jasperFileName = jasperFileName;


        list = new ArrayList<>();

        //a4
        for (int i = 0; i < count; i++) {

            list.add(product);

        }



    }


    public QrProductReport(List<ProductWithQR> products, String jasperFileName ) {




        this.jasperFileName = jasperFileName;


        list = products;



    }

    @Override
    public JRDataSource getDataSource() {


        return new CustomBeanDataSource(list){


            @Override
            public Object getFieldValue(Object bean,JRField field) throws JRException {


                String propertiyName=super.getPropertyName(field);

                ProductWithQR productWithQR = (ProductWithQR) bean;
            //    System.out.println(propertiyName);

                switch (propertiyName)
                {
                    case "unit":
                        return productWithQR.product.getpUnitName();
                     case "pack":
                        return ProductAgent.getProductFullPackageInfo(productWithQR.product);
                    case "name":
                        return productWithQR.product.name;
                    case "url":

                        return productWithQR.qrFilePath;


                }

               return "";
            }


        };
    }

    @Override
    public InputStream getReportFile() {
        return QrProductReport.class.getClassLoader().getResourceAsStream("jasper/"+jasperFileName+".jrxml");
    }

    @Override
    public Map<String, Object> getParameters() {

      return new HashMap<>();



    }




}
