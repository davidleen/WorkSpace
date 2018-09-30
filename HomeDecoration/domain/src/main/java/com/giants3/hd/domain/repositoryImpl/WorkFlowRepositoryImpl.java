package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity_erp.Sub_workflow_state;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.*;
import com.giants3.hd.entity_erp.Zhilingdan;
import com.giants3.hd.exception.HdException;

import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**
 * 生产进度
 * Created by david on  20160917
 */
public class WorkFlowRepositoryImpl extends BaseRepositoryImpl implements WorkFlowRepository {
    @Inject
    ApiManager apiManager;

    @Override
    public Observable<RemoteData<WorkFlow>> getWorkFlowList() {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlow>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlow>> subscriber) {


                try {
                    RemoteData<WorkFlow> remoteData = apiManager.getWorkFlowList();
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable<RemoteData<WorkFlow>> saveWorkFlowList(final List<WorkFlow> workFlows) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlow>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlow>> subscriber) {


                try {
                    RemoteData<WorkFlow> remoteData = apiManager.saveWorkFlowList(workFlows);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    /**
     * 启动订单跟踪
     *
     * @param os_no
     * @return
     */
    @Override
    public Observable<RemoteData<Void>> startOrderTrack(final String os_no) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {


                try {
                    RemoteData<Void> remoteData = apiManager.startOrderTrack(os_no);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });


    }


    @Override
    public Observable<RemoteData<WorkFlowSubType>> getWorkFlowSubTypes() {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowSubType>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowSubType>> subscriber) {


                try {
                    RemoteData<WorkFlowSubType> remoteData = apiManager.getWorkFlowSubTypes();
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable<RemoteData<WorkFlowProduct>> getWorkFlowOfProduct(final long productId) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowProduct>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowProduct>> subscriber) {


                try {
                    RemoteData<WorkFlowProduct> remoteData = apiManager.getWorkFlowOfProduct(productId);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }


    @Override
    public Observable<RemoteData<WorkFlowProduct>> saveWorkFlowProduct(final WorkFlowProduct workFlowProduct) {


        return crateObservable(new ApiCaller<WorkFlowProduct>() {
            @Override
            public RemoteData<WorkFlowProduct> call() throws HdException {
                return apiManager.saveWorkFlowProduct(workFlowProduct);
            }
        });



    }


    /**
     * 获取货款的进度数据
     *
     * @param orderItemId
     * @return
     */
    @Override
    public Observable<RemoteData<ErpOrderItemProcess>> getOrderItemWorkFlowState(final long orderItemId) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<ErpOrderItemProcess>>() {
            @Override
            public void call(Subscriber<? super RemoteData<ErpOrderItemProcess>> subscriber) {


                try {
                    RemoteData<ErpOrderItemProcess> remoteData = apiManager.getOrderItemWorkFlowState(orderItemId);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }


    /**
     * 保存排厂类型列表
     *
     * @param datas
     * @return
     */
    @Override
    public Observable saveWorkFlowSubTypeList(final List<WorkFlowSubType> datas) {


        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowSubType>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowSubType>> subscriber) {


                try {
                    RemoteData<WorkFlowSubType> remoteData = apiManager.saveWorkFlowSubTypeList(datas);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });


    }


    @Override
    public Observable searchZhilingdan(final String osName, final String startDate, final String endDate) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Zhilingdan>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Zhilingdan>> subscriber) {


                try {
                    RemoteData<Zhilingdan> remoteData = apiManager.searchZhiling(osName, startDate, endDate);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable loadWorkFlowWorkers() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowWorker>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowWorker>> subscriber) {


                try {
                    RemoteData<WorkFlowWorker> remoteData = apiManager.loadWorkFlowWorks();
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }


    @Override
    public Observable saveWorkFlowWorker(final WorkFlowWorker workFlowWorker) {

        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowWorker>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowWorker>> subscriber) {


                try {
                    RemoteData<WorkFlowWorker> remoteData = apiManager.saveWorkFlowWorker(workFlowWorker);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });


    }

    @Override
    public Observable saveWorkFlowArranger(final WorkFlowArranger workFlowArranger) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowArranger>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowArranger>> subscriber) {


                try {
                    RemoteData<WorkFlowArranger> remoteData = apiManager.saveWorkFlowArranger(workFlowArranger);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }


    @Override
    public Observable getWorkFlowArrangers() {
        return Observable.create(new Observable.OnSubscribe<RemoteData<WorkFlowArranger>>() {
            @Override
            public void call(Subscriber<? super RemoteData<WorkFlowArranger>> subscriber) {


                try {
                    RemoteData<WorkFlowArranger> remoteData = apiManager.getWorkFlowArrangers();
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable deleteWorkFlowWorker(final long workFlowWorkerId) {
        return Observable.create(new Observable.OnSubscribe<RemoteData<Void>>() {
            @Override
            public void call(Subscriber<? super RemoteData<Void>> subscriber) {


                try {
                    RemoteData<Void> remoteData = apiManager.deleteWorkFlowWorker(workFlowWorkerId);
                    if (remoteData.isSuccess()) {
                        subscriber.onNext(remoteData);
                        subscriber.onCompleted();

                    } else {
                        subscriber.onError(HdException.create(remoteData.message));

                    }

                } catch (HdException e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable deleteWorkFlowArranger(final long workFlowArrangerId) {

     return    crateObservable(new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {
              return   apiManager.deleteWorkFlowArranger(workFlowArrangerId);
            }
        });

    }

    @Override
    public Observable getWorkFlowEventList() {
        return crateObservable(new ApiCaller<WorkFlowEvent>() {
            @Override
            public RemoteData<WorkFlowEvent> call() throws HdException {
                return apiManager.getWorkFLowEventList();
            }
        });
    }


    @Override
    public Observable getWorkFlowEventWorkerList() {
        return crateObservable(new ApiCaller<WorkFlowEventWorker>() {
            @Override
            public RemoteData<WorkFlowEventWorker> call() throws HdException {
                return apiManager.getWorkFLowEventWorkerList();
            }
        });
    }


    @Override
    public Observable getErpOrderItemProcess(final String osNo, final String prdNo) {
        return crateObservable(new ApiCaller<ErpOrderItemProcess>() {
            @Override
            public RemoteData<ErpOrderItemProcess> call() throws HdException {
                return apiManager.getErpOrderItemProcess(  osNo,  prdNo);
            }
        });
    }
    @Override
    public Observable getErpOrderItemReport(final String osNo, final String prdNo) {
        return crateObservable(new ApiCaller<ErpWorkFlowReport>() {
            @Override
            public RemoteData<ErpWorkFlowReport> call() throws HdException {
                return apiManager.getErpOrderItemReport(  osNo,  prdNo);
            }
        });
    }


    @Override
    public Observable getWorkFlowAreaList() {
        return crateObservable(  new ApiCaller<WorkFlowArea>() {
            @Override
            public RemoteData<WorkFlowArea> call() throws HdException {
                return apiManager.getWorkFlowAreas(   );
            }
        });
    }

    @Override
    public Observable saveWorkFlowArea(final WorkFlowArea data) {
        return crateObservable(  new ApiCaller<WorkFlowArea>() {
            @Override
            public RemoteData<WorkFlowArea> call() throws HdException {
                return apiManager.saveWorkFlowArea(  data );
            }
        });
    }  @Override
    public Observable deleteWorkFlowArea(final long id) {
        return crateObservable(  new ApiCaller<WorkFlowArea>() {
            @Override
            public RemoteData<WorkFlowArea> call() throws HdException {
                return apiManager.deleteWorkFlowArea(  id );
            }
        });
    }

    @Override
    public Observable getWorkFlowLimit() {

        return crateObservable(  new ApiCaller<WorkFlowTimeLimit>() {
            @Override
            public RemoteData<WorkFlowTimeLimit> call() throws HdException {
                return apiManager.getWorkFlowLimit(    );
            }
        });

    }

    @Override
    public Observable saveWorkFlowLimit(final List<WorkFlowTimeLimit> workFlowLimit, final boolean updateCompletedOrderItem) {
        return crateObservable(  new ApiCaller<Void>() {
            @Override
            public RemoteData<Void> call() throws HdException {
                return apiManager.saveWorkFlowLimit(    workFlowLimit,  updateCompletedOrderItem);
            }
        });
    }

    @Override
    public Observable getSubWorkFlowList(final String key, final String dateStart, final String dateEnd) {
        return crateObservable(  new ApiCaller<Sub_workflow_state>() {
            @Override
            public RemoteData<Sub_workflow_state> call() throws HdException {
                return apiManager.searchErpSubWorkFlow(    key,dateStart,dateEnd);
            }
        });
    }

    @Override
    public Observable getWorkFlowMessageReport(final String dateStart, final String dateEnd, final boolean unhandle, final boolean overdue) {


        return crateObservable(  new ApiCaller<WorkFlowMessage>() {
            @Override
            public RemoteData<WorkFlowMessage> call() throws HdException {
                return apiManager.getWorkFlowMessageReport(   dateStart,   dateEnd,   unhandle,   overdue);
            }
        });
    }
}

