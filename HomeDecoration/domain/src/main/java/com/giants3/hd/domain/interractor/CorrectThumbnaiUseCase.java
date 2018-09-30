package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;
import rx.Scheduler;

/**
 * 修复产品缩略图
 * Created by davidleen29 on 2017/3/2.
 */
public class CorrectThumbnaiUseCase extends UseCase {
    private final long productId;
    private final ProductRepository productRepository;

    public CorrectThumbnaiUseCase(Scheduler scheduler, Scheduler immediate, long productId, ProductRepository productRepository) {
        super(scheduler,immediate);
        this.productId = productId;
        this.productRepository = productRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return productRepository.correctThumbnail(productId);
    }
}
