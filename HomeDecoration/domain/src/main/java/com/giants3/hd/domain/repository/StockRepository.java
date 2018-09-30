package com.giants3.hd.domain.repository;

import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.StockSubmit;
import com.giants3.hd.entity.StockXiaoku;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import rx.Observable;

/** 库存资源接口
 * Created by david on 2015/10/13.
 */
public interface StockRepository {


    /**
     * Get an {@link Observable} which will emit a List of {@link com.giants3.hd.entity_erp.ErpOrder}.
     * <p/>
     * 获取产品信息 根据 产品名称顺序取值
     */
    Observable<RemoteData<ErpStockOut>> getStockOutList(String key,long salesId, int pageIndex, int pageSize);

    Observable<RemoteData<ErpStockOutDetail>>  getStockOutDetail(String ck_no);

    /**
     * 保存出库详情
     * @param stockOutDetail
     * @return
     */
    Observable<RemoteData<ErpStockOutDetail>>  saveStockOutDetail(ErpStockOutDetail stockOutDetail);

    Observable<RemoteData<StockSubmit>>  getStockInAndSubmitList(String key, String startDate, String endDate);

    Observable<RemoteData<StockSubmit>> getStockXiaokuItemList(String ps_no );


    Observable<RemoteData<StockSubmit>> getStockXiaokuItemList(String key,String dateStart,String dateEnd );

    Observable<RemoteData<StockXiaoku>> getStockXiaokuList(String key,int pageIndex, int pageSize);
}
