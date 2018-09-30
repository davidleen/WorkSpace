package com.giants3.hd.domain.repository;

import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.*;

import rx.Observable;

import java.util.List;

/** 生产进度接口
 * Created by david on 20160917
 */
public interface WorkFlowRepository {


    Observable<RemoteData<WorkFlow>> getWorkFlowList();

    Observable<RemoteData<WorkFlow>> saveWorkFlowList(List<WorkFlow> workFlows);

    /**
     * 启动订单跟踪
     * @param os_no
     * @return
     */
    Observable<RemoteData<Void>> startOrderTrack(String os_no);

    Observable<RemoteData<WorkFlowSubType>> getWorkFlowSubTypes();

    Observable<RemoteData<WorkFlowProduct>>  getWorkFlowOfProduct(long productId);

    Observable<RemoteData<WorkFlowProduct>> saveWorkFlowProduct(WorkFlowProduct workFlowProduct);

    /**
     * 获取货款的进度数据
     * @param orderItemId
     * @return
     */
    Observable<RemoteData<ErpOrderItemProcess>> getOrderItemWorkFlowState(long orderItemId);

    /**
     * 报春
     * @param datas
     * @return
     */
    Observable saveWorkFlowSubTypeList(List<WorkFlowSubType> datas);

    Observable searchZhilingdan(String osName, String startDate, String endDate);

    Observable loadWorkFlowWorkers();

    Observable saveWorkFlowWorker(WorkFlowWorker workFlowWorker);

    Observable saveWorkFlowArranger(WorkFlowArranger workFlowArranger);

    Observable getWorkFlowArrangers();

    Observable deleteWorkFlowWorker(long workFlowWorkerId);

    Observable deleteWorkFlowArranger(long workFlowArrangerId);

    Observable getWorkFlowEventList();
    Observable getWorkFlowEventWorkerList();

    Observable getErpOrderItemProcess(String osNo, String prdNo);
    Observable getErpOrderItemReport(String osNo, String prdNo);

    Observable getWorkFlowAreaList();

    Observable saveWorkFlowArea(WorkFlowArea data);

    Observable deleteWorkFlowArea(long id);

    Observable getWorkFlowLimit();

    Observable saveWorkFlowLimit(List<WorkFlowTimeLimit> workFlowLimit,boolean updateCompletedOrderItem);

    Observable getSubWorkFlowList(String key, String dateStart, String dateEnd);

    Observable getWorkFlowMessageReport(String dateStart, String dateEnd, boolean unhandle, boolean overdue);
}
