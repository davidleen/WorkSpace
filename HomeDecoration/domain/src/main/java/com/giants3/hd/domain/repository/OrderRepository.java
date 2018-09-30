package com.giants3.hd.domain.repository;

import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.ErpOrderDetail;
import com.giants3.hd.noEntity.OrderReportItem;
import rx.Observable;

import java.io.File;

/**
 * Created by david on 2015/10/6.
 */
public interface OrderRepository {


    /**
     * Get an {@link Observable} which will emit a List of {@link com.giants3.hd.entity_erp.ErpOrder}.
     * <p/>
     * 获取产品信息 根据 产品名称顺序取值
     */
    Observable<RemoteData<ErpOrder>> getOrderList(String key,long salesId, int pageIndex, int pageSize);

    Observable getOrderItemList(String or_no);

    Observable searchOrderItemList(String key, int pageIndex, int pageSize);

    Observable<ErpOrderDetail> getOrderOutDetail(String os_no);

    /**
     * 保存订单详情
     * @param orderDetail
     * @return
     */
    Observable<RemoteData<ErpOrderDetail>>  saveOrderDetail(ErpOrderDetail orderDetail);

    /**
     * 订单报表查询    验货日期
     * @param   userId
     * @param dateStart
     * @param dateEnd
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Observable<RemoteData<OrderReportItem>>  getOrderReport(long userId, String dateStart, String dateEnd, int pageIndex, int pageSize);

    /**
     * 获取未出库订单货款
     * @return
     */
    Observable<RemoteData<ErpOrderItemProcess>> getUnCompleteOrderWorkFlowReport();

    Observable<RemoteData<ErpOrderItem>> getOrderWorkFlowReport(String key, int pageIndex, int pageSize);

    Observable<RemoteData<OrderItemWorkFlow>> getOrderItemWorkFlow(long orderItemId);

    /**
     * 撤销订单排厂
     * @param orderItemWorkFlowId
     * @return
     */
    Observable cancelOrderWorkFlow(long orderItemWorkFlowId);

    /**
     * 上传唛头文件
     * @param os_no
     * @param file
     * @return
     */
    Observable updateMaitouFile(String os_no, File file);
}
