package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;
import rx.Scheduler;

/**随机查询货物信息    参数  表示 以逗号隔开的货号列表
 * Created by david on 2015/10/7.
 */
public class ProductRandomUseCase extends DefaultUseCase {


    private String productNames;
    private ProductRepository productRepository;
    /**
     * 是否包含翻单
     */
    private boolean withCopy;

    public ProductRandomUseCase(  String productNames,boolean withCopy, ProductRepository productRepository) {
        super( );

        this.withCopy=withCopy;
        this.productNames = productNames;
        this.productRepository = productRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return productRepository.loadByProductNameRandom(productNames,withCopy);
    }
}
