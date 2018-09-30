package com.giants3.report.jasper;

import com.giants3.hd.entity.ErpOrder;
import com.giants3.hd.entity.ErpOrderItem;
import com.giants3.hd.entity.ProductPaint;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.report.PictureUrl;
import net.sf.jasperreports.engine.JRDataSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * 颜料清单报表
 * Created by david on 2016/3/13.
 */
public class ProductPaintReport extends JRPreviewReport {


    private final ProductDetail productDetail;
    private final ErpOrder order;
    private final ErpOrderItem orderItem;

    public ProductPaintReport(ProductDetail productDetail, ErpOrder order, ErpOrderItem orderItem) {
        this.productDetail = productDetail;
        this.order = order;
        this.orderItem = orderItem;


    }

    @Override
    public JRDataSource getDataSource() {


        java.util.List<ProductPaint> paintList = productDetail.paints;
        int paintSize = paintList.size();
        java.util.List<OrderProductPaint> orderProductPaints = new ArrayList<>(paintSize);
        int qty = orderItem.qty;
        for (ProductPaint productPaint : paintList) {
            OrderProductPaint orderProductPaint = new OrderProductPaint();
            orderProductPaint.setProductPaint(productPaint);
            orderProductPaint.setQty(qty);
            orderProductPaints.add(orderProductPaint);
        }
        return new CustomBeanDataSource(orderProductPaints);
    }

    @Override
    public InputStream getReportFile() {
        return ProductPaintReport.class.getClassLoader().getResourceAsStream("jasper/product_paint.jrxml");
    }

    @Override
    public Map<String, Object> getParameters() {

        OrderItemReportData orderItemReportData = new OrderItemReportData();
        orderItemReportData.orderName = order.os_no;
        int qty = orderItem.qty;
        orderItemReportData.itemQty = qty;
        orderItemReportData.amount = FloatHelper.scale((productDetail.product.paintCost + productDetail.product.paintWage) * qty);
        orderItemReportData.deliverDate = order.est_dd;
        orderItemReportData.unit = productDetail.product.pUnitName;
        orderItemReportData.pcAmount = FloatHelper.scale(productDetail.product.paintCost + productDetail.product.paintWage);
        orderItemReportData.materialCost = FloatHelper.scale(productDetail.product.paintCost);
        orderItemReportData.salary = FloatHelper.scale(productDetail.product.paintWage);

        orderItemReportData.reportDate = DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime());
        orderItemReportData.url = PictureUrl.completeUrl(orderItem.url);
        orderItemReportData.prdName = productDetail.product == null ? "test" : productDetail.product.name;

        return new ReportData(orderItemReportData,OrderItemReportData.class);
    }
}
