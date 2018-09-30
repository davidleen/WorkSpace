package com.giants3.report.jasper.product_list_4;

import com.giants3.hd.entity.Product;
import com.giants3.report.ResourceUrl;
import com.giants3.report.jasper.JRReport;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 产品一页四个打印。
 * <p/>
 * Created by davidleen29 on 2017/3/17.
 */
public class ProductList4Report extends JRReport {


    List<ProductList4Data> datas;

    public ProductList4Report(List<Product> products) {

        datas = new ArrayList<>();

        final int size = products.size();
        for (int i = 0; i < size; i += 2) {

            ProductList4Data data = new ProductList4Data();

            int index = i;

            data.url = ResourceUrl.completeUrl(products.get(index).url);
            data.name = products.get(index).name;
            data.unit = products.get(index).pUnitName;

            int index1 = i + 1;
            if (index1 < size) {
                data.url1 = ResourceUrl.completeUrl(products.get(index1).url);
                data.name1 = products.get(index1).name;
                data.unit1 = products.get(index1).pUnitName;
            }
            datas.add(data);
        }


    }

    @Override
    public JRDataSource getDataSource() {
        return new JRBeanCollectionDataSource(datas);
    }

    @Override
    public InputStream getReportFile() {
        return getClass().getClassLoader().getResourceAsStream("jasper/product_list_4.jrxml");
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }
}
