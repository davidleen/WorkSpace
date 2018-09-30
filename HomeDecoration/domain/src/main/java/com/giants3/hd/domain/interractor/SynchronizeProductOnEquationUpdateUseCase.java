package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;
import rx.Scheduler;

/** 公式改变时候， 进行产品表的数据同步。
 * Created by davidleen29 on 2017/3/12.
 */

public class SynchronizeProductOnEquationUpdateUseCase extends UseCase {
    private final ProductRepository productRepository;

    public SynchronizeProductOnEquationUpdateUseCase(Scheduler scheduler, Scheduler immediate, ProductRepository productRepository) {
        super(scheduler,immediate);
        this.productRepository = productRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return productRepository.synchronizeProductOnEquationUpdate() ;
    }
}
