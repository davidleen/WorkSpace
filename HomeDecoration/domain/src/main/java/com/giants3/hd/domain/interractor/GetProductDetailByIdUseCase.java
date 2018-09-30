package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;

/**根据货号id产品详细信息
 * Created by david on 2015/10/7.
 */
public class GetProductDetailByIdUseCase extends DefaultUseCase {


    private long productId;
    private ProductRepository productRepository;

    public GetProductDetailByIdUseCase(long  productId, ProductRepository productRepository) {
        super( );

        this.productId = productId;
        this.productRepository = productRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return productRepository.loadDetailById(productId);
    }
}
