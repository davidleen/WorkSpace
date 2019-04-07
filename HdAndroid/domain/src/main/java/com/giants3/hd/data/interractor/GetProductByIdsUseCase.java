package com.giants3.hd.data.interractor;


import com.giants3.hd.data.net.RestApi;

import rx.Observable;

/**根据货号查询 产品详细信息
 * Created by david on 2015/10/7.
 */
public class GetProductByIdsUseCase extends DefaultUseCase {


    private long[] productIds;
    private RestApi restApi;

    public GetProductByIdsUseCase(long[] productId, RestApi restApi) {
        super( );

        this.productIds = productId;
        this.restApi = restApi;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return restApi.findProductByIds(productIds);
    }
}
