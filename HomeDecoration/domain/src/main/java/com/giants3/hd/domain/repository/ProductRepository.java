package com.giants3.hd.domain.repository;


import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Product;
import com.giants3.hd.noEntity.ProductDetail;
import rx.Observable;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public interface ProductRepository {


    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Product}.
     *
     * 获取产品信息 根据 产品名称顺序取值
     *
     */
    Observable<List<Product>> loadByProductNameBetween(String startName, String endName, boolean withCopy);


    Observable<RemoteData<Product>> loadByProductNameRandom(String productNames, boolean withCopy);

    Observable<ProductDetail> loadByProductNo(String prdNo);

    Observable<RemoteData<Void>> correctThumbnail(long productId);

    /**
     *  公式改变时候， 进行产品表的数据同步。
     * @return
     */
    Observable<RemoteData<Void>> synchronizeProductOnEquationUpdate();

    Observable<RemoteData<Void>> syncRelateProductPicture();

    Observable<RemoteData<Product>> loadById(long[] productId);

    Observable<RemoteData<ProductDetail>> loadDetailById(long productId);
}
