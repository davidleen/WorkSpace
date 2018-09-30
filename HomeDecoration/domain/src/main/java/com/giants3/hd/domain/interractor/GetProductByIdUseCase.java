package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;

/**根据货号查询 产品详细信息
 * Created by david on 2015/10/7.
 */
public class GetProductByIdUseCase extends DefaultUseCase {


    private long[] productIds;
    private ProductRepository productRepository;

    public GetProductByIdUseCase(long[] productId, ProductRepository productRepository) {
        super( );

        this.productIds = productId;
        this.productRepository = productRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return productRepository.loadById(productIds);
    }
}
